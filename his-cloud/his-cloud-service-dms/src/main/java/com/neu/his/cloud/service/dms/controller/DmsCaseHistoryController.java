package com.neu.his.cloud.service.dms.controller;

import com.neu.his.cloud.service.dms.common.CommonResult;
import com.neu.his.cloud.service.dms.dto.dms.DmsCaseHistoryParam;
import com.neu.his.cloud.service.dms.dto.dms.DmsCaseHistoryResult;
import com.neu.his.cloud.service.dms.service.DmsCaseHistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Api(tags = "DmsCaseHistoryController", description = "诊断历史管理")
@RequestMapping("/caseHistory")
@CrossOrigin(allowCredentials = "true")
public class DmsCaseHistoryController {
    @Autowired
    DmsCaseHistoryService dmsCaseHistoryService;

    //1.调用DmsCaseHistoryService的insertPriliminaryDise
    @ApiOperation(value = "提交初诊信息")
    @RequestMapping(value = "/submitPriliminaryDise", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult submitPriliminaryDise(@RequestBody DmsCaseHistoryParam dmsCaseHistoryParam){
        int returnResult = dmsCaseHistoryService.insertPriliminaryDise(dmsCaseHistoryParam);
        if (returnResult == 1){
            return CommonResult.success(returnResult);
        }
        else{
            return CommonResult.failed();
        }
    }
    //根据挂号id查询历史病历
    //1.调用DmsCaseHistoryService的selectCaseHistoryByReg
    @ApiOperation(value = "根据挂号id查询未结束就诊的历史病历（只含有初诊信息）")
    @RequestMapping(value = "/selectNotEndCaseHistoryByReg/{registrationId}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<DmsCaseHistoryResult> selectNotEndCaseHistoryByReg(@PathVariable("registrationId") Long registrationId){
        DmsCaseHistoryResult dmsCaseHistoryResult = new DmsCaseHistoryResult();
        dmsCaseHistoryResult = dmsCaseHistoryService.selectCaseHistoryByReg(registrationId,1);
        return CommonResult.success(dmsCaseHistoryResult);
    }


    //根据挂号id查询历史病历(状态为2的病人)
    //2.调用DmsCaseHistoryService的selectMiddleCaseHistoryByReg
    @ApiOperation(value = "根据挂号id查询未结束就诊的历史病历（只含有确诊信息）")
    @RequestMapping(value = "/selectMiddleCaseHistoryByReg/{registrationId}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<DmsCaseHistoryResult> selectMiddleCaseHistoryByReg(@PathVariable("registrationId") Long registrationId){
        DmsCaseHistoryResult dmsCaseHistoryResult = new DmsCaseHistoryResult();
        dmsCaseHistoryResult = dmsCaseHistoryService.selectCaseHistoryByReg(registrationId,2);
        return CommonResult.success(dmsCaseHistoryResult);
    }



    /**
     * 描述：根据挂号id查询已结束就诊的历史病历（病历首页显示各种串）
     * <p>author:赵煜
     */
    @ApiOperation(value = "根据挂号id查询已结束就诊的历史病历（病历首页显示各种串）")
    @RequestMapping(value = "/selectEndCaseHistoryByReg/{registrationId}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<DmsCaseHistoryResult> selectEndCaseHistoryByReg(@PathVariable("registrationId") Long registrationId){
        DmsCaseHistoryResult dmsCaseHistoryResult = new DmsCaseHistoryResult();
        dmsCaseHistoryResult = dmsCaseHistoryService.selectCaseHistoryByReg(registrationId,3);
        return CommonResult.success(dmsCaseHistoryResult);
    }


    //门诊确诊
    //1.调用DmsCaseHistoryService的submitDefiniteDise
    @ApiOperation(value = "门诊确诊")
    @RequestMapping(value = "/submitDefiniteDise", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult submitDefiniteDise(@RequestBody DmsCaseHistoryParam dmsCaseHistoryParam){
        int returnResult = dmsCaseHistoryService.submitDefiniteDise(dmsCaseHistoryParam);
        if (returnResult == 1){
            return CommonResult.success(returnResult);
        }
        else{
            return CommonResult.failed();
        }
    }
    //诊毕
    //1.调用DmsCaseHistoryService的endDiagnosis
    @ApiOperation(value = "诊毕")
    @RequestMapping(value = "/endDiagnosis", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult endDiagnosis(@RequestBody DmsCaseHistoryParam dmsCaseHistoryParam){
        int returnResult = dmsCaseHistoryService.endDiagnosis(dmsCaseHistoryParam);
        if (returnResult == 1){
            return CommonResult.success(returnResult);
        }
        else{
            return CommonResult.failed();
        }
    }

    /**
     * 描述：根据挂号id查询已结束就诊的历史病历（诊中）
     * <p>author:赵煜
     */
    @ApiOperation(value = "根据挂号id查询已结束就诊的历史病历（诊中）")
    @RequestMapping(value = "/selectClinicalCaseHistoryByReg/{registrationId}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<DmsCaseHistoryResult> selectClinicalCaseHistoryByReg(@PathVariable("registrationId") Long registrationId){
        DmsCaseHistoryResult dmsCaseHistoryResult = new DmsCaseHistoryResult();
        /*selectClinicalCaseHistoryByReg*/
        Integer integer = dmsCaseHistoryService.selectClinicalCaseHistoryByReg(registrationId);
        dmsCaseHistoryResult = dmsCaseHistoryService.selectCaseHistoryByReg(registrationId,integer);
        return CommonResult.success(dmsCaseHistoryResult);
    }

    //诊毕
    //1.调用DmsCaseHistoryService的endDiagnosis
    @ApiOperation(value = "根据病人Id查询历史病历")
    @RequestMapping(value = "/queryCaseHistory", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<DmsCaseHistoryResult> queryCaseHistory(@RequestParam("patientId") long patientId){
        DmsCaseHistoryResult returnResult = dmsCaseHistoryService.queryCaseHistory(patientId);
        return CommonResult.success(returnResult);
    }

}
