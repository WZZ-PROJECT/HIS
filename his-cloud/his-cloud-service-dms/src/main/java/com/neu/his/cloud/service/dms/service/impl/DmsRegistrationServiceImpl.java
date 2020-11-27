package com.neu.his.cloud.service.dms.service.impl;

import cn.hutool.core.date.DateTime;
import com.neu.his.cloud.service.dms.WxPay.MyWXPay;
import com.neu.his.cloud.service.dms.common.CommonResult;
import com.neu.his.cloud.service.dms.config.SendSMSUtils;
import com.neu.his.cloud.service.dms.dto.app.AppRegistrationParam;
import com.neu.his.cloud.service.dms.dto.dms.*;
import com.neu.his.cloud.service.dms.mapper.*;
import com.neu.his.cloud.service.dms.model.*;
import com.neu.his.cloud.service.dms.service.DmsRegistrationService;
import com.neu.his.cloud.service.dms.util.AgeStrUtil;
import com.neu.his.cloud.service.dms.util.DateUtil;
import com.neu.his.cloud.service.dms.util.InvoiceNo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class DmsRegistrationServiceImpl implements DmsRegistrationService {

    private final static Logger logger = LoggerFactory.getLogger(DmsRegistrationServiceImpl.class);
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

    @Autowired
    private BmsRefundBillMapper bmsRefundBillMapper;

    /**
     * 同一患者在同一天，未就诊情况下应不能重复挂同一医生
     *
     * @param wxDmsRegistrationParam
     * @return
     */
    @Override
    public int queryCreateRegistration(WXDmsRegistrationParam wxDmsRegistrationParam,long patientId) {
        DmsRegistrationExample example = new DmsRegistrationExample();
        example.createCriteria().andPatientIdEqualTo(patientId)
                .andAttendanceDateBetween(getStartOfDay(wxDmsRegistrationParam.getAttendanceDate()),getEndOfDay(wxDmsRegistrationParam.getAttendanceDate()));
//                .andDeptIdEqualTo(wxDmsRegistrationParam.getDeptId());
        List<DmsRegistration> dmsRegistrations = dmsRegistrationMapper.selectByExample(example);
        // 患者挂号时选择的排班信息
        SmsSkd addSkd = smsSkdMapper.selectByPrimaryKey(wxDmsRegistrationParam.getSkdId());
        if (!CollectionUtils.isEmpty(dmsRegistrations)) {
            AtomicReference<String> status = new AtomicReference<>("");
            AtomicReference<String> staffId = new AtomicReference<>("");
            dmsRegistrations.forEach(data ->{
                status.updateAndGet(v -> v + data.getStatus());
                SmsSkd smsSkd = smsSkdMapper.selectByPrimaryKey(data.getSkdId());
                if (!StringUtils.isEmpty(smsSkd)) {
                    staffId.updateAndGet(v -> v + smsSkd.getStaffId());
                }
            });
            //今天已存在挂号，请勿重新挂号
            String s = status.get();
            String staffIds = staffId.get();
            if ((s.contains("1") || s.contains("2")) && staffIds.contains(addSkd.getStaffId().toString())) {
                return 3;
            }
        }
        return 4;
    }
    // 获得某天最大时间 2017-10-15 23:59:59
    public static Date getEndOfDay(Date date) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());;
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
        return Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }

    // 获得某天最小时间 2017-10-15 00:00:00
    public static Date getStartOfDay(Date date) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        return Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }
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
            SmsStaff smsStaff = null;
            SmsDept smsDept = null;
            if (dmsRegistrationParam.getSkdId() != null) {
                int i1 = this.queryCreateRegistration(dmsRegistrationParam, patientId);
                if (i1 == 3) {
                    return i1;
                }
                SmsSkd skd = smsSkdMapper.selectByPrimaryKey(dmsRegistrationParam.getSkdId());
                smsStaff  = smsStaffMapper.selectByPrimaryKey(skd.getStaffId());
            } else {
                smsDept = smsDeptMapper.selectByPrimaryKey(dmsRegistrationParam.getDeptId());
            }
            //创建要插入的DmsRegistration对象
            DmsRegistration dmsRegistration = new DmsRegistration();
            dmsRegistration.setRegisteredlevel(1L);
            if(!StringUtils.isEmpty(dmsRegistrationParam.getRegisteredLevel())){
                dmsRegistration.setRegisteredlevel(dmsRegistrationParam.getRegisteredLevel());
            }
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
            bmsInvoiceRecord.setBillId(billId);
            bmsInvoiceRecord.setAmount(dmsRegistrationParam.getAmount());
            // bmsInvoiceRecord.setFreezeStatus(1);//冻结状态为1正常
            bmsInvoiceRecord.setOperatorId(dmsRegistrationParam.getOpratorId());
            bmsInvoiceRecord.setSettlementCatId(dmsRegistrationParam.getSettlementCatId());
            bmsInvoiceRecord.setInvoiceNo(InvoiceNo.getInvoiceNo());
            bmsInvoiceRecord.setItemList(registrationId + "," + 0 + "," + dmsRegistrationParam.getAmount() + "><");//挂号id,0,amount><
            if(dmsRegistrationParam.getSettlementCatId()==-1){
                bmsInvoiceRecord.setSettlementCatId(6L);
            }
            long invoiceId = bmsInvoiceRecordMapper.insertSelective(bmsInvoiceRecord);
            if(dmsRegistrationParam.getSettlementCatId()==6){
                //挂号微信付款码支付
                //没有账户是新建一个账户
                Map<String, String> stringStringMap = WxPay(dmsRegistrationParam);
                if(!CollectionUtils.isEmpty(stringStringMap) && stringStringMap.get("return_code").equals("SUCCESS") && stringStringMap.get("result_code").equals("SUCCESS")){
                    DmsRegistration id = new DmsRegistration();
                    id.setId(registrationId);
                    id.setWxResultsId(Long.parseLong(stringStringMap.get("wxResultsId")));
                    dmsRegistrationMapper.updateByPrimaryKeySelective(id);
                    WxResults wxResults = wxResultsMapper.selectByPrimaryKey(Long.parseLong(stringStringMap.get("wxResultsId")));
                    wxResults.setType(-1L);
                    wxResultsMapper.updateByPrimaryKey(wxResults);
                    if (dmsRegistrationParam.getSkdId() == null) {
                        SendSMSUtils.sendMessage(dmsRegistrationParam.getPhoneNo(),dmsRegistrationParam.getName(),DateUtil.date(dmsRegistrationParam.getAttendanceDate())+" "+dmsRegistrationParam.getTime(),smsDept.getName());
                    } else {
                        SendSMSUtils.sendMessage(dmsRegistrationParam.getPhoneNo(),dmsRegistrationParam.getName(),DateUtil.date(dmsRegistrationParam.getAttendanceDate())+" "+dmsRegistrationParam.getTime(),smsStaff.getName());
                    }
                    return insertBmsAccount(dmsRegistrationParam);
                }else {
                    String string  = null;
                    if(string.equals("")) {
                        int i = 0;
                    }
                }
            }else if(dmsRegistrationParam.getSettlementCatId()==7){
                //挂号账户支付
                BmsAccount bmsAccount = returnAccount(dmsRegistrationParam);
                if(!StringUtils.isEmpty(bmsAccount)){
                    int i = amountSufficient(bmsAccount.getPatientId(), dmsRegistrationParam.getAmount());
                    if(i>0){
                        bmsAccount.setBlance(bmsAccount.getBlance().subtract(dmsRegistrationParam.getAmount()));
                        WxResults account = new WxResults();
                        account.setTotalFee(dmsRegistrationParam.getAmount().toString());
                        account.setType(dmsRegistrationParam.getSettlementCatId());
                        account.setState((long)0);
                        // 支付完成时间
                        account.setTimeEnd(new Date());
                        // 病人id
                        account.setPatientId(patientId);
                        account.setResults("挂号账户支付");
                        wxResultsMapper.insertSelective(account);

                        DmsRegistration id = new DmsRegistration();
                        id.setId(registrationId);
                        id.setWxResultsId(account.getId());
                        dmsRegistrationMapper.updateByPrimaryKeySelective(id);
                        if (dmsRegistrationParam.getSkdId() == null) {
                            SendSMSUtils.sendMessage(dmsRegistrationParam.getPhoneNo(),dmsRegistrationParam.getName(),DateUtil.date(dmsRegistrationParam.getAttendanceDate())+" "+dmsRegistrationParam.getTime(),smsDept.getName());
                        } else {
                            SendSMSUtils.sendMessage(dmsRegistrationParam.getPhoneNo(),dmsRegistrationParam.getName(),DateUtil.date(dmsRegistrationParam.getAttendanceDate())+" "+dmsRegistrationParam.getTime(),smsStaff.getName());
                        }
                        return bmsAccountMapper.updateByPrimaryKey(bmsAccount);
                    }
                }
            } else if (dmsRegistrationParam.getSettlementCatId() == 1) {
                // 挂号现金支付
                WxResults wxResults = new WxResults();
                wxResults.setTotalFee(dmsRegistrationParam.getAmount().toString());
                wxResults.setType(dmsRegistrationParam.getSettlementCatId());
                wxResults.setState((long)0);
                // 支付完成时间
                wxResults.setTimeEnd(new Date());
                // 病人id
                wxResults.setPatientId(patientId);
                wxResults.setResults("挂号现金支付");
                wxResultsMapper.insertSelective(wxResults);
                DmsRegistration id = new DmsRegistration();
                id.setId(registrationId);
                id.setWxResultsId(wxResults.getId());
                dmsRegistrationMapper.updateByPrimaryKeySelective(id);
                if (dmsRegistrationParam.getSkdId() == null) {
                    SendSMSUtils.sendMessage(dmsRegistrationParam.getPhoneNo(),dmsRegistrationParam.getName(),DateUtil.date(dmsRegistrationParam.getAttendanceDate())+" "+dmsRegistrationParam.getTime(),smsDept.getName());
                } else {
                    SendSMSUtils.sendMessage(dmsRegistrationParam.getPhoneNo(),dmsRegistrationParam.getName(),DateUtil.date(dmsRegistrationParam.getAttendanceDate())+" "+dmsRegistrationParam.getTime(),smsStaff.getName());
                }
                return insertBmsAccount(dmsRegistrationParam);
            } else if(dmsRegistrationParam.getSettlementCatId() == -1) { //小程序
                DmsRegistration id = new DmsRegistration();
                id.setId(registrationId);
                id.setWxResultsId(dmsRegistrationParam.getWxResultId());
                dmsRegistrationMapper.updateByPrimaryKeySelective(id);
                if (dmsRegistrationParam.getSkdId() == null) {
                    SendSMSUtils.sendMessage(dmsRegistrationParam.getPhoneNo(),dmsRegistrationParam.getName(),DateUtil.date(dmsRegistrationParam.getAttendanceDate())+" "+dmsRegistrationParam.getTime(),smsDept.getName());
                } else {
                    SendSMSUtils.sendMessage(dmsRegistrationParam.getPhoneNo(),dmsRegistrationParam.getName(),DateUtil.date(dmsRegistrationParam.getAttendanceDate())+" "+dmsRegistrationParam.getTime(),smsStaff.getName());
                }
                //创建病人账户信息
                return WXXinsertBmsAccount(dmsRegistrationParam);
            } else {
                if (dmsRegistrationParam.getSkdId() == null) {
                    SendSMSUtils.sendMessage(dmsRegistrationParam.getPhoneNo(),dmsRegistrationParam.getName(),DateUtil.date(dmsRegistrationParam.getAttendanceDate())+" "+dmsRegistrationParam.getTime(),smsDept.getName());
                } else {
                    SendSMSUtils.sendMessage(dmsRegistrationParam.getPhoneNo(),dmsRegistrationParam.getName(),DateUtil.date(dmsRegistrationParam.getAttendanceDate())+" "+dmsRegistrationParam.getTime(),smsStaff.getName());
                }
                //创建病人账户信息
                return WXXinsertBmsAccount(dmsRegistrationParam);
            }
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
        dmsRegistrationExample.createCriteria().andSkdIdEqualTo(skdId).andStatusBetween(1,2);
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
                    String next=ruletime+" "+iterator.next()+":00";
                    if(format.equals(next)){
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

    //挂号微信支付
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
                        int i = WxPayResult(stringStringMap, patientid, 6L);
                        if (i>0) {
                            stringStringMap.put("wxResultsId",String.valueOf(i));
                            return stringStringMap;
                        }
                    }
                    return stringStringMap;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }//付款码支付结束
        return null;
    }

    //查看该病人是不是有账号
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

    //该病人没有账号则新建一个账号,有账号直接使用以前的账号
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

    //充值
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

                        //封装充值信息
                        if(wxDmsRegistrationParam.getSettlementCatId()!=6){
                            Map<String, String> stringStringMap=new HashMap<>();
                            stringStringMap.put("total_fee",wxDmsRegistrationParam.getTopUp().toString());
                            stringStringMap.put("time_end",new DateTime().toString().replaceAll("-","").replaceAll(":","").replaceAll(" ",""));
                            WxPayResult(stringStringMap,bmsAccount.getPatientId(),wxDmsRegistrationParam.getSettlementCatId());
                        }
                    }else {
                        //修改充值状态失败
                        bmsRecharge.setRechargeStates(2);
                    }
                    //修改充值状态
                    int i = bmsRechargeMapper.updateByPrimaryKey(bmsRecharge);
                    //插入发票信息
                    if(i>0){
                        return insertBmsInvoiceRecord(wxDmsRegistrationParam);
                    }
                }
            }
        }
        return 0;
    }

    //退款
    @Transactional
    @Override
    public int rollback(WXDmsRegistrationParam wxDmsRegistrationParam) {

        //获得各个方式退款金额
        RefundResultsParam refundResultsParam = selectRefundResultsParam(wxDmsRegistrationParam);
        //退费总金额
        BigDecimal sum=new BigDecimal(0);
        if(refundResultsParam!=null){
            //判断数据的合理性
            if(refundResultsParam.getCash().compareTo(BigDecimal.valueOf(0))>=0 &&
                    refundResultsParam.getBankCard().compareTo(BigDecimal.valueOf(0L))>=0 &&
                    refundResultsParam.getWeChat().compareTo(BigDecimal.valueOf(0L))>=0){
                //求退费总金额
                sum= refundResultsParam.getBankCard().add(refundResultsParam.getWeChat()).add(refundResultsParam.getCash());
                //获得病人的信息
                PmsPatientExample pmsPatientExample=new PmsPatientExample();
                pmsPatientExample.createCriteria().andIdentificationNoEqualTo(wxDmsRegistrationParam.getIdentificationNo());
                List<PmsPatient> pmsPatients = pmsPatientMapper.selectByExample(pmsPatientExample);
                if(!CollectionUtils.isEmpty(pmsPatients)){
                    //病人信息
                    PmsPatient pmsPatient = pmsPatients.get(0);

                    //修改账户的信息：1.获得用户信息；2.插入总的记录和子记录
                    //获得病人账户信息
                    BmsAccount bmsAccount = SelectAccountByCardId(wxDmsRegistrationParam);
                    if(bmsAccount!=null){
                        //插入总记录一条
                        BmsRefundBill bmsRefundBill=new BmsRefundBill();
                        bmsRefundBill.setPatientId(pmsPatient.getId());
                        bmsRefundBill.setRefundTime(new DateTime());
                        bmsRefundBill.setAmount(refundResultsParam.getBankCard().add(refundResultsParam.getCash()).add(refundResultsParam.getWeChat()));
                        int i = bmsRefundBillMapper.insertSelective(bmsRefundBill);
                        //插入子记录的模板
                        BmsRollback bmsRollback=new BmsRollback();
                        //判断Id的合理性
                        if(!StringUtils.isEmpty(i)){
                            //插入微信、银行卡、现金3类的子记录
                            bmsRollback.setRefundBillId(Long.parseLong(i+""));
                            bmsRollback.setAccountCode(bmsAccount.getAccountCode());
                            bmsRollback.setRbState(4);
                            bmsRollback.setAppTime(new Date());
                            bmsRollback.setCreateUser(wxDmsRegistrationParam.getOpratorId());
                            bmsRollback.setCreateTime(new Date());
                            bmsRollback.setUpdateUser(wxDmsRegistrationParam.getOpratorId());
                            bmsRollback.setUpdateTime(new Date());
                            bmsRollback.setIsDeleted(0);
                            //插入银行卡退款记录
                            BmsRollback bmsRollback2=new BmsRollback();
                            BeanUtils.copyProperties(bmsRollback,bmsRollback2);
                            bmsRollback2.setRbAmount(refundResultsParam.getBankCard());
                            bmsRollback2.setRbTime(new Date());
                            bmsRollback2.setRbType(2);
                            bmsRollbackMapper.insert(bmsRollback2);
                            refundResultsParam.setBankCard(BigDecimal.valueOf(0));
                            //插入现金退款记录
                            BmsRollback bmsRollback3=new BmsRollback();
                            BeanUtils.copyProperties(bmsRollback,bmsRollback3);
                            bmsRollback3.setRbAmount(refundResultsParam.getCash());
                            bmsRollback3.setRbTime(new Date());
                            bmsRollback3.setRbType(1);
                            bmsRollbackMapper.insert(bmsRollback3);
                            refundResultsParam.setCash(BigDecimal.valueOf(0));

                            //获得病人充值信息=》用于微信退款（一个账单的金额可能不够）
                            WxResultsExample wxResultsExample=new WxResultsExample();
                            wxResultsExample.createCriteria().andPatientIdEqualTo(pmsPatient.getId()).andTypeEqualTo(6L).andStateEqualTo(0L);
                            List<WxResults> wxResults = wxResultsMapper.selectByExample(wxResultsExample);

                            wxResults.forEach(wxResults1 -> {
                                Map<String, String> refund=null;
                                BigDecimal count=new BigDecimal(0);
                                //退款逻辑
                                //多个退款分别退款进行存储记录；
                                // 修改该充值记录状态已退款
                                try {
                                    if(refundResultsParam.getWeChat().compareTo(BigDecimal.valueOf(Double.parseDouble(wxResults1.getTotalFee())))>=0) {

                                        String totalFee=(BigDecimal.valueOf(Double.parseDouble(wxResults1.getTotalFee())).multiply(BigDecimal.valueOf(100)).intValue())+"";
                                        refund = MyWXPay.refund(wxResults1.getTransactionId(), totalFee, totalFee);
                                        if(!CollectionUtils.isEmpty(refund)){
                                            refundResultsParam.setWeChat(refundResultsParam.getWeChat().subtract(BigDecimal.valueOf(Double.parseDouble(wxResults1.getTotalFee()))));
                                            count=BigDecimal.valueOf(Double.parseDouble(wxResults1.getTotalFee()));
                                        }
                                    }else {
                                        String totalFee=(BigDecimal.valueOf(Double.parseDouble(wxResults1.getTotalFee())).multiply(BigDecimal.valueOf(100)).intValue())+"";
                                        String refund_fee=(refundResultsParam.getWeChat().multiply(BigDecimal.valueOf(100)).intValue())+"";
                                        refund = MyWXPay.refund(wxResults1.getTransactionId(), totalFee, refund_fee);
                                        if(!CollectionUtils.isEmpty(refund)){
                                            refundResultsParam.setWeChat(refundResultsParam.getWeChat().subtract(refundResultsParam.getWeChat()));
                                            count=refundResultsParam.getWeChat();
                                        }
                                    }
                                    if(!CollectionUtils.isEmpty(refund)){
                                        //插入退款记录
                                        BmsRollback bmsRollback1=new BmsRollback();
                                        BeanUtils.copyProperties(bmsRollback,bmsRollback1);
                                        bmsRollback1.setRbAmount(count);
                                        bmsRollback1.setRbTime(new Date());
                                        bmsRollback1.setRbType(6);
                                        bmsRollback1.setOutTradeId(refund.get("out_refund_no_0"));
                                        bmsRollback1.setRosult(refund.get("results"));
                                        bmsRollbackMapper.insert(bmsRollback1);
                                        //修改这个订单为已退费状态
                                        wxResults1.setState(1L);
                                        wxResultsMapper.updateByPrimaryKey(wxResults1);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });
                            //所有退费操作完成将该用户的订单设置为已退费
                            if(refundResultsParam.getWeChat().add(refundResultsParam.getCash()).add(refundResultsParam.getBankCard()).compareTo(BigDecimal.valueOf(0L))==0){
                                WxResultsExample wxResultsExample1=new WxResultsExample();
                                wxResultsExample1.createCriteria().andPatientIdEqualTo(pmsPatient.getId()).andStateEqualTo(0L);
                                List<WxResults> wxResultsList = wxResultsMapper.selectByExample(wxResultsExample1);

                                wxResultsList.forEach(wxResults1 -> {
                                    wxResults1.setState(1L);
                                    wxResultsMapper.updateByPrimaryKey(wxResults1);
                                });
                            }
                            //插入账单信息
                            int i1 = insertBmsInvoiceRecord(wxDmsRegistrationParam);
                            //修改用户账户金额
                            if(i1>0){
                                bmsAccount.setSummery(bmsAccount.getSummery().subtract(sum));
                                bmsAccount.setBlance(BigDecimal.valueOf(0L));
                                bmsAccount.setUpdateUser(wxDmsRegistrationParam.getOpratorId());
                                bmsAccount.setUpdateTime(new Date());
                                int i2 = bmsAccountMapper.updateByPrimaryKey(bmsAccount);
                                return i2;
                            }
                        }

                    }
                }
            }
        }
        return 0;
    }


    //封装微信支付的结果
    @Override
    public int WxPayResult(Map<String, String> stringStringMap,Long patientid,Long type) {
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
            if(type==6){
                wxResults.setTotalFee((Double.parseDouble(total_fee)/100)+"");
            }else if(type==-2){
                wxResults.setTotalFee((Double.parseDouble(total_fee)/100)+"");
                type=-1L;
            }
            else {
                wxResults.setTotalFee(total_fee);
            }
            wxResults.setTimeEnd(parse);
            wxResults.setResults(results);
            wxResults.setType(type);
            wxResults.setState(0L);
            int i = wxResultsMapper.insertSelective(wxResults);
            if (i > 0) {
                return Integer.parseInt(wxResults.getId().toString());
            }
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

    //微信小程序结果封装
    @Override
    public int WxProgramResults(WxProgramResultsParam wxProgramResultsParam) {
        try {
            if(!StringUtils.isEmpty(wxProgramResultsParam) && !CollectionUtils.isEmpty(wxProgramResultsParam.getResults())){
                BigDecimal amount =new BigDecimal(0);
                amount=BigDecimal.valueOf(Double.parseDouble(wxProgramResultsParam.getResults().get("total_fee"))/100);
                WxResults wxResults =new WxResults();
                wxResults.setPatientId(wxProgramResultsParam.getPatientid());
                wxResults.setTransactionId(wxProgramResultsParam.getResults().get("transaction_id"));
                wxResults.setOpenid(wxProgramResultsParam.getOpenid());
                wxResults.setMchId(wxProgramResultsParam.getResults().get("mch_id"));
                wxResults.setOutTradeNo(wxProgramResultsParam.getResults().get("out_trade_no"));
                wxResults.setTotalFee((Double.parseDouble(wxProgramResultsParam.getResults().get("total_fee"))/100)+"");
                wxResults.setResults(wxProgramResultsParam.getResults().get("results"));
                wxResults.setTimeEnd(new DateTime());
                wxResults.setType(6L);
                wxResults.setState(0L);
                int i = wxResultsMapper.insert(wxResults);
                if(i>0){
                    BmsAccountExample bmsAccountExample=new BmsAccountExample();
                    bmsAccountExample.createCriteria().andPatientIdEqualTo(wxProgramResultsParam.getPatientid());
                    List<BmsAccount> bmsAccounts = bmsAccountMapper.selectByExample(bmsAccountExample);
                    if(!CollectionUtils.isEmpty(bmsAccounts)){
                        BmsAccount bmsAccount = bmsAccounts.get(0);
                        if(amount.compareTo(BigDecimal.valueOf(0))>=0){
                            bmsAccount.setBlance(bmsAccount.getBlance().add(amount));
                            bmsAccount.setSummery(bmsAccount.getSummery().add(amount));
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

    //补全用户信息
    @Override
    public WXDmsRegistrationParam WxProgramCompletion(WxRegisteredPatam wxRegisteredPatam) {
        WXDmsRegistrationParam wXDmsRegistrationParam=new WXDmsRegistrationParam();
        wXDmsRegistrationParam.setSkdId(wxRegisteredPatam.getSkdId());
        wXDmsRegistrationParam.setDeptId(wxRegisteredPatam.getDeptId());
        wXDmsRegistrationParam.setAttendanceDate(wxRegisteredPatam.getAttendanceDate());
        wXDmsRegistrationParam.setAmount(wxRegisteredPatam.getAmount());
        wXDmsRegistrationParam.setTime(wxRegisteredPatam.getTime());
        PmsPatient pmsPatient = pmsPatientMapper.selectByPrimaryKey(wxRegisteredPatam.getPatientId());
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
        wXDmsRegistrationParam.setSettlementCatId(-1L);
        /*wXDmsRegistrationParam.setAmount(wXDmsRegistrationParam.getAmount().add(BigDecimal.valueOf(1)));*/
        wXDmsRegistrationParam.setAmount(wXDmsRegistrationParam.getAmount());
        wXDmsRegistrationParam.setInvoiceNo( System.currentTimeMillis());
        wXDmsRegistrationParam.setOpratorId(-1L);
        /* int i = Integer.parseInt(wXDmsRegistrationParam.getTime().substring(0, 2));*/
        return wXDmsRegistrationParam;
    }

    //余额是否足够
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

    //扣费
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

    //微信小程序生成病人账号
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

    //判断是不用有账户
    @Override
    public int isAccount(String identificationNo) {
        PmsPatientExample pmsPatientExample=new PmsPatientExample();
        pmsPatientExample.createCriteria().andIdentificationNoEqualTo(identificationNo);
        List<PmsPatient> pmsPatients = pmsPatientMapper.selectByExample(pmsPatientExample);
        if(!CollectionUtils.isEmpty(pmsPatients)){
            Long id = pmsPatients.get(0).getId();
            BmsAccountExample bmsAccountExample=new BmsAccountExample();
            bmsAccountExample.createCriteria().andPatientIdEqualTo(id);
            List<BmsAccount> bmsAccounts = bmsAccountMapper.selectByExample(bmsAccountExample);
            if(!CollectionUtils.isEmpty(bmsAccounts)){
                return 1;
            }else {
                return 0;
            }
        }
        return 0;
    }

    //获得病人账户信息
    @Override
    public BmsAccount returnAccount(WXDmsRegistrationParam dmsRegistrationParam){
        int i = WXXinsertBmsAccount(dmsRegistrationParam);
        if(i>0){
            PmsPatientExample pmsPatientExample=new PmsPatientExample();
            pmsPatientExample.createCriteria().andIdentificationNoEqualTo(dmsRegistrationParam.getIdentificationNo());
            List<PmsPatient> pmsPatients = pmsPatientMapper.selectByExample(pmsPatientExample);
            if(!CollectionUtils.isEmpty(pmsPatients)){
                Long id = pmsPatients.get(0).getId();
                BmsAccountExample bmsAccountExample=new BmsAccountExample();
                bmsAccountExample.createCriteria().andPatientIdEqualTo(id);
                List<BmsAccount> bmsAccounts = bmsAccountMapper.selectByExample(bmsAccountExample);
                if(!CollectionUtils.isEmpty(bmsAccounts)){
                    BmsAccount bmsAccount = bmsAccounts.get(0);
                    return bmsAccount;
                }
            }
        }
        return null;
    }

    //修改病人信息
    @Override
    public int updateInformation(WXDmsRegistrationParam wxDmsRegistrationParam) {
        PmsPatientExample pmsPatientExample=new PmsPatientExample();
        pmsPatientExample.createCriteria().andIdentificationNoEqualTo(wxDmsRegistrationParam.getIdentificationNo());
        List<PmsPatient> pmsPatients = pmsPatientMapper.selectByExample(pmsPatientExample);
        if(!CollectionUtils.isEmpty(pmsPatients)){
            PmsPatient pmsPatient = pmsPatients.get(0);
            pmsPatient.setName(wxDmsRegistrationParam.getName());
            pmsPatient.setGender(wxDmsRegistrationParam.getGender());
            pmsPatient.setHomeAddress(wxDmsRegistrationParam.getHomeAddress());
            pmsPatient.setPhoneNo(wxDmsRegistrationParam.getPhoneNo());
            return pmsPatientMapper.updateByPrimaryKeySelective(pmsPatient);
        }
        return 0;
    }

    @Override
    public int insertBmsInvoiceRecord(WXDmsRegistrationParam wxDmsRegistrationParam) {
        //生成随机数
        long  rs =(long) ((Math.random() * 9 + 1) * Math.pow(10, 19 - 1));
        //封装发票信息
        BmsInvoiceRecord bmsInvoiceRecord=new BmsInvoiceRecord();
        bmsInvoiceRecord.setCreateTime(new DateTime());
        bmsInvoiceRecord.setInvoiceNo(rs);
        bmsInvoiceRecord.setBillId(-1L);
        bmsInvoiceRecord.setOperatorId(wxDmsRegistrationParam.getOpratorId());
        //判断是收费还是退费
        if(wxDmsRegistrationParam.getTopUp()!=null && wxDmsRegistrationParam.getTopUp().compareTo(BigDecimal.valueOf(0L))>0){
            //收费 settlement_cat_id 10
            bmsInvoiceRecord.setAmount(wxDmsRegistrationParam.getTopUp());
            bmsInvoiceRecord.setSettlementCatId(wxDmsRegistrationParam.getSettlementCatId());
            String itemList =  wxDmsRegistrationParam.getOpratorId() + "," + 10 + "," + wxDmsRegistrationParam.getTopUp() + "><";//项目列表串:插入发票的时候要用
            bmsInvoiceRecord.setItemList(itemList);
        }

        if(wxDmsRegistrationParam.getRefund()!=null && wxDmsRegistrationParam.getRefund().compareTo(BigDecimal.valueOf(0L))>0){
            //退费 settlement_cat_id 为9
            RefundResultsParam refundResultsParam = selectRefundResultsParam(wxDmsRegistrationParam);
            if(refundResultsParam!=null){
                /*bmsInvoiceRecord.setAmount(wxDmsRegistrationParam.getRefund());*/
                bmsInvoiceRecord.setAmount(refundResultsParam.getCash().add(refundResultsParam.getWeChat()).add(refundResultsParam.getBankCard()));
                bmsInvoiceRecord.setSettlementCatId(1L);
                String itemList =  wxDmsRegistrationParam.getOpratorId() + "," + 9 + "," + wxDmsRegistrationParam.getRefund() + "><";//项目列表串:插入发票的时候要用
                bmsInvoiceRecord.setItemList(itemList);
            }
        }

        bmsInvoiceRecord.setType(1);

        return bmsInvoiceRecordMapper.insertSelective(bmsInvoiceRecord);
    }

    @Override
    public RefundResultsParam selectRefundResultsParam(WXDmsRegistrationParam wxDmsRegistrationParam) {
        //返回值
        RefundResultsParam refundResultsParam=new RefundResultsParam();
        refundResultsParam.setCash(BigDecimal.valueOf(0L));
        refundResultsParam.setWeChat(BigDecimal.valueOf(0L));
        refundResultsParam.setBankCard(BigDecimal.valueOf(0L));

        //查询病人账户余额
        PmsPatientExample pmsPatientExample=new PmsPatientExample();
        pmsPatientExample.createCriteria().andIdentificationNoEqualTo(wxDmsRegistrationParam.getIdentificationNo());
        List<PmsPatient> pmsPatients = pmsPatientMapper.selectByExample(pmsPatientExample);
        if(!CollectionUtils.isEmpty(pmsPatients)){
            //获得病人信息
            PmsPatient pmsPatient = pmsPatients.get(0);
            //获得病人账户金额
            BmsAccountExample bmsAccountExample=new BmsAccountExample();
            bmsAccountExample.createCriteria().andPatientIdEqualTo(pmsPatient.getId());
            List<BmsAccount> bmsAccounts = bmsAccountMapper.selectByExample(bmsAccountExample);
            if(!CollectionUtils.isEmpty(bmsAccounts)){
                //账户信息
                BmsAccount bmsAccount = bmsAccounts.get(0);
                //根据充值记录计算各个方式充值金额
                WxResultsExample wxResultsExample=new WxResultsExample();
                wxResultsExample.createCriteria().andPatientIdEqualTo(pmsPatient.getId()).andStateEqualTo(0L);
                List<WxResults> wxResults = wxResultsMapper.selectByExample(wxResultsExample);
                if(!CollectionUtils.isEmpty(wxResults)){
                    wxResults.forEach(wxResults1 -> {
                        if(wxResults1.getType()==1){
                            refundResultsParam.setCash(refundResultsParam.getCash().add(BigDecimal.valueOf(Double.parseDouble(wxResults1.getTotalFee()))));
                        }else if(wxResults1.getType()==2){
                            refundResultsParam.setBankCard(refundResultsParam.getBankCard().add(BigDecimal.valueOf(Double.parseDouble(wxResults1.getTotalFee()))));
                        }else if(wxResults1.getType()==6){
                            refundResultsParam.setWeChat(refundResultsParam.getWeChat().add(BigDecimal.valueOf(Double.parseDouble(wxResults1.getTotalFee()))));
                        }
                    });
                }
                //计算各个方式退款金额；退款金额不可大于订单金额
                //账户金额≤银行卡充值金额;直接银行卡退款，其余方式设置为0
                if(bmsAccount.getBlance().compareTo(refundResultsParam.getBankCard())<=0){

                    //现金设置为0
                    refundResultsParam.setCash(BigDecimal.valueOf(0L));
                    //银行卡设置为账户金额
                    refundResultsParam.setBankCard(bmsAccount.getBlance());
                    //微信设置为0
                    refundResultsParam.setWeChat(BigDecimal.valueOf(0L));
                }else{

                    //账户金额大于银行卡充值金额；银行卡金额为充值金额；账户金额减去银行卡充值金额
                    bmsAccount.setBlance(bmsAccount.getBlance().subtract(refundResultsParam.getBankCard()));

                    //账户金额≤微信充值金额
                    if(bmsAccount.getBlance().compareTo(refundResultsParam.getWeChat())<=0){
                        refundResultsParam.setWeChat(bmsAccount.getBlance());
                        refundResultsParam.setCash(BigDecimal.valueOf(0L));
                    }else{

                        //账户金额大于微信充值金额；微信金额为充值金额；账户金额减去微信充值金额
                        bmsAccount.setBlance(bmsAccount.getBlance().subtract(refundResultsParam.getWeChat()));
                        //账户金额≤现金充值金额
                        if(bmsAccount.getBlance().compareTo(refundResultsParam.getCash())<=0){
                            refundResultsParam.setCash(bmsAccount.getBlance());
                        }
                    }
                }
                return refundResultsParam;
            }

        }
        return null;
    }
}
