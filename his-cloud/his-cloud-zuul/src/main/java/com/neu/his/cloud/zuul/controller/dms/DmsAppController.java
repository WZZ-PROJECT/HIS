package com.neu.his.cloud.zuul.controller.dms;


import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.neu.his.cloud.zuul.common.CommonResult;
import com.neu.his.cloud.zuul.distribution.api.pc.dms.ApiDmsAppService;
import com.neu.his.cloud.zuul.dto.dms.*;
import com.neu.his.cloud.zuul.dto.dms.*;
import com.neu.his.cloud.zuul.dto.dms.DmsCaseHistoryResult;
import com.neu.his.cloud.zuul.dto.dms.DmsDiseCatalogResult;
import com.neu.his.cloud.zuul.dto.dms.DmsStaffResult;
import com.neu.his.cloud.zuul.dto.dms.SmsStaffRecord;
import com.neu.his.cloud.zuul.model.SmsStaff;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "DmsAppController", description = "微信小程序")
@RequestMapping("/staff")
@CrossOrigin(allowCredentials = "true")
public class DmsAppController {

    @Autowired
    ApiDmsAppService apiDmsAppService;

    @HystrixCommand(fallbackMethod = "listHistoryDoctorFallbackInfo")
    @ApiOperation(value = "根据病人Id查询历史医生")
    @RequestMapping(value = "/listHistoryDoctor", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<SmsStaffRecord>> listHistoryDoctor(@RequestParam("patientId") Long patientId){
        return apiDmsAppService.listHistoryDoctor(patientId);
    }
    private CommonResult<List<SmsStaffRecord>> listHistoryDoctorFallbackInfo(@RequestParam("patientId") Long patientId){
        return CommonResult.success(null,"请检查您的网络") ;
    }

    @HystrixCommand(fallbackMethod = "listDoctorFallbackInfo")
    @ApiOperation(value = "根据Id查询医生信息和医生排班信息")
    @RequestMapping(value = "/listDoctor", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<DmsStaffResult> listDoctor(@RequestParam("staffId") Long staffId,@RequestParam("patientId") Long patientId){
        return apiDmsAppService.listDoctor(staffId,patientId);
    }
    private CommonResult<DmsStaffResult> listDoctorFallbackInfo(Long staffId,Long patientId){
        return CommonResult.success(null,"请检查您的网络") ;
    }

    @HystrixCommand(fallbackMethod = "selectStaffByIdFallbackInfo")
    @ApiOperation(value = "根据医生ID查询医生信息")
    @GetMapping(value = "/selectStaffById")
    @ResponseBody
    public CommonResult<DmsStaffResult> selectStaffById(@RequestParam("id") Long id,@RequestParam("patientId") Long patientId){
        return apiDmsAppService.selectStaffById(id,patientId);
    }
    private CommonResult<DmsStaffResult> selectStaffByIdFallbackInfo(@RequestParam("id") Long id,@RequestParam("patientId") Long patientId){
        return CommonResult.success(null,"请检查您的网络") ;
    }

    @HystrixCommand(fallbackMethod = "listRegistrationBillFallbackInfo")
    @ApiOperation(value = "查询该医生挂号账单")
    @PostMapping(value = "/listRegistrationBill")
    @ResponseBody
    public CommonResult<DmsDoctorBillsResult> listRegistrationBill(@RequestBody DmsDoctorBillsParam dmsDoctorBillsParam){
        return apiDmsAppService.listRegistrationBill(dmsDoctorBillsParam);
    }
    public CommonResult<DmsDoctorBillsResult> listRegistrationBillFallbackInfo(@RequestBody DmsDoctorBillsParam dmsDoctorBillsParam){
        return CommonResult.success(null,"请检查您的网络") ;
    }

    @HystrixCommand(fallbackMethod = "selectStaffByNameFallbackInfo")
    @ApiOperation(value = "医生名字模糊查询信息")
    @GetMapping(value = "/selectStaffByName")
    @ResponseBody
    public CommonResult<List<DmsStaffResult>> selectStaffByName(@RequestParam("name") String name){
        return apiDmsAppService.selectStaffByName(name);
    }
    public CommonResult<List<DmsStaffResult>> selectStaffByNameFallbackInfo(@RequestParam("name") String name){
        return CommonResult.success(null,"请检查您的网络") ;
    }

    @HystrixCommand(fallbackMethod = "attentionStaffFallbackInfo")
    @ApiOperation(value = "病人关注医生")
    @PostMapping(value = "/attentionStaff")
    @ResponseBody
    public CommonResult attentionStaff(@RequestBody DmsDoctorBillsParam dmsDoctorBillsParam){
        return apiDmsAppService.attentionStaff(dmsDoctorBillsParam);
    }
    public CommonResult attentionStaffFallbackInfo(@RequestBody DmsDoctorBillsParam dmsDoctorBillsParam){
        return CommonResult.success(null,"请检查您的网络") ;
    }


    @HystrixCommand(fallbackMethod = "listNowDoctorRegistrationFallbackInfo")
    @ApiOperation(value = "根据科室Id查询医生、科室、排班信息")
    @RequestMapping(value = "/listNowDoctorRegistration", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<DmsNowDoctorRegistrationResult>> listNowDoctorRegistration(@RequestParam("deptId") Long deptId,
                                                                                        @RequestParam("thedate") String thedate){
        return apiDmsAppService.listNowDoctorRegistration(deptId,thedate);
    }
    private CommonResult<List<DmsNowDoctorRegistrationResult>> listNowDoctorRegistrationFallbackInfo( Long deptId,String thedate){
        return CommonResult.success(null,"请检查您的网络") ;
    }

    @HystrixCommand(fallbackMethod = "listPatientConventionFallbackInfo")
    @ApiOperation(value = "根据openID获取当前患者已预约的信息列表")
    @RequestMapping(value = "/listPatientConvention", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<BookingInformationResult>> listPatientConvention(@RequestParam("openID") String openID){
        return apiDmsAppService.listPatientConvention(openID);
    }
    private CommonResult<List<BookingInformationResult>> listPatientConventionFallbackInfo(@RequestParam("openID") String openID){
        return CommonResult.success(null,"请检查您的网络") ;
    }

    @HystrixCommand(fallbackMethod = "listPatientConventionUpdateFallbackInfo")
    @ApiOperation(value = "根据openID获取当前患者已预约的信息列表")
    @RequestMapping(value = "/listPatientConventionUpdate", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<BookingInformationResult>> listPatientConventionUpdate(@RequestParam("Id") Long Id){
        return apiDmsAppService.listPatientConventionUpdate(Id);
    }
    private CommonResult<List<BookingInformationResult>> listPatientConventionUpdateFallbackInfo(@RequestParam("Id") Long Id){
        return CommonResult.success(null,"请检查您的网络") ;
    }
}