package com.neu.his.cloud.api.pc.controller.dms;

import com.neu.his.cloud.api.pc.common.CommonResult;
import com.neu.his.cloud.api.pc.dto.dms.DmsCaseHistoryParam;
import com.neu.his.cloud.api.pc.dto.dms.DmsCaseHistoryResult;
import com.neu.his.cloud.api.pc.service.dms.DmsCaseHistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "DmsCaseHistoryController", description = "病历管理")
@RequestMapping("/caseHistory")
@CrossOrigin(allowCredentials = "true")
public class DmsCaseHistoryController {
    @Autowired
    DmsCaseHistoryService dmsCaseHistoryService;


    @ApiOperation(value = "提交初诊信息")
    @RequestMapping(value = "/submitPriliminaryDise", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult submitPriliminaryDise(@RequestBody DmsCaseHistoryParam dmsCaseHistoryParam){
       return dmsCaseHistoryService.submitPriliminaryDise(dmsCaseHistoryParam);

    }


    @ApiOperation(value = "根据挂号Id查询未结束就诊的历史病历（只含有初诊信息）")
    @RequestMapping(value = "/selectNotEndCaseHistoryByReg/{registrationId}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<DmsCaseHistoryResult> selectNotEndCaseHistoryByReg(@PathVariable("registrationId") Long registrationId){
        return dmsCaseHistoryService.selectNotEndCaseHistoryByReg(registrationId);
    }

    @ApiOperation(value = "根据挂号Id查询未结束就诊的历史病历（只含有确诊信息）")
    @RequestMapping(value = "/selectMiddleCaseHistoryByReg/{registrationId}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<DmsCaseHistoryResult> selectMiddleCaseHistoryByReg(@PathVariable("registrationId") Long registrationId){
        return dmsCaseHistoryService.selectMiddleCaseHistoryByReg(registrationId);
    }

    @ApiOperation(value = "根据挂号id查询已结束就诊的历史病历（病历首页显示各种串）")
    @RequestMapping(value = "/selectEndCaseHistoryByReg/{registrationId}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<DmsCaseHistoryResult> selectEndCaseHistoryByReg(@PathVariable("registrationId") Long registrationId){

        return dmsCaseHistoryService.selectEndCaseHistoryByReg(registrationId);

    }


    @ApiOperation(value = "确诊")
    @RequestMapping(value = "/submitDefiniteDise", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult submitDefiniteDise(@RequestBody DmsCaseHistoryParam dmsCaseHistoryParam){
       return  dmsCaseHistoryService.submitDefiniteDise(dmsCaseHistoryParam);
    }

    @ApiOperation(value = "诊毕")
    @RequestMapping(value = "/endDiagnosis", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult endDiagnosis(@RequestBody DmsCaseHistoryParam dmsCaseHistoryParam){
        return dmsCaseHistoryService.endDiagnosis(dmsCaseHistoryParam);

    }

    @ApiOperation(value = "根据挂号Id查询未结束就诊的历史病历（诊中）")
    @RequestMapping(value = "/selectClinicalCaseHistoryByReg/{registrationId}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<DmsCaseHistoryResult> selectClinicalCaseHistoryByReg(@PathVariable("registrationId") Long registrationId){
        return dmsCaseHistoryService.selectClinicalCaseHistoryByReg(registrationId);
    }

    @ApiOperation(value = "根据病人Id查询历史病历")
    @RequestMapping(value = "/queryCaseHistory", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<DmsCaseHistoryResult> queryCaseHistory(@RequestParam("patientId") long patientId){
        return dmsCaseHistoryService.queryCaseHistory(patientId);
    }

}
