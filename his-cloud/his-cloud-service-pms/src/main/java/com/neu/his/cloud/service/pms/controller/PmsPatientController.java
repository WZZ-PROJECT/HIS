package com.neu.his.cloud.service.pms.controller;

import com.neu.his.cloud.service.pms.common.CommonResult;
import com.neu.his.cloud.service.pms.dto.pms.PmsPatientParam;
import com.neu.his.cloud.service.pms.dto.pms.PmsPatientResult;
import com.neu.his.cloud.service.pms.dto.pms.PmsPatients;
import com.neu.his.cloud.service.pms.dto.sms.SmsStaffResult;
import com.neu.his.cloud.service.pms.dto.sms.SmsStopFollowDoctorParam;
import com.neu.his.cloud.service.pms.model.PmsPatient;
import com.neu.his.cloud.service.pms.model.SmsStaff;
import com.neu.his.cloud.service.pms.service.PmsPatientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Api(tags = "PmsPatientController", description = "病人管理")
@RequestMapping("/pmsPatient")
@CrossOrigin(allowCredentials = "true")
public class PmsPatientController {

    @Autowired
    private PmsPatientService pmsPatientService;
    /**
     * 描述:患者手机端登录
     * <p>author: ma
     */
    @ApiOperation("患者 手机端登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<PmsPatientResult> login(@RequestParam("identificationNo") String identificationNo,
                                                @RequestParam("medicalRecordNo") String medicalRecordNo){
        PmsPatientResult result = pmsPatientService.patientLogin(identificationNo ,medicalRecordNo);
        System.err.println("result:"+result);
        if(result == null){
            return  CommonResult.failed("登录失败");
        }else {
            return CommonResult.success(result,"登录成功");
        }
    }

    @ApiOperation("根据openID获取当前患者已关注的医生信息列表")
    @RequestMapping(value = "/queryStaffByOpenId", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<SmsStaffResult>> queryStaffByOpenId(@RequestParam("openId") String openId){
        List<SmsStaffResult> result = pmsPatientService.queryStaffByOpenId(openId);
        return CommonResult.success(result,"列表成功返回");
    }

    @ApiOperation("根据医生和患者的id，取消关注接口，更改关注状态")
    @RequestMapping(value = "/stopFollow", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Boolean> stopFollow(@RequestBody SmsStopFollowDoctorParam smsStopFollowDoctorParam){
        Boolean result = pmsPatientService.stopFollow(smsStopFollowDoctorParam);
        return CommonResult.success(result,"操作成功");
    }

    @ApiOperation("根据openId 获得 patient")
    @RequestMapping(value = "/patientByOpenId", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<PmsPatients> patientByOpenId(@RequestParam String openId){
        PmsPatients pmsPatient = pmsPatientService.patientByOpenId(openId);
        return CommonResult.success(pmsPatient,"操作成功");
    }

    @ApiOperation("添加患者并绑定")
    @RequestMapping(value = "/insertBindPmsPatient", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<PmsPatient> insertBindPmsPatient(@RequestBody PmsPatientParam pmsPatientParam){
        PmsPatient pmsPatient = pmsPatientService.insertPatient(pmsPatientParam);
        return CommonResult.success(pmsPatient,"操作成功");
    }
}
