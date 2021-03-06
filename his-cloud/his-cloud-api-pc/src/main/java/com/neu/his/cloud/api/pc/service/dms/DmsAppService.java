package com.neu.his.cloud.api.pc.service.dms;


import com.neu.his.cloud.api.pc.common.CommonResult;
import com.neu.his.cloud.api.pc.dto.dms.*;
import com.neu.his.cloud.api.pc.model.SmsStaff;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.*;

import java.util.List;
@FeignClient(value = "his-cloud-service-dms")
public interface DmsAppService {

    /**
     * 描述：查询该病人历史就诊医生信息
     */
    @RequestMapping(value = "/staff/listHistoryDoctor", method = RequestMethod.GET)
    CommonResult<List<SmsStaffRecord>> listHistoryDoctor(@RequestParam("patientId")  Long patientId);

    /**
     * 根据医生Id查询医生信息和排班信息
     */
    @RequestMapping(value = "/staff/listDoctor", method = RequestMethod.GET)
    CommonResult<DmsStaffResult> listDoctor(@RequestParam("staffId") Long staffId,@RequestParam Long patientId);


    @ApiOperation(value = "根据医生ID查询医生信息")
    @RequestMapping(value = "/staff/selectStaffById",method = RequestMethod.GET)
    CommonResult<DmsStaffResult> selectStaffById(@RequestParam("id") Long id,@RequestParam("patientId") Long patientId);


    @ApiOperation(value = "查询该医生挂号账单")
    @PostMapping("/staff/listRegistrationBill")
    CommonResult<DmsDoctorBillsResult> listRegistrationBill(@RequestBody DmsDoctorBillsParam dmsDoctorBillsParam);

    @ApiOperation(value = "医生名字模糊查询信息")
    @GetMapping("/staff/selectStaffByName")
    CommonResult<List<DmsStaffResult>> selectStaffByName(@RequestParam("name") String name);

    @ApiOperation(value = "病人关注医生")
    @PostMapping("/staff/attentionStaff")
    CommonResult attentionStaff(@RequestBody DmsDoctorBillsParam dmsDoctorBillsParam);


    /**
     * 描述：查询该病人历史就诊医生信息
     */
    @RequestMapping(value = "/staff/listNowDoctorRegistration", method = RequestMethod.GET)
    CommonResult <DmsNowDoctorRegistrationResults> listNowDoctorRegistration(@RequestParam("deptId")  Long deptId,
                                                                                 @RequestParam("thedate") String thedate);

    /**
     * 描述：根据openID获取当前患者已预约的信息列表
     */
    @RequestMapping(value = "/staff/listPatientConvention", method = RequestMethod.GET)
    CommonResult<List<BookingInformationResult>> listPatientConvention(@RequestParam("openID") String openID);

    @RequestMapping(value = "/staff/listPatientConventionUpdate", method = RequestMethod.GET)
    CommonResult<List<BookingInformationResult>> listPatientConventionUpdate(@RequestParam("Id") Long Id);

    /**
     *  查看信息
     */
    @RequestMapping(value = "/staff/selectMaintenanceParam", method = RequestMethod.POST)
    CommonResult<List<InformationMaintenance>> selectMaintenanceParam();

    /**
     *  修改信息
     */
    @RequestMapping(value = "/staff/updateMaintenanceParam", method = RequestMethod.POST)
    CommonResult updateMaintenanceParam(@RequestBody InformationMaintenance informationMaintenance);
}
