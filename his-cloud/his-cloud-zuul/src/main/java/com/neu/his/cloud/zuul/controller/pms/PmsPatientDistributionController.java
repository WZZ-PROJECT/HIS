package com.neu.his.cloud.zuul.controller.pms;


import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.neu.his.cloud.zuul.common.CommonResult;
import com.neu.his.cloud.zuul.distribution.api.pc.pms.ApiPcPmsDiagnosisPatientDistributionService;
import com.neu.his.cloud.zuul.dto.pms.PmsDiagnosisPatientListResult;
import com.neu.his.cloud.zuul.dto.pms.PmsPatientParam;
import com.neu.his.cloud.zuul.dto.pms.PmsPatients;
import com.neu.his.cloud.zuul.dto.pms.PmsQueryPeople;
import com.neu.his.cloud.zuul.dto.sms.SmsStaffResult;
import com.neu.his.cloud.zuul.dto.sms.SmsStopFollowDoctorParam;
import com.neu.his.cloud.zuul.model.PmsPatient;
import com.neu.his.cloud.zuul.service.sms.SmsStaffService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "PmsPatientController", description = "病人管理")
@RequestMapping("/pmsPatient")
@CrossOrigin(allowCredentials = "true")
public class PmsPatientDistributionController {

    @Autowired
    ApiPcPmsDiagnosisPatientDistributionService apiPcPmsDiagnosisPatientDistributionService;

    @Autowired
    SmsStaffService smsStaffService;


    @HystrixCommand(fallbackMethod = "queryStaffByOpenIdFallbackInfo")
    @ApiOperation("根据openID获取当前患者已关注的医生信息列表")
    @RequestMapping(value = "/queryStaffByOpenId", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<SmsStaffResult>> queryStaffByOpenId(@RequestParam("openId") String openId){
        return apiPcPmsDiagnosisPatientDistributionService.queryStaffByOpenId(openId);
    }
    public CommonResult<List<SmsStaffResult>> queryStaffByOpenIdFallbackInfo(@RequestParam("openId") String openId){
        return CommonResult.success(null,"请检查您的网络") ;
    }

    @HystrixCommand(fallbackMethod = "stopFollowFallbackInfo")
    @ApiOperation("根据医生和患者的id，取消关注接口，更改关注状态")
    @RequestMapping(value = "/stopFollow", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Boolean> stopFollow(@RequestBody SmsStopFollowDoctorParam smsStopFollowDoctorParam){
        return apiPcPmsDiagnosisPatientDistributionService.stopFollow(smsStopFollowDoctorParam);
    }
    public CommonResult<Boolean> stopFollowFallbackInfo(@RequestBody SmsStopFollowDoctorParam smsStopFollowDoctorParam){
        return CommonResult.success(null,"请检查您的网络") ;
    }

    @HystrixCommand(fallbackMethod = "patientByOpenIdFallbackInfo")
    @ApiOperation("根据 openId 获得 patient")
    @RequestMapping(value = "/patientByOpenId", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<PmsPatients> patientByOpenId(@RequestParam String openId){
        CommonResult<PmsPatients> pmsPatientsCommonResult = apiPcPmsDiagnosisPatientDistributionService.patientByOpenId(openId);
        String token = smsStaffService.getToken(pmsPatientsCommonResult.getData().getName());
        pmsPatientsCommonResult.getData().setToken("Bearer"+token);
        /*pmsPatientsCommonResult.getData().setToken(token);*/
        return pmsPatientsCommonResult;
    }
    public CommonResult<PmsPatients> patientByOpenIdFallbackInfo(@RequestParam String openId){
        return CommonResult.success(null,"请检查您的网络") ;
    }

    @HystrixCommand(fallbackMethod = "insertBindPmsPatientFallbackInfo")
    @ApiOperation("添加患者并绑定")
    @RequestMapping(value = "/insertBindPmsPatient", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<PmsPatient> insertBindPmsPatient(@RequestBody PmsPatientParam pmsPatientParam){
        return apiPcPmsDiagnosisPatientDistributionService.insertBindPmsPatient(pmsPatientParam);
    }
    public CommonResult<PmsPatient> insertBindPmsPatientFallbackInfo(PmsPatientParam pmsPatientParam){
        return CommonResult.success(null,"请检查您的网络");
    }


}
