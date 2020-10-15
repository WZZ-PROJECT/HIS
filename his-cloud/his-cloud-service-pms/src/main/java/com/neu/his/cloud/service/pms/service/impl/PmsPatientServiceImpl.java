package com.neu.his.cloud.service.pms.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import com.neu.his.cloud.service.pms.dto.dms.DmsCaseHistoryResult;
import com.neu.his.cloud.service.pms.dto.pms.PmsDiagnosisPatientListResult;
import com.neu.his.cloud.service.pms.dto.pms.PmsDiagnosisPatientResult;
import com.neu.his.cloud.service.pms.dto.pms.PmsPatientParam;
import com.neu.his.cloud.service.pms.dto.pms.PmsPatientResult;
import com.neu.his.cloud.service.pms.dto.sms.SmsStaffResult;
import com.neu.his.cloud.service.pms.dto.sms.SmsStopFollowDoctorParam;
import com.neu.his.cloud.service.pms.mapper.*;
import com.neu.his.cloud.service.pms.model.*;
import com.neu.his.cloud.service.pms.service.PmsPatientService;
import com.neu.his.cloud.service.pms.util.AgeStrUtil;
import com.neu.his.cloud.service.pms.util.DateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class PmsPatientServiceImpl implements PmsPatientService {
    @Resource
    private PmsPatientMapper pmsPatientMapper;
    @Resource
    private SmsStaffMapper smsStaffMapper;
    @Resource
    private SmsDeptMapper smsDeptMapper;
    @Resource
    private DmsRegistrationMapper dmsRegistrationMapper;
    @Resource
    private SmsSkdMapper smsSkdMapper;
    @Resource
    private DmsCaseHistoryMapper dmsCaseHistoryMapper;
    @Resource
    private SmsPatientFollowMapper smsPatientFollowMapper;

    @Resource
    private BmsAccountMapper bmsAccountMapper;
    //1.调用PmsPatientDao根据身份证号查询病人信息，并封装
    //2. 如果病人信息不存在，则直接返回


    @Override
    public PmsPatientResult selectPatientByIdNo(String identificationNo) {
        int count;//用于记录是否存在该病人
        PmsPatientExample example = new PmsPatientExample();
        example.createCriteria().andIdentificationNoEqualTo(identificationNo);
        List<PmsPatient> pmsPatientList = pmsPatientMapper.selectByExample(example);
        if(pmsPatientList.size()>0){//如果存在该病人
            PmsPatientResult pmsPatientResult = new PmsPatientResult();
            pmsPatientResult.setName(pmsPatientList.get(0).getName());

            //处理年龄
            Date dateOfBirth = pmsPatientList.get(0).getDateOfBirth();
            String age = AgeStrUtil.getAgeStr(dateOfBirth);
            pmsPatientResult.setAge(age);

            pmsPatientResult.setDateOfBirth(pmsPatientList.get(0).getDateOfBirth());
            pmsPatientResult.setGender(pmsPatientList.get(0).getGender());
            pmsPatientResult.setHomeAddress(pmsPatientList.get(0).getHomeAddress());
            pmsPatientResult.setPhoneNo(pmsPatientList.get(0).getPhoneNo());
            pmsPatientResult.setMedicalRecordNo(pmsPatientList.get(0).getMedicalRecordNo());

            //封装可用、总、冻结金额
            BmsAccountExample bmsAccountExample=new BmsAccountExample();
            bmsAccountExample.createCriteria().andPatientIdEqualTo(pmsPatientList.get(0).getId());
            List<BmsAccount> bmsAccounts = bmsAccountMapper.selectByExample(bmsAccountExample);
            if(!CollectionUtils.isEmpty(bmsAccounts)){
                pmsPatientResult.setSummery(bmsAccounts.get(0).getSummery());
                pmsPatientResult.setBlance(bmsAccounts.get(0).getBlance());
                pmsPatientResult.setFrozen(bmsAccounts.get(0).getFrozen());
            }else {
                pmsPatientResult.setSummery(BigDecimal.valueOf(0));
                pmsPatientResult.setBlance(BigDecimal.valueOf(0));
                pmsPatientResult.setFrozen(BigDecimal.valueOf(0));
            }
            return pmsPatientResult;
        }
        return null;
    }
    //1.传入医生id
    // 2.先根据医生id查找部门id,再从dms_registration中根据bind_status（0）、attendance_date、dept_id、status(1)查找出科室待诊患者
    // 3.
    //	3.1先通过午别、时间、医生id在表sms_skd中查找出sms_skd的id
    //	3.1.从dms_registration中根据bind_status（1）、attendance_date、skd_id查找出分配了的患者信息，并根据status分类（1，2，3）
    /**
     * 描述：
     * <p>author:王思阳
     * <p>author:赵煜  修改mysql5.7中比较时间错误
     *                 修改空指针异常
     *                 修改通过无别筛选列表错误
     */
    @Override
    public PmsDiagnosisPatientListResult refreshPatient(Long staffId) {
        //根据医生id查找部门id
        SmsStaffExample smsStaffExample = new SmsStaffExample();
        smsStaffExample.createCriteria().andIdEqualTo(staffId);
        List<SmsStaff> smsStaffList = smsStaffMapper.selectByExample(smsStaffExample);
        Long deptId = smsStaffList.get(0).getDeptId();
        System.err.println("deptId:"+deptId);


        //从dms_registration中根据bind_status（0）、attendance_date、dept_id、status(1)查找出科室待诊患者
        DmsRegistrationExample dmsRegistrationExample = new DmsRegistrationExample();
        Date date = new Date();//注意：这里的时间判断可能有问题

        //时间  对于科室等待患者，列出今天和昨天的（防止12点出故障）

        Date beforeAday= DateUtil.getDateBefore(date,1);
        System.err.println("date:"+date);
        System.err.println("beforeAday:"+beforeAday);

        dmsRegistrationExample.createCriteria().andBindStatusEqualTo(0).andDeptIdEqualTo(deptId).andStatusEqualTo(1).andAttendanceDateBetween(getStartOfDay(beforeAday),getEndOfDay(date));
        List<DmsRegistration> dmsRegistrationList = dmsRegistrationMapper.selectByExample(dmsRegistrationExample);

        System.err.println("dmsRegistrationList.size: " + dmsRegistrationList.size());

        //映射List<DmsRegistration>到List<PmsDiagnosisPatientResult>
        List<PmsDiagnosisPatientResult> deptWaitList = new ArrayList<>();//科室待诊列表
        for (DmsRegistration dmsRegistration : dmsRegistrationList) {
            PmsDiagnosisPatientResult pmsDiagnosisPatientResult = new PmsDiagnosisPatientResult();
            pmsDiagnosisPatientResult.setPatientId(dmsRegistration.getPatientId());
            pmsDiagnosisPatientResult.setPatientAge(dmsRegistration.getPatientAgeStr());
            pmsDiagnosisPatientResult.setRegistrationId(dmsRegistration.getId());
            pmsDiagnosisPatientResult.setRegistrationStatus(dmsRegistration.getStatus());
            //从PmsPatient中查询其他字段并封装
            PmsPatientExample pmsPatientExample = new PmsPatientExample();
            pmsPatientExample.createCriteria().andIdEqualTo(dmsRegistration.getPatientId());
            List<PmsPatient> pmsPatientList = pmsPatientMapper.selectByExample(pmsPatientExample);
            pmsDiagnosisPatientResult.setPatientName(pmsPatientList.get(0).getName());
            pmsDiagnosisPatientResult.setPatientHomeAdress(pmsPatientList.get(0).getHomeAddress());
            pmsDiagnosisPatientResult.setPatientGender(pmsPatientList.get(0).getGender());
            pmsDiagnosisPatientResult.setPatientMedicalRecordNo(pmsPatientList.get(0).getMedicalRecordNo());
            deptWaitList.add(pmsDiagnosisPatientResult);//加入List<PmsDiagnosisPatientResult>
        }
        System.err.println("deptWaitList.size: " + deptWaitList.size());

        //获取当前午别
        SimpleDateFormat df = new SimpleDateFormat("HH");
        String hourStr = df.format(date);

        //通过午别、时间、医生id在表sms_skd中查找出sms_skd的id
        SmsSkdExample smsSkdExample = new SmsSkdExample();
        System.err.println("staffId:"+staffId);
        smsSkdExample.createCriteria().andStaffIdEqualTo(staffId).andDateEqualTo(DateUtil.getDate(DateUtil.setMilliSecond(date,0)));
        List<SmsSkd> smsSkdList = smsSkdMapper.selectByExample(smsSkdExample);

        System.err.println(smsSkdList==null?"smsSkdList=null":"smsSkdList!=null");

        System.err.println(smsSkdList.size());
        if(CollectionUtil.isEmpty(smsSkdList)){
            return null;
        }

        List<Long> idList = new ArrayList<>();
        for(SmsSkd skd : smsSkdList){
            idList.add(skd.getId());
        }



        //从dms_registration中根据bind_status（1）、attendance_date、skd_id查找出分配了的患者信息，并根据status分类（1，2，3）
        DmsRegistrationExample dmsRegistrationExample1 = new DmsRegistrationExample();
        //使用DateUtil只截取日期部分
        dmsRegistrationExample1.createCriteria().andBindStatusEqualTo(1).andAttendanceDateBetween(getStartOfDay(new Date()),getEndOfDay(new Date())).andSkdIdIn(idList);


        List<DmsRegistration> dmsRegistrationList1 = dmsRegistrationMapper.selectByExample(dmsRegistrationExample1);

        List<PmsDiagnosisPatientResult> personalWaitList = new ArrayList<>();//个人待诊
        List<PmsDiagnosisPatientResult> personalDuringList = new ArrayList<>();//个人诊中
        List<PmsDiagnosisPatientResult> personalEndList = new ArrayList<>();//个人已诊
        for (DmsRegistration dmsRegistration:dmsRegistrationList1) {
            PmsDiagnosisPatientResult pmsDiagnosisPatientResult = new PmsDiagnosisPatientResult();
            pmsDiagnosisPatientResult.setPatientId(dmsRegistration.getPatientId());
            pmsDiagnosisPatientResult.setPatientAge(dmsRegistration.getPatientAgeStr());
            pmsDiagnosisPatientResult.setRegistrationId(dmsRegistration.getId());
            pmsDiagnosisPatientResult.setRegistrationStatus(dmsRegistration.getStatus());
            //从PmsPatient中查询其他字段并封装
            PmsPatientExample pmsPatientExample = new PmsPatientExample();
            pmsPatientExample.createCriteria().andIdEqualTo(dmsRegistration.getPatientId());
            List<PmsPatient> pmsPatientList = pmsPatientMapper.selectByExample(pmsPatientExample);
            pmsDiagnosisPatientResult.setPatientName(pmsPatientList.get(0).getName());
            pmsDiagnosisPatientResult.setPatientHomeAdress(pmsPatientList.get(0).getHomeAddress());
            pmsDiagnosisPatientResult.setPatientGender(pmsPatientList.get(0).getGender());
            pmsDiagnosisPatientResult.setPatientMedicalRecordNo(pmsPatientList.get(0).getMedicalRecordNo());
            if (pmsDiagnosisPatientResult.getRegistrationStatus() == 1){//待诊
                personalWaitList.add(pmsDiagnosisPatientResult);
            }
            else if (pmsDiagnosisPatientResult.getRegistrationStatus() == 2 || pmsDiagnosisPatientResult.getRegistrationStatus() == 6){//诊中
                personalDuringList.add(pmsDiagnosisPatientResult);
            }
            else if (pmsDiagnosisPatientResult.getRegistrationStatus() == 3){//诊毕
                personalEndList.add(pmsDiagnosisPatientResult);
            }
        }
        //封装PmsDiagnosisPatientListResult对象并返回
        PmsDiagnosisPatientListResult pmsDiagnosisPatientListResult = new PmsDiagnosisPatientListResult();
        pmsDiagnosisPatientListResult.setDeptWaitList(deptWaitList);
        pmsDiagnosisPatientListResult.setPersonalWaitList(personalWaitList);
        pmsDiagnosisPatientListResult.setPersonalDuringList(personalDuringList);
        pmsDiagnosisPatientListResult.setPersonalEndList(personalEndList);
        return pmsDiagnosisPatientListResult;
    }
    //1. 传入dms_registration的id和医生id
    //2. 先根据医生id、时间、午别查找sms_skd的id
    //3.根据dms_registration的id修改dms_registration中bind_status为1,并添加skd_id
    @Override
    public int bindPatient(Long registrationId, Long staffId) {
        //根据医生id、时间、午别查找sms_skd的id
        //获取当前午别
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("HH");
        String hourStr = df.format(date);
        int hour = Integer.parseInt(hourStr);
        Integer noon;//午别
        if (hour <= 12){
            noon = 0;//上午
        }
        else {
            noon = 1;//下午
        }
        SmsSkdExample smsSkdExample = new SmsSkdExample();
        smsSkdExample.createCriteria().andStaffIdEqualTo(staffId).andDateEqualTo(DateUtil.getDate(DateUtil.setMilliSecond(date,0))).andNoonEqualTo(noon);
        /*
        时间判断问题
         */
        //smsSkdExample.createCriteria().andStaffIdEqualTo(staffId).andNoonEqualTo(noon);
        List<SmsSkd> smsSkdList = smsSkdMapper.selectByExample(smsSkdExample);
        Long skdId = smsSkdList.get(0).getId();
        if (skdId == null){
            return 0;
        }
        //根据dms_registration的id修改dms_registration中bind_status为1,并添加skd_id
        DmsRegistrationExample dmsRegistrationExample = new DmsRegistrationExample();
        dmsRegistrationExample.createCriteria().andIdEqualTo(registrationId);
        DmsRegistration dmsRegistration = new DmsRegistration();
        dmsRegistration.setBindStatus(1);
        dmsRegistration.setSkdId(skdId);
        dmsRegistrationMapper.updateByExampleSelective(dmsRegistration,dmsRegistrationExample);
        return 1;
    }
    //1.调用PmsPatientService的updateDiagnosisSatus 根据挂号id修改status状态为2
    //2.调用PmsPatientService的selectCaseHistoryByPatient()
    @Override
    public DmsCaseHistoryResult startDiagnosis(Long registrationId) {
        updateDiagnosisStatus(registrationId,2);
        DmsCaseHistoryResult dmsCaseHistoryResult = selectCaseHistoryByPatient(registrationId);
        return dmsCaseHistoryResult;
    }
    //修改状态通用方法
    @Override
    public int updateDiagnosisStatus(Long registrationId, Integer status) {
        DmsRegistration dmsRegistration = new DmsRegistration();
        dmsRegistration.setId(registrationId);
        dmsRegistration.setStatus(status);
        dmsRegistrationMapper.updateByPrimaryKeySelective(dmsRegistration);
        return 1;
    }
    //1.根据registrationId查找出patientId
    //2.根据patientId查询历史病历(status为2)
    @Override
    public DmsCaseHistoryResult selectCaseHistoryByPatient(Long registrationId) {
        DmsRegistration dmsRegistration =  dmsRegistrationMapper.selectByPrimaryKey(registrationId);
        Long patientId = dmsRegistration.getPatientId();
        //List<DmsCaseHistory> dmsCaseHistoryList = new ArrayList<>();
        DmsCaseHistoryExample dmsCaseHistoryExample = new DmsCaseHistoryExample();
        dmsCaseHistoryExample.createCriteria().andPatientIdEqualTo(patientId).andStatusEqualTo(2);
        dmsCaseHistoryExample.setOrderByClause("create_time desc");//按创建时间降序
        List<DmsCaseHistory> dmsCaseHistoryList = dmsCaseHistoryMapper.selectByExample(dmsCaseHistoryExample);
        DmsCaseHistoryResult dmsCaseHistoryResult = new DmsCaseHistoryResult();
        dmsCaseHistoryResult.setDmsCaseHistoryList(dmsCaseHistoryList);
        return dmsCaseHistoryResult;
    }

    @Override
    public PmsPatientResult patientLogin(String identificationNo, String medicalRecordNo) {
        PmsPatientExample pmsPatientExample=new PmsPatientExample();
        pmsPatientExample.createCriteria().andIdentificationNoEqualTo(identificationNo).andMedicalRecordNoEqualTo(medicalRecordNo);
        List<PmsPatient> pmsPatientList = pmsPatientMapper.selectByExample(pmsPatientExample);
        if(CollectionUtil.isEmpty(pmsPatientList)||pmsPatientList.size()>1){
            return null;
        }

        //数据正常的情况下，只会返回一个病人
        PmsPatientResult pmsPatientResult=new PmsPatientResult();
        BeanUtils.copyProperties(pmsPatientList.get(0),pmsPatientResult);

        return pmsPatientResult;

    }

    /**
     * 根据openId查询患者信息
     *
     * @param openId
     * @return
     */
    @Override
    public PmsPatientResult getPatient(String openId) {
        PmsPatientExample example = new PmsPatientExample();
        example.createCriteria().andOpenIdEqualTo(openId);
        List<PmsPatient> pmsPatients = pmsPatientMapper.selectByExample(example);
        PmsPatientResult pmsPatientResult=new PmsPatientResult();
        if (CollectionUtils.isEmpty(pmsPatients)) {
            return null;
        }
        BeanUtils.copyProperties(pmsPatients.get(0),pmsPatientResult);
        BmsAccountExample bmsAccountExample=new BmsAccountExample();
        bmsAccountExample.createCriteria().andPatientIdEqualTo(pmsPatients.get(0).getId());
        List<BmsAccount> bmsAccounts = bmsAccountMapper.selectByExample(bmsAccountExample);
        if(!CollectionUtils.isEmpty(bmsAccounts)){
            pmsPatientResult.setBlance(bmsAccounts.get(0).getBlance());
        }
        return pmsPatientResult;
    }

    /**
     * 根据openId查询已关注医生
     *
     * @param openId
     * @return
     */
    @Override
    public List<SmsStaffResult> queryStaffByOpenId(String openId) {

        List<SmsStaffResult> list = new ArrayList<>();
        PmsPatientExample example = new PmsPatientExample();
        example.createCriteria().andOpenIdEqualTo(openId);
        List<PmsPatient> pmsPatients = pmsPatientMapper.selectByExample(example);

        SmsPatientFollowExample smsPatientFollowExample = new SmsPatientFollowExample();
        smsPatientFollowExample.createCriteria().andPatientIdEqualTo(pmsPatients.get(0).getId()).andStateEqualTo((long)1);
        List<SmsPatientFollow> smsPatientFollows = smsPatientFollowMapper.selectByExample(smsPatientFollowExample);

        if (!CollectionUtils.isEmpty(smsPatientFollows)) {
            smsPatientFollows.forEach(smsPatientFollow -> {
                SmsStaffResult smsStaffResult = new SmsStaffResult();
                SmsStaff smsStaff = smsStaffMapper.selectByPrimaryKey(smsPatientFollow.getStaffId());
                SmsDept smsDept = smsDeptMapper.selectByPrimaryKey(smsStaff.getDeptId());
                smsStaffResult.setDept(smsDept);
                BeanUtils.copyProperties(smsStaff,smsStaffResult);
                list.add(smsStaffResult);
            });
            return list;
        }

        return list;
    }

    /**
     * 取消关注
     *
     * @param smsStopFollowDoctorParam
     * @return
     */
    @Override
    public Boolean stopFollow(SmsStopFollowDoctorParam smsStopFollowDoctorParam) {
        SmsPatientFollowExample example = new SmsPatientFollowExample();
        example.createCriteria().andStaffIdEqualTo(smsStopFollowDoctorParam.getStaffId()).andPatientIdEqualTo(smsStopFollowDoctorParam.getPatientId());
        List<SmsPatientFollow> smsPatientFollows = smsPatientFollowMapper.selectByExample(example);
        SmsPatientFollow smsPatientFollow = new SmsPatientFollow();
        smsPatientFollow.setId(smsPatientFollows.get(0).getId());
        smsPatientFollow.setState((long)0);
        int i = smsPatientFollowMapper.updateByPrimaryKeySelective(smsPatientFollow);
        if (i >0) {
            return true;
        }
        return false;
    }
    /**
     * 根据openId获得patient
     * @param openId
     * @return
     */
    @Override
    public PmsPatient patientByOpenId(String openId) {
        PmsPatientExample pmsPatientExample=new PmsPatientExample();
        pmsPatientExample.createCriteria().andOpenIdEqualTo(openId);
        PmsPatient pmsPatient = pmsPatientMapper.selectByExample(pmsPatientExample).get(0);
        return pmsPatient;
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

    @Override
    public PmsPatient insertPatient(PmsPatientParam pmsPatientParam) {
        PmsPatientExample pmsPatientExample=new PmsPatientExample();
        pmsPatientExample.createCriteria().andIdentificationNoEqualTo(pmsPatientParam.getIdentificationNo());
        List<PmsPatient> pmsPatientsList = pmsPatientMapper.selectByExample(pmsPatientExample);
        if(!CollectionUtils.isEmpty(pmsPatientsList)){
            PmsPatient pmsPatient = pmsPatientsList.get(0);
            pmsPatient.setOpenId(pmsPatientParam.getOpenId());
            int i = pmsPatientMapper.updateByPrimaryKey(pmsPatient);
            return pmsPatient;
        }else {
            PmsPatient  pmsPatient = new PmsPatient();
            pmsPatient.setDateOfBirth(pmsPatientParam.getDateOfBirth());
            pmsPatient.setHomeAddress(pmsPatientParam.getHomeAddress());
            pmsPatient.setGender(pmsPatientParam.getGender());
            pmsPatient.setPhoneNo(pmsPatientParam.getPhoneNo());
            pmsPatient.setIdentificationNo(pmsPatientParam.getIdentificationNo());
            pmsPatient.setName(pmsPatientParam.getName());
            pmsPatient.setMedicalRecordNo(generateMedicalRecordNo(pmsPatientParam.getIdentificationNo()));
            pmsPatient.setOpenId(pmsPatientParam.getOpenId());
            pmsPatient.setPicture(pmsPatientParam.getPicture());
            int insert = pmsPatientMapper.insertSelective(pmsPatient);
            if (insert > 0) {
                PmsPatientExample example = new PmsPatientExample();
                example.createCriteria().andIdentificationNoEqualTo(pmsPatient.getIdentificationNo());
                List<PmsPatient> pmsPatients = pmsPatientMapper.selectByExample(example);
                insertBmsAccount(pmsPatients.get(0));
                return pmsPatients.get(0);
            }
        }
        return null;
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

    public int insertBmsAccount(PmsPatient pmsPatient) {
        if(!StringUtils.isEmpty(pmsPatient)){

            //查看病人是不是有账户
            BmsAccount bmsAccount1 = SelectAccountByCardId(pmsPatient);
            //如果没有新建账户
            if(StringUtils.isEmpty(bmsAccount1)){
                BmsAccount bmsAccount=new BmsAccount();
                bmsAccount.setPatientId(pmsPatient.getId());
                bmsAccount.setAccountCode(UUID.randomUUID().toString().trim().replaceAll("-", ""));
                bmsAccount.setBlance(BigDecimal.valueOf(0));
                bmsAccount.setFrozen(BigDecimal.valueOf(0));
                bmsAccount.setSummery(BigDecimal.valueOf(0));
                bmsAccount.setCardId(pmsPatient.getIdentificationNo());
                bmsAccount.setLevel(1);
                bmsAccount.setAccountStatus(1);
                bmsAccount.setCreateUser(-1L);
                bmsAccount.setCreateTime(new Date());
                bmsAccount.setUpdateUser(-1L);
                bmsAccount.setUpdateTime(new Date());
                bmsAccount.setIsDeleted(0);
                return bmsAccountMapper.insertSelective(bmsAccount);
            }
        }
        return 0;
    }

    public BmsAccount SelectAccountByCardId(PmsPatient pmsPatient) {
        if(!StringUtils.isEmpty(pmsPatient)){
            BmsAccountExample bmsAccountExample=new BmsAccountExample();
            bmsAccountExample.createCriteria().andPatientIdEqualTo(pmsPatient.getId());
            List<BmsAccount> bmsAccounts = bmsAccountMapper.selectByExample(bmsAccountExample);
            if (!CollectionUtils.isEmpty(bmsAccounts)){
                return bmsAccounts.get(0);
            }
        }
        return null;
    }

}
