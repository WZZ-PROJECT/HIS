package com.neu.his.cloud.service.pms.controller;

import com.neu.his.cloud.service.pms.common.CommonResult;
import com.neu.his.cloud.service.pms.dto.dms.DmsCaseHistoryResult;
import com.neu.his.cloud.service.pms.dto.pms.PmsDiagnosisPatientListResult;
import com.neu.his.cloud.service.pms.dto.pms.PmsPatientResult;
import com.neu.his.cloud.service.pms.service.PmsPatientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@Api(tags = "PmsDiagnosisPatientController", description = "诊断病人")
@RequestMapping("/diagnosisPatient")
@CrossOrigin(allowCredentials = "true")
public class PmsDiagnosisPatientController {
    @Autowired
    PmsPatientService pmsPatientService;

    //根据身份证号查询患者基本信息
    //1.调用PmsPatientService的selectPatientByIdNo
    @ApiOperation(value = "根据身份证号查询患者基本信息")
    @RequestMapping(value = "/selectPatientByIdNo", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<PmsPatientResult> selectPatientByIdNo(@RequestParam("identificationNo") String identificationNo){
        PmsPatientResult pmsPatientResult = pmsPatientService.selectPatientByIdNo(identificationNo);
        if (pmsPatientResult == null)
            return CommonResult.failed();
        return CommonResult.success(pmsPatientResult);
    }

    //根据openID查询患者基本信息
    //1.调用PmsPatientService的selectPatientByIdNo
    @ApiOperation(value = "根据openId查询患者基本信息")
    @RequestMapping(value = "/getPatient", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<PmsPatientResult> getPatient(@RequestParam("openId") String openId){
        PmsPatientResult pmsPatientResult = pmsPatientService.getPatient(openId);
        if (pmsPatientResult == null) {
            return CommonResult.failed();
        }
        return CommonResult.success(pmsPatientResult);
    }


    //根据医生id查询所有患者
    @ApiOperation(value = "根据医生id查询所有患者")
    @RequestMapping(value = "/refreshPatient", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<PmsDiagnosisPatientListResult> refreshPatient(@RequestParam("staffId") Long staffId){
        PmsDiagnosisPatientListResult pmsDiagnosisPatientListResult = pmsPatientService.refreshPatient(staffId);
        return CommonResult.success(pmsDiagnosisPatientListResult);
    }

    //医生添加患者
    @ApiOperation(value = "医生添加患者")
    @RequestMapping(value = "/bindPatient", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult bindPatient(@RequestParam("registrationId") Long registrationId, @RequestParam("staffId") Long staffId){
        int result;
        result = pmsPatientService.bindPatient(registrationId,staffId);
        if (result == 0){
            return CommonResult.failed();
        }
        else {
            return CommonResult.success(result);
        }
    }
    //医生开始诊断患者
    @ApiOperation(value = "医生开始诊断患者")
    @RequestMapping(value = "/startDiagnosis", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<DmsCaseHistoryResult> startDiagnosis(@RequestParam("registrationId") Long registrationId){
        DmsCaseHistoryResult dmsCaseHistoryResult = pmsPatientService.startDiagnosis(registrationId);
        return CommonResult.success(dmsCaseHistoryResult);
    }
}
