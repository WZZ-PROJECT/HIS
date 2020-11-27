package com.neu.his.cloud.api.pc.controller.dms;



import com.neu.his.cloud.api.pc.common.CommonResult;
import com.neu.his.cloud.api.pc.dto.dms.*;
import com.neu.his.cloud.api.pc.model.SmsStaff;
import com.neu.his.cloud.api.pc.service.dms.DmsAppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;


@RestController
@Api(tags = "DmsAppController", description = "微信小程序")
@RequestMapping("/staff")
@CrossOrigin(allowCredentials = "true")
public class DmsAppController {

    @Resource
    DmsAppService dmsAppService;

    @ApiOperation(value = "根据病人Id查询相关医生")
    @RequestMapping(value = "/listHistoryDoctor", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<SmsStaffRecord>>listHistoryDoctor(@RequestParam("patientId")  Long patientId){
        return dmsAppService.listHistoryDoctor(patientId);
    }

    @ApiOperation(value = "根据医生Id查询医生信息和排班信息")
    @RequestMapping(value = "/listDoctor", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<DmsStaffResult> listDoctor(@RequestParam("staffId") Long staffId,@RequestParam Long patientId){

        return dmsAppService.listDoctor(staffId,patientId);

    }

    @ApiOperation(value = "根据医生ID查询医生信息")
    @GetMapping(value = "/selectStaffById")
    @ResponseBody
    public CommonResult<DmsStaffResult> selectStaffById(@RequestParam("id") Long id,@RequestParam("patientId") Long patientId){
        return dmsAppService.selectStaffById(id,patientId);
    }

    @ApiOperation(value = "查询该医生挂号账单")
    @PostMapping(value = "/listRegistrationBill")
    @ResponseBody
    public CommonResult<DmsDoctorBillsResult> listRegistrationBill(@RequestBody DmsDoctorBillsParam dmsDoctorBillsParam){
        return dmsAppService.listRegistrationBill(dmsDoctorBillsParam);
    }

    @ApiOperation(value = "医生名字模糊查询信息")
    @GetMapping(value = "/selectStaffByName")
    @ResponseBody
    public CommonResult<List<DmsStaffResult>> selectStaffByName(@RequestParam("name") String name){
        return dmsAppService.selectStaffByName(name);
    }

    @ApiOperation(value = "病人关注医生")
    @PostMapping(value = "/attentionStaff")
    @ResponseBody
    public CommonResult attentionStaff(@RequestBody DmsDoctorBillsParam dmsDoctorBillsParam){
        return dmsAppService.attentionStaff(dmsDoctorBillsParam);
    }

    @ApiOperation(value = "根据科室Id查询医生、科室、排班信息")
    @RequestMapping(value = "/listNowDoctorRegistration", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult <DmsNowDoctorRegistrationResults> listNowDoctorRegistration(@RequestParam("deptId")  Long deptId,
                                                                                       @RequestParam("thedate") String thedate){
        return dmsAppService.listNowDoctorRegistration(deptId,thedate);
    }

    @ApiOperation(value = "根据科室Id查询医生、科室、排班信息")
    @RequestMapping(value = "/listPatientConvention", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<BookingInformationResult>>listPatientConvention(@RequestParam("openID") String openID){
        return dmsAppService.listPatientConvention(openID);
    }

    @ApiOperation(value = "根据科室Id查询医生、科室、排班信息")
    @RequestMapping(value = "/listPatientConventionUpdate", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<BookingInformationResult>>listPatientConventionUpdate(@RequestParam("Id") Long Id){
        return dmsAppService.listPatientConventionUpdate(Id);
    }


    @ApiOperation(value = "查看信息")
    @RequestMapping(value = "/selectMaintenanceParam", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<List<InformationMaintenance>>selectMaintenanceParam(){
        return dmsAppService.selectMaintenanceParam();
    }

    @ApiOperation(value = "修改信息")
    @RequestMapping(value = "/updateMaintenanceParam", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<List<BookingInformationResult>>updateMaintenanceParam(@RequestBody InformationMaintenance informationMaintenance){
        return dmsAppService.updateMaintenanceParam(informationMaintenance);
    }

}
