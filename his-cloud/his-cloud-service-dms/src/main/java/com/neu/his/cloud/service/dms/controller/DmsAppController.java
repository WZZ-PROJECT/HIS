package com.neu.his.cloud.service.dms.controller;


import com.neu.his.cloud.service.dms.common.CommonResult;
import com.neu.his.cloud.service.dms.dto.dms.*;
import com.neu.his.cloud.service.dms.model.SmsStaff;
import com.neu.his.cloud.service.dms.service.DmsAppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@Api(tags = "DmsAppController", description = "微信小程序")
@RequestMapping("/staff")
@CrossOrigin(allowCredentials = "true")
public class DmsAppController {

    @Autowired
    DmsAppService dmsAppService;

    //1.根据病人Id查询该病人历史就诊医生信息
    @ApiOperation(value = "查询该病人历史就诊医生信息")
    @RequestMapping(value = "/listHistoryDoctor", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<SmsStaffRecord>> listHistoryDoctor(@RequestParam("patientId")  Long patientId){
        return CommonResult.success(dmsAppService.listHistoryDoctor(patientId));
    }


    //根据医生ID获得医生信息以及排班信息
    @ApiOperation(value = "根据医生ID获得医生信息以及排班信息")
    @RequestMapping(value = "/listDoctor", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<DmsStaffResult> listDoctor(@RequestParam("staffId") Long staffId,@RequestParam Long patientId ){
        return CommonResult.success(dmsAppService.listDoctor(staffId,patientId));
    }

    //1.根据科室Id查询医生、科室、排班信息
    @ApiOperation(value = "根据科室Id查询医生、科室、排班信息")
    @RequestMapping(value = "/listNowDoctorRegistration", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<DmsNowDoctorRegistrationResult>> listNowDoctorRegistration(@RequestParam("deptId")  Long deptId,
                                                                                        @RequestParam("thedate") String thedate){
        return CommonResult.success(dmsAppService.listNowDoctorRegistration(deptId,thedate));
    }

    @ApiOperation(value = "根据医生ID查询医生信息")
    @GetMapping("/selectStaffById")
    @ResponseBody
    public CommonResult<DmsStaffResult> selectStaffById(@RequestParam Long id,@RequestParam("patientId") Long patientId){
        DmsStaffResult dmsStaffResult = dmsAppService.selectStaffById(id,patientId);
        return CommonResult.success(dmsStaffResult);
    }

    @ApiOperation(value = "查询该医生挂号账单")
    @PostMapping("/listRegistrationBill")
    @ResponseBody
    public CommonResult<DmsDoctorBillsResult> listRegistrationBill(@RequestBody DmsDoctorBillsParam dmsDoctorBillsParam){
        DmsDoctorBillsResult dmsDoctorBillsResult = dmsAppService.listRegistrationBill(dmsDoctorBillsParam);
        return CommonResult.success(dmsDoctorBillsResult);
    }

    @ApiOperation(value = "医生名字模糊查询信息")
    @GetMapping("/selectStaffByName")
    @ResponseBody
    public CommonResult<List<DmsStaffResult>> selectStaffByName(@RequestParam("name") String name){
        List<DmsStaffResult> DmsStaffResult = dmsAppService.selectStaffByName(name);
        return CommonResult.success(DmsStaffResult);
    }

    @ApiOperation(value = "病人关注医生")
    @PostMapping("/attentionStaff")
    @ResponseBody
    public CommonResult attentionStaff(@RequestBody DmsDoctorBillsParam dmsDoctorBillsParam){
        int result = dmsAppService.attentionStaff(dmsDoctorBillsParam);
        if (result > 0) {
            return CommonResult.success("关注成功");
        }
        return CommonResult.failed("关注失败");
    }

    //1.根据openID查询预约信息
    @ApiOperation(value = "根据科室Id查询医生、科室、排班信息")
    @RequestMapping(value = "/listPatientConvention", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<BookingInformationResult>> listPatientConvention(@RequestParam("openID")  String openID){
        return CommonResult.success(dmsAppService.listPatientConvention(openID));
    }

    //1.根据openID查询预约信息
    @ApiOperation(value = "根据预约id，取消预约接口，更改预约信息状态")
    @RequestMapping(value = "/listPatientConventionUpdate", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<BookingInformationResult>> listPatientConventionUpdate(@RequestParam("Id")  Long Id){
        return CommonResult.success(dmsAppService.listPatientConventionUpdate(Id));
    }
}
