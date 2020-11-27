package com.neu.his.cloud.api.pc.controller.pms;


import com.neu.his.cloud.api.pc.common.CommonResult;
import com.neu.his.cloud.api.pc.dto.pms.PmsPatientParam;
import com.neu.his.cloud.api.pc.dto.pms.PmsPatients;
import com.neu.his.cloud.api.pc.dto.sms.SmsStaffResult;
import com.neu.his.cloud.api.pc.dto.sms.SmsStopFollowDoctorParam;
import com.neu.his.cloud.api.pc.model.PmsPatient;
import com.neu.his.cloud.api.pc.service.pms.PmsPatientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "PmsPatientController", description = "病人管理")
@RequestMapping("/pmsPatient")
@CrossOrigin(allowCredentials = "true")
public class PmsPatientController {

    @Autowired
    PmsPatientService pmsPatientService;

    @ApiOperation("根据openID获取当前患者已关注的医生信息列表")
    @RequestMapping(value = "/queryStaffByOpenId", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<SmsStaffResult>> queryStaffByOpenId(@RequestParam("openId") String openId){
        return pmsPatientService.queryStaffByOpenId(openId);
    }

    @ApiOperation("根据医生和患者的id，取消关注接口，更改关注状态")
    @RequestMapping(value = "/stopFollow", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Boolean> stopFollow(@RequestBody SmsStopFollowDoctorParam smsStopFollowDoctorParam){
        return pmsPatientService.stopFollow(smsStopFollowDoctorParam);
    }

    @ApiOperation("根据 openId 获得 PmsPatient")
    @RequestMapping(value = "/patientByOpenId", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<PmsPatients> patientByOpenId(@RequestParam String openId){
        return pmsPatientService.patientByOpenId(openId);
    }

    @ApiOperation("添加患者并绑定")
    @RequestMapping(value = "/insertBindPmsPatient", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<PmsPatient> insertBindPmsPatient(@RequestBody PmsPatientParam pmsPatientParam){
        return pmsPatientService.insertBindPmsPatient(pmsPatientParam);
    }


}
