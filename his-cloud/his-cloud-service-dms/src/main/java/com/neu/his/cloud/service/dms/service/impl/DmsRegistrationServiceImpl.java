package com.neu.his.cloud.service.dms.service.impl;

import com.neu.his.cloud.service.dms.WxPay.MyWXPay;
import com.neu.his.cloud.service.dms.common.CommonResult;
import com.neu.his.cloud.service.dms.dto.app.AppRegistrationParam;
import com.neu.his.cloud.service.dms.dto.dms.DmsRegHistoryResult;
import com.neu.his.cloud.service.dms.dto.dms.DmsRegistrationParam;
import com.neu.his.cloud.service.dms.dto.dms.WXDmsRegistrationParam;
import com.neu.his.cloud.service.dms.dto.dms.WxProgramResultsParam;
import com.neu.his.cloud.service.dms.mapper.*;
import com.neu.his.cloud.service.dms.model.*;
import com.neu.his.cloud.service.dms.service.DmsRegistrationService;
import com.neu.his.cloud.service.dms.util.AgeStrUtil;
import com.neu.his.cloud.service.dms.util.DateUtil;
import io.swagger.models.auth.In;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class DmsRegistrationServiceImpl implements DmsRegistrationService {
    @Autowired
    private PmsPatientMapper pmsPatientMapper;
    @Autowired
    private SmsSkdMapper smsSkdMapper;
    @Autowired
    private SmsStaffMapper smsStaffMapper;
    @Autowired
    private SmsRegistrationRankMapper smsRegistrationRankMapper;
    @Autowired
    private DmsRegistrationMapper dmsRegistrationMapper;
    @Autowired
    private BmsBillsRecordMapper bmsBillsRecordMapper;
    @Autowired
    private BmsInvoiceRecordMapper bmsInvoiceRecordMapper;
    @Autowired
    private  SmsDeptMapper smsDeptMapper;

    @Autowired
    private BladeDictMapper bladeDictMapper;

    @Autowired
    private WxResultsMapper wxResultsMapper;

    @Autowired
    private BmsAccountMapper bmsAccountMapper;

    @Autowired
    private BmsRechargeMapper bmsRechargeMapper;

    @Autowired
    private BmsRollbackMapper bmsRollbackMapper;

    //1.调用PmsPatientDao根据身份证号查询是否存在
    //2.1如果不存在，则向PmsPatient表中插入数据，返回id
    //3.判断是否为专家号
    //3.1 如果为专家号，则先根据skd_id判断remain是否>0，如果大于0，则向dms_registration插入信息，并且绑定医生（skd_id、bind_status=1）,并修改sms_skd中的排班限额（-1），否则挂号失败
    //3.2 如果为非专家号，则向dms_registration插入信息，bin_status=0
    //4.向bms_bills_record中插入账单记录
    //5. 通过门诊号和最近时间返回账单id
    //6.插入发票记录
    @Transactional(propagation= Propagation.REQUIRED)
    @Override
    public int createRegistration(WXDmsRegistrationParam dmsRegistrationParam){
        PmsPatientExample example = new PmsPatientExample();
        example.createCriteria().andIdentificationNoEqualTo(dmsRegistrationParam.getIdentificationNo());
        List<PmsPatient> pmsPatientList = pmsPatientMapper.selectByExample(example);
        if (pmsPatientList.size() == 0) {//如果不存在，则向PmsPatient表中插入数据
            PmsPatient pmsPatient = new PmsPatient();
            BeanUtils.copyProperties(dmsRegistrationParam, pmsPatient);
            //生成病历号
            String medicalRecordNo = generateMedicalRecordNo(dmsRegistrationParam.getIdentificationNo());
            pmsPatient.setMedicalRecordNo(medicalRecordNo);
            pmsPatientMapper.insertSelective(pmsPatient);
        }
        //通过身份证号查询并返回病人id
        PmsPatientExample example1 = new PmsPatientExample();
        example1.createCriteria().andIdentificationNoEqualTo(dmsRegistrationParam.getIdentificationNo());
        List<PmsPatient> pmsPatientList1 = pmsPatientMapper.selectByExample(example1);
        if(!pmsPatientList1.isEmpty()){
            Long patientId = pmsPatientList1.get(0).getId();

            //创建要插入的DmsRegistration对象
            DmsRegistration dmsRegistration = new DmsRegistration();
            BeanUtils.copyProperties(dmsRegistrationParam, dmsRegistration);
            dmsRegistration.setPatientId(patientId);
            Date createDate = new Date();//获取当前时间，并在之后通过该时间与病人id获取挂号id
            dmsRegistration.setCreateTime(createDate);
            dmsRegistration.setStatus(1);//挂号状态为待诊1

            SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            try {
                String format = sdf1.format(dmsRegistration.getAttendanceDate())+" "+dmsRegistrationParam.getTime()+":00";
                dmsRegistration.setAttendanceDate(sdf2.parse(format));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //处理年龄
            Date dateOfBirth = dmsRegistrationParam.getDateOfBirth();
            String age = AgeStrUtil.getAgeStr(dateOfBirth);
            dmsRegistration.setPatientAgeStr(age);

            Long registrationId;//用于存储插入的挂号信息的id

            if (dmsRegistrationParam.getSkdId() == null){//非专家号
                dmsRegistration.setBindStatus(0);//非专家号绑定状态为0
                dmsRegistrationMapper.insertSelective(dmsRegistration);//向dms_registration插入信息

                registrationId = dmsRegistration.getId();
            }
            else{//专家号
                SmsSkdExample example2 = new SmsSkdExample();
                example2.createCriteria().andIdEqualTo(dmsRegistrationParam.getSkdId());
                List<SmsSkd> smsSkdList = smsSkdMapper.selectByExample(example2);
                if (!smsSkdList.isEmpty()){
                    if (smsSkdList.get(0).getRemain() > 0) {//如果还有挂号限额
                        dmsRegistration.setBindStatus(1);//专家号绑定状态为1
                        dmsRegistrationMapper.insertSelective(dmsRegistration);//向dms_registration插入信息
                        registrationId = dmsRegistration.getId();

                        SmsSkd smsSkd = smsSkdList.get(0);
                        smsSkd.setRemain(smsSkdList.get(0).getRemain() - 1);
                        //smsSkdList.get(0).setRemain(smsSkdList.get(0).getRemain()-1);
                        smsSkdMapper.updateByPrimaryKey(smsSkd);//修改sms_skd中的排班限额（-1）
                        //return 1;
                    } else {//挂号限额已用完，挂号失败
                        return 0;
                    }
                }
                else {
                    return 0;//无该排班信息,挂号失败
                }

            }
            BmsBillsRecord bmsBillsRecord = new BmsBillsRecord();//保存除了发票号以外的所有属性

            //生成账单号
            String billNo = generateBillNo(registrationId);
            bmsBillsRecord.setBillNo(billNo);
            Date billCreateDate = new Date();//获取当前时间，并在之后通过该时间与病人id获取账单id
            bmsBillsRecord.setCreateTime(billCreateDate);
            bmsBillsRecord.setStatus(1);//1为正常
            bmsBillsRecord.setInvoiceNum(1);//挂号只有一张发票
            bmsBillsRecord.setRegistrationId(registrationId);
            bmsBillsRecord.setRecordList(registrationId + "," + 0 + "><");//recordList怎么用字符串表示,例如挂号id,0><草药id,4><成药,3
            bmsBillsRecordMapper.insertSelective(bmsBillsRecord);//向bms_bills_record中插入账单记录
            //通过挂号id和最近时间返回账单id
            BmsBillsRecordExample example5 = new BmsBillsRecordExample();
            example5.createCriteria().andRegistrationIdEqualTo(registrationId).andBillNoEqualTo(billNo);////通过挂号id和账单号查询刚插入账单的id
            List<BmsBillsRecord> bmsBillsRecordList = bmsBillsRecordMapper.selectByExample(example5);
            Long billId = bmsBillsRecordList.get(0).getId();//账单id

            //插入发票记录
            BmsInvoiceRecord bmsInvoiceRecord = new BmsInvoiceRecord();
            bmsInvoiceRecord.setType(1);//1表示挂号
            Date invoiceCreateTime = new Date();
            bmsInvoiceRecord.setCreateTime(invoiceCreateTime);
            bmsInvoiceRecord.setInvoiceNo(dmsRegistrationParam.getInvoiceNo());
            bmsInvoiceRecord.setBillId(billId);
            bmsInvoiceRecord.setAmount(dmsRegistrationParam.getAmount());
            // bmsInvoiceRecord.setFreezeStatus(1);//冻结状态为1正常
            bmsInvoiceRecord.setOperatorId(dmsRegistrationParam.getOpratorId());
            bmsInvoiceRecord.setSettlementCatId(dmsRegistrationParam.getSettlementCatId());
            bmsInvoiceRecord.setItemList(registrationId + "," + 0 + "," + dmsRegistrationParam.getAmount() + "><");//挂号id,0,amount><
            bmsInvoiceRecordMapper.insertSelective(bmsInvoiceRecord);


            if(dmsRegistrationParam.getSettlementCatId()==6){
                Map<String, String> stringStringMap = WxPay(dmsRegistrationParam);
                if(!CollectionUtils.isEmpty(stringStringMap) && stringStringMap.get("return_code").equals("SUCCESS") && stringStringMap.get("result_code").equals("SUCCESS")){
                    return insertBmsAccount(dmsRegistrationParam);
                }else {
                    String string  = null;
                    if(string.equals("")) {
                        int i = 0;
                    }
                }
            }
            return WXXinsertBmsAccount(dmsRegistrationParam);
        }
        return 0;
    }


    @Override
    public List<DmsRegHistoryResult> listRegHistory(String identificationNo) {
        List<DmsRegHistoryResult> dmsRegHistoryResultList = new ArrayList<>();
        PmsPatientExample pmsPatientExample = new PmsPatientExample();
        pmsPatientExample.createCriteria().andIdentificationNoEqualTo(identificationNo);
        List<PmsPatient> pmsPatientList = pmsPatientMapper.selectByExample(pmsPatientExample);
        if (!pmsPatientList.isEmpty()) {//如果有对应的患者记录
            PmsPatient pmsPatient = pmsPatientList.get(0);
            DmsRegistrationExample dmsRegistrationExample = new DmsRegistrationExample();
            dmsRegistrationExample.createCriteria().andPatientIdEqualTo(pmsPatient.getId());
            dmsRegistrationExample.setOrderByClause("create_time desc");
            List<DmsRegistration> dmsRegistrationList = dmsRegistrationMapper.selectByExample(dmsRegistrationExample);
            for (DmsRegistration dmsRegistration : dmsRegistrationList) {
                DmsRegHistoryResult dmsRegHistoryResult = new DmsRegHistoryResult();
                BeanUtils.copyProperties(dmsRegistration,dmsRegHistoryResult);
                //挂号（门诊）id
                dmsRegHistoryResult.setRegistrationId(dmsRegistration.getId());
                //科室名
                SmsDept smsDept = smsDeptMapper.selectByPrimaryKey(dmsRegistration.getDeptId());
                if (smsDept != null){
                    dmsRegHistoryResult.setDeptName(smsDept.getName());
                }
                //医生姓名,挂号级别名
                SmsSkd smsSkd = smsSkdMapper.selectByPrimaryKey(dmsRegistration.getSkdId());
                if (smsSkd != null){
                    SmsStaff smsStaff = smsStaffMapper.selectByPrimaryKey(smsSkd.getStaffId());
                    dmsRegHistoryResult.setDoctorName(smsStaff.getName());
                    SmsRegistrationRank smsRegistrationRank = smsRegistrationRankMapper.selectByPrimaryKey(smsStaff.getRegistrationRankId());
                    dmsRegHistoryResult.setRegistrationName(smsRegistrationRank.getName());
                }
                dmsRegHistoryResultList.add(dmsRegHistoryResult);
            }
            return dmsRegHistoryResultList;
        }
        return null;
    }


    //先根据skd_id判断remain是否>0，如果大于0，则向dms_registration插入信息，并且绑定医生（skd_id、bind_status=1）,并修改sms_skd中的排班限额（-1），否则挂号失败
    //向bms_bills_record中插入账单记录
    @Override
    public int appRegistration(AppRegistrationParam appRegistrationParam){
        PmsPatient patient = pmsPatientMapper.selectByPrimaryKey(appRegistrationParam.getPatientId());
        if(patient == null){
            return 0;
        }
        SmsSkd skd = smsSkdMapper.selectByPrimaryKey(appRegistrationParam.getSkdId());
        if(skd == null){
            return 0;
        }
        //先根据skd_id判断remain是否>0
        if(skd.getRemain() <= 0){
            return  0;
        }

        //如果大于0，绑定医生（skd_id、bind_status=1）,并修改sms_skd中的排班限额（-1）
        SmsSkd skdRecord = new SmsSkd();
        skdRecord.setRemain(skd.getRemain() - 1);
        SmsSkdExample example = new SmsSkdExample();
        example.createCriteria().andIdEqualTo(skd.getId()).andRemainGreaterThan(new Long(0));
        int count = smsSkdMapper.updateByExampleSelective(skdRecord,example);
        if(count <= 0){
            return 0;
        }

        //向dms_registration插入信息
        DmsRegistration registration = new DmsRegistration();
        registration.setPatientId(appRegistrationParam.getPatientId());
        registration.setCreateTime(new Date());
        registration.setStatus(1);
        registration.setSkdId(skd.getId());
        registration.setNeedBook(0);    //默认不需要病历本
        registration.setBindStatus(1);
        registration.setDeptId(appRegistrationParam.getDeptId());
        registration.setAttendanceDate(appRegistrationParam.getAttendanceDate());
        if(patient.getDateOfBirth() != null){
            registration.setPatientAgeStr(AgeStrUtil.getAgeStr(patient.getDateOfBirth()));
        }
        int regCount = dmsRegistrationMapper.insertSelective(registration);

        //得到刚插入的挂号记录的registrationId
        DmsRegistrationExample registrationExample = new DmsRegistrationExample();
        registrationExample.createCriteria()
                .andPatientIdEqualTo(appRegistrationParam.getPatientId())
                .andSkdIdEqualTo(appRegistrationParam.getSkdId());//通过病人id和skdId查询刚插入挂号记录的id
        registrationExample.setOrderByClause("create_time desc");   //获取最新一次的
        List<DmsRegistration> dmsRegistrationList = dmsRegistrationMapper.selectByExample(registrationExample);
        if(dmsRegistrationList.size() <= 0){
            return 0;
        }
        Long registrationId = dmsRegistrationList.get(0).getId();

        //向bms_bills_record中插入账单记录
        BmsBillsRecord billsRecord = new BmsBillsRecord();
        //生成账单号
        String billNo = generateBillNo(registrationId);
        billsRecord.setBillNo(billNo);
        billsRecord.setCreateTime(new Date());
        billsRecord.setStatus(1);
        billsRecord.setInvoiceNum(0);   //免费挂号，不产生发票
        billsRecord.setRegistrationId(registrationId);
        billsRecord.setRecordList(registrationId + "," + 0 + "><");//recordList怎么用字符串表示,例如挂号id,0><草药id,4><成药,3
        int billCount = bmsBillsRecordMapper.insertSelective(billsRecord);//向bms_bills_record中插入账单记录

        return billCount;
    }


    //生成病历号
    public String generateMedicalRecordNo(String identificationNo) {
        Date date = new Date();
        String yyyymmdd = DateUtil.getDateStr(date);//年月日字符串
        String hhmm = DateUtil.getTimeStr(date, 4);//时分字符串
        int length = identificationNo.length();
        String lastFour;
        if (length > 4) {
            lastFour = identificationNo.substring(length - 4, length);
            return yyyymmdd + hhmm + lastFour;
        }
        return null;
    }

    //生成账单号
    public String generateBillNo(Long registrationId) {
        Date date = new Date();
        String yyyymmdd = DateUtil.getDateStr(date);//年月日字符串
        String hhmm = DateUtil.getTimeStr(date, 4);//时分字符串
        int length = registrationId.toString().length();
        String lastFour;
        if (length >= 4) {
            Long lastFourNo = registrationId % 10000;
            lastFour = lastFourNo.toString();
        } else if (length == 3) {
            lastFour = "0" + registrationId.toString();
        } else if (length == 2) {
            lastFour = "00" + registrationId.toString();
        } else {
            lastFour = "000" + registrationId.toString();
        }
        return yyyymmdd + hhmm + lastFour;
    }

    @Override
    public List<String> TimeDifference(String ruletime,Long skdId,int noon) {

        Date morningStart=null;  //上午开始时间
        Date morningEnd = null;//上午结束始时间
        Date afternoonStart=null;//下午开始时间
        Date afternoonEnd=null;//下午结束时间
        int time1 = 0;  //上午时间间隔
        int time2 = 0;  //下午时间间隔

        List<String> timeList=new ArrayList<>();
        List<Date> morningList = new ArrayList<>();
        List<Date> afternoonList=new ArrayList<>();

        //查出SkdId下的所有时间
        DmsRegistrationExample dmsRegistrationExample=new DmsRegistrationExample();
        dmsRegistrationExample.createCriteria().andSkdIdEqualTo(skdId);
        List<DmsRegistration> dmsRegistrationList = dmsRegistrationMapper.selectByExample(dmsRegistrationExample);

        //查询数据库的到上班时间
        BladeDictExample bladeDictExample=new BladeDictExample();
        bladeDictExample.createCriteria().andCodeEqualTo("time");
        List<BladeDict> bladeDicts = bladeDictMapper.selectByExample(bladeDictExample);

        //诊治病人的数量
        Long skLimit = smsSkdMapper.selectByPrimaryKey(skdId).getSkLimit();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(BladeDict bladeDict:bladeDicts){
            try {
                if(bladeDict.getDictKey()==1){
                    morningStart=formatter.parse("2020-12-12 "+bladeDict.getDictValue()+":00");
                }else if(bladeDict.getDictKey()==2){
                    morningEnd=formatter.parse("2020-12-12 "+bladeDict.getDictValue()+":00");
                }else if(bladeDict.getDictKey()==3){
                    afternoonStart=formatter.parse("2020-12-12 "+bladeDict.getDictValue()+":00");
                }else if(bladeDict.getDictKey()==4){
                    afternoonEnd=formatter.parse("2020-12-12 "+bladeDict.getDictValue()+":00");
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        //上下午每个病人诊治时长
        if(morningStart != null && morningEnd != null && afternoonStart != null && afternoonEnd != null){
            time1 = (int) ((morningEnd.getTime() - morningStart.getTime())/60000/skLimit);
            time2 = (int) ((afternoonEnd.getTime() - afternoonStart.getTime())/60000/skLimit);
        }

        if(noon==0) {
            morningList.add(morningStart);
            for (int i = 0; i < skLimit - 1; i++) {
                Date date = getNewDate(morningList.get(i), time1);
                morningList.add(date);
            }
        }else {
            afternoonList.add(afternoonStart);
            for(int i=0;i<skLimit-1;i++){
                Date date=getNewDate(afternoonList.get(i),time2);
                afternoonList.add(date);
            }
        }
        if(morningList!=null){
            morningList.stream().forEach(mornings->{
                String format = formatter.format(mornings).substring(11,16);
                timeList.add(format);
            });
        }
        if(afternoonList!=null) {
            afternoonList.stream().forEach(afternoons -> {
                String format = formatter.format(afternoons).substring(11, 16);
                timeList.add(format);
            });
        }


        if(!CollectionUtils.isEmpty(dmsRegistrationList)){
            dmsRegistrationList.stream().forEach(dmsRegistration -> {
                Iterator<String> iterator = timeList.iterator();
                while (iterator.hasNext()){
                    String format = formatter.format(dmsRegistration.getAttendanceDate());
                    if(format.equals(ruletime+" "+iterator.next()+":00")){
                        iterator.remove();
                    }
                }
            });
        }

        return timeList;
    }

    public Date getNewDate(Date cur,int time) {
        Calendar c = Calendar.getInstance();
        c.setTime(cur);   //设置时间
        c.add(Calendar.MINUTE, time);
        Date date = c.getTime(); //结果
        return date;
    }

    @Override
    public Map<String, String> WxPay(WXDmsRegistrationParam dmsRegistrationParam){
        //微信付款码支付
        try {
            if(!StringUtils.isEmpty(dmsRegistrationParam.getBarCode())){
                String multiply = dmsRegistrationParam.getAmount().multiply(BigDecimal.valueOf(100)).intValue()+"";
                System.out.println(multiply.toString());
                Map<String, String> stringStringMap = MyWXPay.scanCodeToPay(dmsRegistrationParam.getBarCode(), multiply);

                if(!CollectionUtils.isEmpty(stringStringMap)){
                    PmsPatientExample pmsPatientExample = new PmsPatientExample();
                    pmsPatientExample.createCriteria().andIdentificationNoEqualTo(dmsRegistrationParam.getIdentificationNo());
                    List<PmsPatient> pmsPatients = pmsPatientMapper.selectByExample(pmsPatientExample);
                    if(!pmsPatients.isEmpty()){
                        Long patientid = pmsPatients.get(0).getId();
                        WxPayResult(stringStringMap,patientid);
                        return stringStringMap;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }//付款码支付结束
        return null;
    }


    @Override
    public BmsAccount SelectAccountByCardId(WXDmsRegistrationParam wxDmsRegistrationParam) {
        PmsPatientExample pmsPatientExample=new PmsPatientExample();
        pmsPatientExample.createCriteria().andIdentificationNoEqualTo(wxDmsRegistrationParam.getIdentificationNo());
        List<PmsPatient> pmsPatients = pmsPatientMapper.selectByExample(pmsPatientExample);
        if(!CollectionUtils.isEmpty(pmsPatients)){
            BmsAccountExample bmsAccountExample=new BmsAccountExample();
            bmsAccountExample.createCriteria().andPatientIdEqualTo(pmsPatients.get(0).getId());
            List<BmsAccount> bmsAccounts = bmsAccountMapper.selectByExample(bmsAccountExample);
            if (!CollectionUtils.isEmpty(bmsAccounts)){
                return bmsAccounts.get(0);
            }
        }
        return null;
    }

    @Override
    public int insertBmsAccount(WXDmsRegistrationParam wxDmsRegistrationParam) {

        //根据身份证号查看病人信息
        PmsPatientExample pmsPatientExample=new PmsPatientExample();
        pmsPatientExample.createCriteria().andIdentificationNoEqualTo(wxDmsRegistrationParam.getIdentificationNo());
        List<PmsPatient> pmsPatients = pmsPatientMapper.selectByExample(pmsPatientExample);

        if(!CollectionUtils.isEmpty(pmsPatients)){

            //查看病人是不是有账户
            BmsAccount bmsAccount1 = SelectAccountByCardId(wxDmsRegistrationParam);
            //如果没有新建账户
            if(StringUtils.isEmpty(bmsAccount1)){
                BmsAccount bmsAccount=new BmsAccount();
                bmsAccount.setPatientId(pmsPatients.get(0).getId());
                bmsAccount.setAccountCode(UUID.randomUUID().toString().trim().replaceAll("-", ""));
                bmsAccount.setBlance(BigDecimal.valueOf(0));
                bmsAccount.setFrozen(BigDecimal.valueOf(0));
                bmsAccount.setSummery(wxDmsRegistrationParam.getAmount());
                bmsAccount.setCardId(wxDmsRegistrationParam.getIdentificationNo());
                bmsAccount.setLevel(1);
                bmsAccount.setAccountStatus(1);
                bmsAccount.setCreateUser(wxDmsRegistrationParam.getOpratorId());
                bmsAccount.setCreateTime(new Date());
                bmsAccount.setUpdateUser(wxDmsRegistrationParam.getOpratorId());
                bmsAccount.setUpdateTime(new Date());
                bmsAccount.setIsDeleted(0);
                return bmsAccountMapper.insertSelective(bmsAccount);
            }else {
                bmsAccount1.setUpdateUser(wxDmsRegistrationParam.getOpratorId());
                bmsAccount1.setUpdateTime(new Date());
                bmsAccount1.setSummery(bmsAccount1.getSummery().add(wxDmsRegistrationParam.getAmount()));
                return bmsAccountMapper.updateByPrimaryKey(bmsAccount1);
            }
        }
        return 0;
    }

    @Override
    public int recharge(WXDmsRegistrationParam wxDmsRegistrationParam) {
        //充值金额>0
        if(wxDmsRegistrationParam.getTopUp().compareTo(BigDecimal.valueOf(0))>=0){

            //查询该病人的账号信息
            BmsAccount bmsAccount = SelectAccountByCardId(wxDmsRegistrationParam);
            if(!StringUtils.isEmpty(bmsAccount)){

                //插入充值信息
                BmsRecharge bmsRecharge=new BmsRecharge();
                bmsRecharge.setAccountCode(bmsAccount.getAccountCode());
                bmsRecharge.setAmout(wxDmsRegistrationParam.getTopUp());
                bmsRecharge.setChannel(Integer.parseInt(wxDmsRegistrationParam.getSettlementCatId().toString()));
                bmsRecharge.setRechargeTime(new Date());
                bmsRecharge.setRechargeStates(0);
                bmsRecharge.setCreateUser(wxDmsRegistrationParam.getOpratorId());
                bmsRecharge.setCreateTime(new Date());
                bmsRecharge.setIsDeleted(0);
                int recharge = bmsRechargeMapper.insert(bmsRecharge);
                boolean isSuccess = false;
                //判断充值是否成功
                if(recharge>0){
                    //微信支付
                    wxDmsRegistrationParam.setAmount(wxDmsRegistrationParam.getTopUp());
                    if(wxDmsRegistrationParam.getSettlementCatId()==6){
                        Map<String, String> stringStringMap = WxPay(wxDmsRegistrationParam);
                        bmsRecharge.setOutTradeId(stringStringMap.get(stringStringMap.get("transaction_id")));
                        bmsRecharge.setResult(stringStringMap.get("results"));
                        if(!CollectionUtils.isEmpty(stringStringMap) && stringStringMap.get("return_code").equals("SUCCESS") && stringStringMap.get("result_code").equals("SUCCESS")){
                            isSuccess = true;
                        }
                    } else {
                        //由人工确认充值结果，此处默认充值成功！！！！
                        isSuccess = true;
                    }
                    //设置充值表的字段信息（封装充值结果）
                    bmsRecharge.setUpdateUser(wxDmsRegistrationParam.getOpratorId());
                    bmsRecharge.setUpdateTime(new Date());
                    //充值成功
                    if(isSuccess){
                        //修改充值状态成功
                        bmsRecharge.setRechargeStates(1);
                        //修改账户表数据
                        bmsAccount.setSummery(bmsAccount.getSummery().add(wxDmsRegistrationParam.getAmount()));
                        bmsAccount.setBlance(bmsAccount.getBlance().add(wxDmsRegistrationParam.getAmount()));
                        bmsAccount.setUpdateTime(new Date());
                        bmsAccount.setUpdateUser(wxDmsRegistrationParam.getOpratorId());
                        bmsAccountMapper.updateByPrimaryKey(bmsAccount);
                    }else {
                        //修改充值状态失败
                        bmsRecharge.setRechargeStates(2);
                    }
                    return bmsRechargeMapper.updateByPrimaryKey(bmsRecharge);
                }
            }
        }
        return 0;
    }

    @Override
    public int rollback(WXDmsRegistrationParam wxDmsRegistrationParam) {
        //判断数据的合理性
        if(wxDmsRegistrationParam.getRefund().compareTo(BigDecimal.valueOf(0))>=0 && wxDmsRegistrationParam.getRefund().compareTo(wxDmsRegistrationParam.getBlance())<=0){
            BmsAccount bmsAccount = SelectAccountByCardId(wxDmsRegistrationParam);
            BmsRollback bmsRollback=new BmsRollback();
            bmsRollback.setAccountCode(bmsAccount.getAccountCode());
            bmsRollback.setRbAmount(wxDmsRegistrationParam.getRefund());
            bmsRollback.setRbState(1);
            bmsRollback.setAppTime(new Date());
            bmsRollback.setRbType(1);
            bmsRollback.setCreateUser(wxDmsRegistrationParam.getOpratorId());
            bmsRollback.setCreateTime(new Date());
            bmsRollback.setUpdateUser(wxDmsRegistrationParam.getOpratorId());
            bmsRollback.setUpdateTime(new Date());
            bmsRollback.setIsDeleted(0);
            int insertRollback = bmsRollbackMapper.insert(bmsRollback);
            if(insertRollback>0){
                bmsAccount.setBlance(bmsAccount.getBlance().subtract(wxDmsRegistrationParam.getRefund()));
                bmsAccount.setSummery(bmsAccount.getSummery().subtract(wxDmsRegistrationParam.getRefund()));
                bmsAccount.setUpdateUser(wxDmsRegistrationParam.getOpratorId());
                bmsAccount.setUpdateTime(new Date());
                int accountupdate = bmsAccountMapper.updateByPrimaryKey(bmsAccount);
                if(accountupdate>0){
                    bmsRollback.setRbState(4);
                    bmsRollback.setUpdateUser(wxDmsRegistrationParam.getOpratorId());
                    bmsRollback.setUpdateTime(new Date());
                    return bmsRollbackMapper.updateByPrimaryKey(bmsRollback);
                }

            }
        }
        return 0;
    }

    @Override
    public int WxPayResult(Map<String, String> stringStringMap,Long patientid) {
        try {
            WxResults wxResults=new WxResults();
            String transaction_id = stringStringMap.get("transaction_id");
            String openid = stringStringMap.get("openid");
            String mch_id = stringStringMap.get("mch_id");
            String out_trade_no = stringStringMap.get("out_trade_no");
            String total_fee = stringStringMap.get("total_fee");
            String time_end = stringStringMap.get("time_end");
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date parse = simpleDateFormat.parse(time_end.substring(0, 4) + "-" + time_end.substring(4, 6) + "-" + time_end.substring(6, 8) + " " + time_end.substring(8, 10) + ":" + time_end.substring(10, 12));
            String results = stringStringMap.get("results");

            wxResults.setPatientId(patientid);
            wxResults.setTransactionId(transaction_id);
            wxResults.setOpenid(openid);
            wxResults.setMchId(mch_id);
            wxResults.setOutTradeNo(out_trade_no);
            wxResults.setTotalFee(total_fee);
            wxResults.setTimeEnd(parse);
            wxResults.setResults(results);
            return wxResultsMapper.insertSelective(wxResults);
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 检查可用金额是否充足
     *
     * @param
     * @return
     */
    @Override
    public CommonResult amountSufficient(Long registrationId,List<BigDecimal> amount) {
        if (CollectionUtils.isEmpty(amount)) {
            return CommonResult.failed("操作失败");
        }

        if (StringUtils.isEmpty(registrationId)) {
            return CommonResult.failed("请传入挂号相关信息");
        }
        // 查询患者ID
        DmsRegistration dmsRegistration = dmsRegistrationMapper.selectByPrimaryKey(registrationId);
        if (!StringUtils.isEmpty(dmsRegistration.getPatientId())) {
            // 查询患者账户可用余额
            BmsAccount bmsAccount = bmsAccountMapper.selectByPrimaryKey(dmsRegistration.getPatientId());
            BigDecimal money = null;
            amount.forEach(result -> {
                money.add(result);
            });
            if (bmsAccount.getBlance().compareTo(money) > 0) {
                return  CommonResult.success("余额充足");
            }
        }
        return CommonResult.failed("余额不足,请充值");
    }


    @Override
    public int WxProgramResults(WxProgramResultsParam wxProgramResultsParam) {
        try {
            if(!StringUtils.isEmpty(wxProgramResultsParam)){
                WxResults wxResults =new WxResults();
                wxResults.setPatientId(wxProgramResultsParam.getPatientid());
                wxResults.setTotalFee(wxProgramResultsParam.getAmount().toString());
                //时间戳转换成时间
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd :HH:mm:ss");
                String sd = sdf.format(new Date(Long.parseLong(String.valueOf(wxProgramResultsParam.getTimeStamp()))));
                wxResults.setTimeEnd(sdf.parse(sd));
                wxResults.setResults("微信小程序支付成功");
                int i = wxResultsMapper.insertSelective(wxResults);
                if(i>0){
                    BmsAccountExample bmsAccountExample=new BmsAccountExample();
                    bmsAccountExample.createCriteria().andPatientIdEqualTo(wxProgramResultsParam.getPatientid());
                    List<BmsAccount> bmsAccounts = bmsAccountMapper.selectByExample(bmsAccountExample);
                    if(!CollectionUtils.isEmpty(bmsAccounts)){
                        BmsAccount bmsAccount = bmsAccounts.get(0);
                        if(wxProgramResultsParam.getAmount().compareTo(BigDecimal.valueOf(0))>=0){
                            bmsAccount.setBlance(bmsAccount.getBlance().add(wxProgramResultsParam.getAmount()));
                            return bmsAccountMapper.updateByPrimaryKey(bmsAccount);
                        }else {
                            return 0;
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;

    }

    @Override
    public WXDmsRegistrationParam WxProgramCompletion(WXDmsRegistrationParam wXDmsRegistrationParam) {
        PmsPatient pmsPatient = pmsPatientMapper.selectByPrimaryKey(Long.parseLong(wXDmsRegistrationParam.getName()));
        wXDmsRegistrationParam.setName(pmsPatient.getName());
        wXDmsRegistrationParam.setDateOfBirth(pmsPatient.getDateOfBirth());
        wXDmsRegistrationParam.setIdentificationNo(pmsPatient.getIdentificationNo());
        wXDmsRegistrationParam.setHomeAddress(pmsPatient.getHomeAddress());
        wXDmsRegistrationParam.setGender(pmsPatient.getGender());
        wXDmsRegistrationParam.setMedicalRecordNo(pmsPatient.getMedicalRecordNo());
        if(!StringUtils.isEmpty(pmsPatient.getPhoneNo())){
            wXDmsRegistrationParam.setPhoneNo(pmsPatient.getPhoneNo());
        }
        wXDmsRegistrationParam.setNeedBook(1);
        wXDmsRegistrationParam.setSettlementCatId(1L);
        /*wXDmsRegistrationParam.setAmount(wXDmsRegistrationParam.getAmount().add(BigDecimal.valueOf(1)));*/
        wXDmsRegistrationParam.setAmount(wXDmsRegistrationParam.getAmount());
        wXDmsRegistrationParam.setInvoiceNo( System.currentTimeMillis());
        wXDmsRegistrationParam.setOpratorId(-1L);
        /* int i = Integer.parseInt(wXDmsRegistrationParam.getTime().substring(0, 2));*/
        return wXDmsRegistrationParam;
    }

    @Override
    public int amountSufficient(Long patientId,BigDecimal acount) {
        if (!StringUtils.isEmpty(patientId)){
            // 查询患者账户可用余额
            BmsAccountExample bmsAccountExample=new BmsAccountExample();
            bmsAccountExample.createCriteria().andPatientIdEqualTo(patientId);
            List<BmsAccount> bmsAccounts = bmsAccountMapper.selectByExample(bmsAccountExample);
            if(!CollectionUtils.isEmpty(bmsAccounts)){
                if(bmsAccounts.get(0).getBlance().compareTo(acount)>=0){
                    return 1;
                }
            }
        }
        return 0;
    }

    @Override
    public int subBmsAccount(Long patientId,BigDecimal acount) {
        BmsAccountExample bmsAccountExample=new BmsAccountExample();
        bmsAccountExample.createCriteria().andPatientIdEqualTo(patientId);
        List<BmsAccount> bmsAccounts = bmsAccountMapper.selectByExample(bmsAccountExample);
        if(!CollectionUtils.isEmpty(bmsAccounts)){
            BmsAccount bmsAccount = bmsAccounts.get(0);
            if(bmsAccount.getBlance().compareTo(acount)>=0){
                bmsAccount.setBlance(bmsAccount.getBlance().subtract(acount));
                return bmsAccountMapper.updateByPrimaryKey(bmsAccount);
            }
        }
        return 0;
    }

    @Override
    public int WXXinsertBmsAccount(WXDmsRegistrationParam wxDmsRegistrationParam)  {

        //根据身份证号查看病人信息
        PmsPatientExample pmsPatientExample=new PmsPatientExample();
        pmsPatientExample.createCriteria().andIdentificationNoEqualTo(wxDmsRegistrationParam.getIdentificationNo());
        List<PmsPatient> pmsPatients = pmsPatientMapper.selectByExample(pmsPatientExample);

        if(!CollectionUtils.isEmpty(pmsPatients)){

            //查看病人是不是有账户
            BmsAccount bmsAccount1 = SelectAccountByCardId(wxDmsRegistrationParam);
            //如果没有新建账户
            if(StringUtils.isEmpty(bmsAccount1)){
                BmsAccount bmsAccount=new BmsAccount();
                bmsAccount.setPatientId(pmsPatients.get(0).getId());
                bmsAccount.setAccountCode(UUID.randomUUID().toString().trim().replaceAll("-", ""));
                bmsAccount.setBlance(BigDecimal.valueOf(0));
                bmsAccount.setFrozen(BigDecimal.valueOf(0));
                bmsAccount.setSummery(wxDmsRegistrationParam.getAmount());
                bmsAccount.setCardId(wxDmsRegistrationParam.getIdentificationNo());
                bmsAccount.setLevel(1);
                bmsAccount.setAccountStatus(1);
                bmsAccount.setCreateUser(wxDmsRegistrationParam.getOpratorId());
                bmsAccount.setCreateTime(new Date());
                bmsAccount.setUpdateUser(wxDmsRegistrationParam.getOpratorId());
                bmsAccount.setUpdateTime(new Date());
                bmsAccount.setIsDeleted(0);
                return bmsAccountMapper.insertSelective(bmsAccount);
            }else {
                return 1;
            }
        }
        return 0;
    }
}
