package com.neu.his.cloud.api.pc.service.pms;

import com.neu.his.cloud.api.pc.common.CommonResult;
import com.neu.his.cloud.api.pc.dto.dms.DmsCaseHistoryResult;
import com.neu.his.cloud.api.pc.dto.pms.PmsDiagnosisPatientListResult;
import com.neu.his.cloud.api.pc.dto.pms.PmsPatientParam;
import com.neu.his.cloud.api.pc.dto.pms.PmsPatientResult;
import com.neu.his.cloud.api.pc.dto.sms.SmsStaffResult;
import com.neu.his.cloud.api.pc.dto.sms.SmsStopFollowDoctorParam;
import com.neu.his.cloud.api.pc.model.PmsPatient;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "his-cloud-service-pms")
public interface PmsPatientService {

    @RequestMapping(value = "/diagnosisPatient/selectPatientByIdNo", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<PmsPatientResult> selectPatientByIdNo(@RequestParam("identificationNo") String identificationNo);


    @RequestMapping(value = "/diagnosisPatient/refreshPatient", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<PmsDiagnosisPatientListResult> refreshPatient(@RequestParam("staffId") Long staffId);


    @RequestMapping(value = "/diagnosisPatient/bindPatient", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult bindPatient(@RequestParam("registrationId") Long registrationId, @RequestParam("staffId") Long staffId);

    @RequestMapping(value = "/diagnosisPatient/startDiagnosis", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<DmsCaseHistoryResult> startDiagnosis(@RequestParam("registrationId") Long registrationId);

    @ApiOperation(value = "根据openId查询患者基本信息")
    @RequestMapping(value = "/diagnosisPatient/getPatient", method = RequestMethod.GET)
    @ResponseBody
    CommonResult<PmsPatientResult> getPatient(@RequestParam("openId") String openId);

    @ApiOperation("根据openID获取当前患者已关注的医生信息列表")
    @RequestMapping(value = "/pmsPatient/queryStaffByOpenId", method = RequestMethod.GET)
    @ResponseBody
    CommonResult<List<SmsStaffResult>> queryStaffByOpenId(@RequestParam("openId") String openId);

    @ApiOperation("根据医生和患者的id，取消关注接口，更改关注状态")
    @RequestMapping(value = "/pmsPatient/stopFollow", method = RequestMethod.POST)
    @ResponseBody
    CommonResult<Boolean> stopFollow(@RequestBody SmsStopFollowDoctorParam smsStopFollowDoctorParam);

    @ApiOperation("根据openId获得patient")
    @RequestMapping(value = "/pmsPatient/patientByOpenId", method = RequestMethod.GET)
    @ResponseBody
    CommonResult<PmsPatient> patientByOpenId(@RequestParam String openId);

    @ApiOperation("添加患者并绑定")
    @RequestMapping(value = "/pmsPatient/insertBindPmsPatient", method = RequestMethod.POST)
    @ResponseBody
    CommonResult<PmsPatient> insertBindPmsPatient(@RequestBody PmsPatientParam pmsPatientParam);

}
