package com.neu.his.cloud.service.dms.service.impl;

import com.neu.his.cloud.service.dms.dto.dms.*;
import com.neu.his.cloud.service.dms.mapper.*;
import com.neu.his.cloud.service.dms.model.*;
import com.neu.his.cloud.service.dms.service.DmsAppService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class DmsAppServiceImpl implements DmsAppService {

    @Resource
    DmsRegistrationMapper dmsRegistrationMapper;

    @Resource
    SmsSkdMapper smsSkdMapper;

    @Resource
    SmsStaffMapper smsStaffMapper;

    @Resource
    SmsDeptMapper smsDeptMapper;

    @Resource
    SmsRegistrationRankMapper smsRegistrationRankMapper;

    @Resource
    PmsPatientMapper patientMapper;

    @Resource
    SmsPatientFollowMapper smsPatientFollowMapper;

    @Resource
    PmsPatientMapper pmsPatientMapper;

    @Override
    public List<SmsStaffRecord> listHistoryDoctor(Long patientId) {

        List<SmsStaffRecord> smsStaffRecordList=new ArrayList<>();

        //根据病人ID取出Skd_id
        DmsRegistrationExample dmsRegistrationExample = new DmsRegistrationExample();
        dmsRegistrationExample.createCriteria().andPatientIdEqualTo(patientId);
        List<DmsRegistration> dmsRegistrationList  = dmsRegistrationMapper.selectByExample(dmsRegistrationExample);
        List<Long> skdList=new ArrayList<>();

        if(!CollectionUtils.isEmpty(dmsRegistrationList)){
            dmsRegistrationList.stream().forEach(dmsRegistration ->{
                skdList.add(dmsRegistration.getSkdId());
            });
            //根据Skd_id查sms_skd取出staff_id
            SmsSkdExample smsSkdExample=new SmsSkdExample();
            smsSkdExample.createCriteria().andIdIn(skdList);
            List<SmsSkd> smsSkdList=smsSkdMapper.selectByExample(smsSkdExample);
            List<Long> staffIdList=new ArrayList<>();
            if(smsSkdList!=null){
                smsSkdList.stream().forEach(smsSkd->{
                    staffIdList.add(smsSkd.getStaffId());
                });
                SmsStaffExample smsStaffExample=new SmsStaffExample();
                smsStaffExample.createCriteria().andIdIn(staffIdList);
                List<SmsStaff> smsStaffList = smsStaffMapper.selectByExample(smsStaffExample);
                smsStaffList.stream().forEach(smsStaff -> {
                    SmsStaffRecord smsStaffRecord=new SmsStaffRecord();
                    smsStaffRecord.setStaffId(smsStaff.getId());
                    smsStaffRecord.setStaffName(smsStaff.getName());
                    smsStaffRecord.setPicture(smsStaff.getPicture());
                    smsStaffRecord.setAdvantages(smsStaff.getAdvantages());
                    smsStaffRecord.setDescription(smsStaff.getDescription());
                    SmsDept smsDept = smsDeptMapper.selectByPrimaryKey(smsStaff.getDeptId());
                    smsStaffRecord.setDeptId(smsDept.getId());
                    smsStaffRecord.setDeptName(smsDept.getName());
                    smsStaffRecordList.add(smsStaffRecord);
                });
                return smsStaffRecordList;
            }
        }
        return smsStaffRecordList;
    }


    @Override
    public DmsStaffResult listDoctor(Long staffId,Long patientId) {


        //拿去医生的信息
        DmsStaffResult dmsStaffResult=new DmsStaffResult();

        //查看是否关注改医生
        SmsPatientFollowExample smsPatientFollowExample=new SmsPatientFollowExample();
        smsPatientFollowExample.createCriteria().andStaffIdEqualTo(staffId).andPatientIdEqualTo(patientId);
        List<SmsPatientFollow> smsPatientFollows = smsPatientFollowMapper.selectByExample(smsPatientFollowExample);
        if(!CollectionUtils.isEmpty(smsPatientFollows)){
            SmsPatientFollow smsPatientFollow = smsPatientFollows.get(0);
            dmsStaffResult.setState(smsPatientFollow.getState());
        }else {
            dmsStaffResult.setState(Long.parseLong("0"));
        }
        SmsStaff smsStaff = smsStaffMapper.selectByPrimaryKey(staffId);
        dmsStaffResult.setStaffId(smsStaff.getId());
        dmsStaffResult.setStaffName(smsStaff.getName());
        dmsStaffResult.setDeptId(smsStaff.getDeptId());
        dmsStaffResult.setTitle(smsStaff.getTitle());
        dmsStaffResult.setAdvantages(smsStaff.getAdvantages());
        dmsStaffResult.setDescription(smsStaff.getDescription());
        dmsStaffResult.setPicture(smsStaff.getPicture());

        //拿去科室信息
        SmsDept smsDept = smsDeptMapper.selectByPrimaryKey(smsStaff.getDeptId());
        dmsStaffResult.setDeptName(smsDept.getName());

        //拿去挂号费用
        SmsRegistrationRank smsRegistrationRank = smsRegistrationRankMapper.selectByPrimaryKey(smsStaff.getRegistrationRankId());

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 6);
        Date today = calendar.getTime();

        SmsSkdExample smsSkdExample=new SmsSkdExample();
        smsSkdExample.createCriteria().andStaffIdEqualTo(staffId).andDateBetween(getStartOfDay(new Date()),getEndOfDay(today));
        List<SmsSkd> smsSkdList = smsSkdMapper.selectByExample(smsSkdExample);
        List<SmsSkdResult> smsSkdResults=new ArrayList<>();
        smsSkdList.stream().forEach(smsSkd -> {

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = formatter.format(smsSkd.getDate());
            SmsSkdResult smsSkdResult=new SmsSkdResult();
            smsSkdResult.setDate(dateString);
            smsSkdResult.setWeek(dateToWeek(dateString));
            smsSkdResult.setNoon(smsSkd.getNoon());
            smsSkdResult.setRemain(smsSkd.getRemain()>0?0:1);//1是约满
            smsSkdResult.setAmount(smsRegistrationRank.getPrice());
            smsSkdResult.setSkdId(smsSkd.getId());
            smsSkdResults.add(smsSkdResult);
        });
        dmsStaffResult.setSkdList(smsSkdResults);

        return dmsStaffResult;
    }

    /**
     * @param id
     * @return DmsStaffResult
     * @since 根据医生id查询医生详细信息
     */
    @Override
    public DmsStaffResult selectStaffById(Long id,Long patientId) {

        DmsStaffResult result = new DmsStaffResult();
        SmsPatientFollowExample smsPatientFollowExample=new SmsPatientFollowExample();
        smsPatientFollowExample.createCriteria().andStaffIdEqualTo(id).andPatientIdEqualTo(patientId);

        //查询医生详情
        SmsStaff smsStaff = smsStaffMapper.selectByPrimaryKey(id);
        //根据deptId查询科室详情
        SmsDept smsDept = smsDeptMapper.selectByPrimaryKey(smsStaff.getDeptId());

        List<SmsPatientFollow> smsPatientFollows = smsPatientFollowMapper.selectByExample(smsPatientFollowExample);
        if (CollectionUtils.isEmpty(smsPatientFollows)) {
            result.setState((long)0);
        } else {
            SmsPatientFollow smsPatientFollow = smsPatientFollows.get(0);
            result.setState(smsPatientFollow.getState());
        }
        BeanUtils.copyProperties(smsStaff,result);
        result.setStaffId(smsStaff.getId());
        result.setStaffName(smsStaff.getName());
        result.setDeptId(smsStaff.getDeptId());
        result.setDeptName(smsDept.getName());
        result.setPicture(smsStaff.getPicture());
        result.setDescription(smsStaff.getDescription());
        result.setAdvantages(smsStaff.getAdvantages());
        result.setTitle(smsStaff.getTitle());

        return result;
    }

    /**
     * @param dmsDoctorBillsParam
     * @return
     * @since 查询医生挂号所需费用
     */
    @Override
    public DmsDoctorBillsResult listRegistrationBill(DmsDoctorBillsParam dmsDoctorBillsParam) {
        DmsDoctorBillsResult doctorBillsResult = new DmsDoctorBillsResult();
        DmsStaffResult result = selectStaffById(dmsDoctorBillsParam.getStaffId(),dmsDoctorBillsParam.getPatientId());
        BeanUtils.copyProperties(result,doctorBillsResult);
        SmsStaff smsStaff = smsStaffMapper.selectByPrimaryKey(dmsDoctorBillsParam.getStaffId());
        //查询费用
        SmsRegistrationRank smsRegistrationRank = smsRegistrationRankMapper.selectByPrimaryKey(smsStaff.getRegistrationRankId());
        doctorBillsResult.setAmount(smsRegistrationRank.getPrice());
        PmsPatient pmsPatient = patientMapper.selectByPrimaryKey(dmsDoctorBillsParam.getPatientId());
        doctorBillsResult.setPatientId(pmsPatient.getId());
        doctorBillsResult.setPatientName(pmsPatient.getName());
        doctorBillsResult.setCard(pmsPatient.getCard());
        return doctorBillsResult;
    }

    /**
     * 根据医生的名字模糊查询
     *
     * @param name
     * @return
     */
    @Override
    public List<DmsStaffResult> selectStaffByName(String name) {
        List<DmsStaffResult> list = new ArrayList<>();
        SmsStaffExample example = new SmsStaffExample();
        example.createCriteria().andNameLike(name);
        List<SmsStaff> smsStaffs = smsStaffMapper.selectByExample(example);
        smsStaffs.forEach(result ->{
            DmsStaffResult staffResult = new DmsStaffResult();
            staffResult.setStaffId(result.getId());
            staffResult.setStaffName(result.getName());
            staffResult.setDeptId(result.getDeptId());
            staffResult.setPicture(result.getPicture());
            staffResult.setTitle(result.getTitle());
            staffResult.setAdvantages(result.getAdvantages());
            staffResult.setDescription(result.getDescription());
            staffResult.setDeptName(smsDeptMapper.selectByPrimaryKey(result.getDeptId()).getName());
            list.add(staffResult);
        });
        return list;
    }

    /**
     * 添加关注
     *
     * @param dmsDoctorBillsParam
     * @return
     */
    @Override
    public int attentionStaff(DmsDoctorBillsParam dmsDoctorBillsParam) {
        SmsPatientFollow smsPatientFollow = new SmsPatientFollow();
        SmsStaff smsStaff = smsStaffMapper.selectByPrimaryKey(dmsDoctorBillsParam.getStaffId());
        //校验传入医生id可查出医生
        if (StringUtils.isEmpty(smsStaff)) {
            return 0;
        }
        PmsPatient pmsPatient = patientMapper.selectByPrimaryKey(dmsDoctorBillsParam.getPatientId());
        //校验传入病人id可查出医生
        if (StringUtils.isEmpty(pmsPatient)) {
            return 0;
        }
        //1 ：已关注 0：未关注
        Long state = (long)1;
        smsPatientFollow.setPatientId(dmsDoctorBillsParam.getPatientId());
        smsPatientFollow.setStaffId(dmsDoctorBillsParam.getStaffId());
        smsPatientFollow.setState(state);
        int insert = smsPatientFollowMapper.insert(smsPatientFollow);
        return insert;
    }

    public static String dateToWeek(String datetime) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        Calendar cal = Calendar.getInstance(); // 获得一个日历
        Date datet = null;
        try {
            datet = f.parse(datetime);
            cal.setTime(datet);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    @Override
    public List<DmsNowDoctorRegistrationResult> listNowDoctorRegistration(Long deptId,String thedate) {

        List<DmsNowDoctorRegistrationResult> dmsNowDoctorRegistrationResultList=new ArrayList<>();
        //根据deptId获得科室信息
        SmsDept smsDept = smsDeptMapper.selectByPrimaryKey(deptId);
        //根据科室查询属于该科室的医生信息（集合）
        SmsStaffExample smsStaffExample=new SmsStaffExample();
        smsStaffExample.createCriteria().andDeptIdEqualTo(deptId);
        List<SmsStaff> smsStaffList = smsStaffMapper.selectByExample(smsStaffExample);
        if(smsStaffList!=null){
            smsStaffList.stream().forEach(smsStaff -> {
                Date parse=null;
                try {
                    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
                    parse= simpleDateFormat.parse(thedate);
                }catch (Exception e){
                    e.printStackTrace();
                }
                SmsSkdExample smsSkdExample=new SmsSkdExample();
                smsSkdExample.createCriteria().andStaffIdEqualTo(smsStaff.getId()).andDateBetween(getStartOfDay(parse),getEndOfDay(parse));
                List<SmsSkd> smsSkdList = smsSkdMapper.selectByExample(smsSkdExample);
                if(!CollectionUtils.isEmpty(smsSkdList)){
                    smsSkdList.stream().forEach(smsSkd -> {
                        //封装医生信息以及科室信息
                        DmsNowDoctorRegistrationResult dmsNowDoctorRegistrationResult=new DmsNowDoctorRegistrationResult();
                        dmsNowDoctorRegistrationResult.setDeptId(smsDept.getId());
                        dmsNowDoctorRegistrationResult.setDeptName(smsDept.getName());
                        dmsNowDoctorRegistrationResult.setStaffId(smsStaff.getId());
                        dmsNowDoctorRegistrationResult.setStaffName(smsStaff.getName());
                        dmsNowDoctorRegistrationResult.setPicture(smsStaff.getPicture());
                        dmsNowDoctorRegistrationResult.setAdvantages(smsStaff.getAdvantages());
                        dmsNowDoctorRegistrationResult.setTitle(smsStaff.getTitle());
                        //封装诊费
                        SmsRegistrationRank smsRegistrationRank = smsRegistrationRankMapper.selectByPrimaryKey(smsStaff.getRegistrationRankId());
                        dmsNowDoctorRegistrationResult.setAmount(smsRegistrationRank.getPrice());
                        //封装排班信息
                        dmsNowDoctorRegistrationResult.setRemain(smsSkd.getRemain());
                        dmsNowDoctorRegistrationResult.setSkLimit(smsSkd.getSkLimit());
                        dmsNowDoctorRegistrationResult.setNoon(smsSkd.getNoon());
                        dmsNowDoctorRegistrationResult.setSkdId(smsSkd.getId());
                        dmsNowDoctorRegistrationResultList.add(dmsNowDoctorRegistrationResult);
                    });
                }
            });
        }

        return dmsNowDoctorRegistrationResultList;
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
    public List<BookingInformationResult> listPatientConvention(String openID) {

        List<BookingInformationResult> bookingInformationResultList=new ArrayList<>();

        //拿去病人信息
        PmsPatientExample pmsPatientExample=new PmsPatientExample();
        pmsPatientExample.createCriteria().andOpenIdEqualTo(openID);
        List<PmsPatient> pmsPatients = patientMapper.selectByExample(pmsPatientExample);
        if(CollectionUtils.isEmpty(pmsPatients)){
            return null;
        }
        PmsPatient pmsPatient = pmsPatients.get(0);
        //拿去挂号信息
        DmsRegistrationExample dmsRegistrationExample=new DmsRegistrationExample();
        dmsRegistrationExample.createCriteria().andPatientIdEqualTo(pmsPatient.getId());
        List<DmsRegistration> dmsRegistrationList = dmsRegistrationMapper.selectByExample(dmsRegistrationExample);

        dmsRegistrationList.stream().forEach(dmsRegistration -> {

            BookingInformationResult bookingInformationResult=new BookingInformationResult();

            SmsDept smsDept = smsDeptMapper.selectByPrimaryKey(dmsRegistration.getDeptId());
            SmsSkd smsSkd = smsSkdMapper.selectByPrimaryKey(dmsRegistration.getSkdId());
            SmsStaff smsStaff = smsStaffMapper.selectByPrimaryKey(smsSkd.getStaffId());
            SmsRegistrationRank smsRegistrationRank = smsRegistrationRankMapper.selectByPrimaryKey(smsStaff.getRegistrationRankId());

            bookingInformationResult.setId(dmsRegistration.getId());
            bookingInformationResult.setDeptId(smsDept.getId());
            bookingInformationResult.setDeptName(smsDept.getName());
            bookingInformationResult.setStaffId(smsStaff.getId());
            bookingInformationResult.setStaffName(smsStaff.getName());
            bookingInformationResult.setDescription(smsStaff.getDescription());
            bookingInformationResult.setPicture(smsStaff.getPicture());

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String dateString = formatter.format(dmsRegistration.getAttendanceDate());
            bookingInformationResult.setDate(dateString);

            bookingInformationResult.setPatientId(pmsPatient.getId());
            bookingInformationResult.setPatientName(pmsPatient.getName());
            bookingInformationResult.setAmount(smsRegistrationRank.getPrice());
            bookingInformationResult.setStatus(dmsRegistration.getStatus());
            bookingInformationResultList.add(bookingInformationResult);
        });
        return bookingInformationResultList;
    }

    @Override
    public List<BookingInformationResult> listPatientConventionUpdate(Long Id) {
        DmsRegistration dmsRegistration = dmsRegistrationMapper.selectByPrimaryKey(Id);
        dmsRegistration.setStatus(5);
        int i = dmsRegistrationMapper.updateByPrimaryKeySelective(dmsRegistration);
        if(i>0) {
            PmsPatient pmsPatient = pmsPatientMapper.selectByPrimaryKey(dmsRegistration.getPatientId());
            return listPatientConvention(pmsPatient.getOpenId());
        }
        return null;
    }
}
