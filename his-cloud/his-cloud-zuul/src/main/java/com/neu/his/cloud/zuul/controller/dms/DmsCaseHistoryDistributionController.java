package com.neu.his.cloud.zuul.controller.dms;


import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.neu.his.cloud.zuul.common.CommonResult;
import com.neu.his.cloud.zuul.distribution.api.pc.dms.ApiPcDmsCaseHistoryDistributionService;
import com.neu.his.cloud.zuul.dto.dms.DmsCaseHistoryParam;
import com.neu.his.cloud.zuul.dto.dms.DmsCaseHistoryResult;
import com.neu.his.cloud.zuul.dto.sms.AddInformParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "DmsCaseHistoryDistributionController", description = "病历管理")
@RequestMapping("/caseHistory")
@CrossOrigin(allowCredentials = "true")
public class DmsCaseHistoryDistributionController {
    @Autowired
    ApiPcDmsCaseHistoryDistributionService apiPcDmsCaseHistoryDistributionService;

    @HystrixCommand(fallbackMethod = "submitPriliminaryDiseFallbackInfo")
    @ApiOperation(value = "提交初诊信息")
    @RequestMapping(value = "/submitPriliminaryDise", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult submitPriliminaryDise(@RequestBody DmsCaseHistoryParam dmsCaseHistoryParam){
        return apiPcDmsCaseHistoryDistributionService.submitPriliminaryDise(dmsCaseHistoryParam);
    }

    private CommonResult submitPriliminaryDiseFallbackInfo(DmsCaseHistoryParam dmsCaseHistoryParam){
        return CommonResult.success(null,"请检查您的网络") ;
    }

    @HystrixCommand(fallbackMethod = "selectNotEndCaseHistoryByRegFallbackInfo")
    @ApiOperation(value = "根据挂号Id查询未结束就诊的历史病历（只含有初诊信息）")
    @RequestMapping(value = "/selectNotEndCaseHistoryByReg/{registrationId}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<DmsCaseHistoryResult> selectNotEndCaseHistoryByReg(@PathVariable("registrationId") Long registrationId){
        return apiPcDmsCaseHistoryDistributionService.selectNotEndCaseHistoryByReg(registrationId);
    }
    private CommonResult<DmsCaseHistoryResult> selectNotEndCaseHistoryByRegFallbackInfo(Long registrationId){
        return CommonResult.success(null,"请检查您的网络") ;
    }

    @HystrixCommand(fallbackMethod = "selectMiddleCaseHistoryByRegFallbackInfo")
    @ApiOperation(value = "根据挂号Id查询未结束就诊的历史病历（只含有初诊信息）")
    @RequestMapping(value = "/selectMiddleCaseHistoryByReg/{registrationId}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<DmsCaseHistoryResult> selectMiddleCaseHistoryByReg(@PathVariable("registrationId") Long registrationId){
        return apiPcDmsCaseHistoryDistributionService.selectMiddleCaseHistoryByReg(registrationId);
    }
    private CommonResult<DmsCaseHistoryResult> selectMiddleCaseHistoryByRegFallbackInfo(Long registrationId){
        return CommonResult.success(null,"请检查您的网络") ;
    }

    @HystrixCommand(fallbackMethod = "selectEndCaseHistoryByRegFallbackInfo")
    @ApiOperation(value = "根据挂号id查询已结束就诊的历史病历（病历首页显示各种串）")
    @RequestMapping(value = "/selectEndCaseHistoryByReg/{registrationId}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<DmsCaseHistoryResult> selectEndCaseHistoryByReg(@PathVariable("registrationId") Long registrationId){

        return apiPcDmsCaseHistoryDistributionService.selectEndCaseHistoryByReg(registrationId);
    }
    private CommonResult<DmsCaseHistoryResult> selectEndCaseHistoryByRegFallbackInfo(Long registrationId){
        return CommonResult.success(null,"请检查您的网络") ;
    }

    @HystrixCommand(fallbackMethod = "submitDefiniteDiseFallbackInfo")
    @ApiOperation(value = "确诊")
    @RequestMapping(value = "/submitDefiniteDise", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult submitDefiniteDise(@RequestBody DmsCaseHistoryParam dmsCaseHistoryParam){

        return  apiPcDmsCaseHistoryDistributionService.submitDefiniteDise(dmsCaseHistoryParam);
    }
    private CommonResult submitDefiniteDiseFallbackInfo(DmsCaseHistoryParam dmsCaseHistoryParam){
        return CommonResult.success(null,"请检查您的网络") ;
    }


    @HystrixCommand(fallbackMethod = "endDiagnosisFallbackInfo")
    @ApiOperation(value = "诊毕")
    @RequestMapping(value = "/endDiagnosis", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult endDiagnosis(DmsCaseHistoryParam dmsCaseHistoryParam){
        return apiPcDmsCaseHistoryDistributionService.endDiagnosis(dmsCaseHistoryParam);
    }
    private CommonResult endDiagnosisFallbackInfo(DmsCaseHistoryParam dmsCaseHistoryParam){
        return CommonResult.success(null,"请检查您的网络") ;
    }

    @HystrixCommand(fallbackMethod = "selectClinicalCaseHistoryByRegFallbackInfo")
    @ApiOperation(value = "根据挂号Id查询未结束就诊的历史病历（只含有初诊信息）")
    @RequestMapping(value = "/selectClinicalCaseHistoryByReg/{registrationId}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<DmsCaseHistoryResult> selectClinicalCaseHistoryByReg(@PathVariable("registrationId") Long registrationId){
        return apiPcDmsCaseHistoryDistributionService.selectClinicalCaseHistoryByReg(registrationId);
    }
    private CommonResult<DmsCaseHistoryResult> selectClinicalCaseHistoryByRegFallbackInfo(Long registrationId){
        return CommonResult.success(null,"请检查您的网络") ;
    }

    @HystrixCommand(fallbackMethod = "queryCaseHistoryFallbackInfo")
    @ApiOperation(value = "根据病人Id查询历史病历")
    @RequestMapping(value = "/queryCaseHistory", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<DmsCaseHistoryResult> queryCaseHistory(@RequestParam("patientId") long patientId){
        return apiPcDmsCaseHistoryDistributionService.queryCaseHistory(patientId);
    }
    private CommonResult<DmsCaseHistoryResult> queryCaseHistoryFallbackInfo(long patientId){
        return CommonResult.success(null,"请检查您的网络") ;
    }

    @HystrixCommand(fallbackMethod = "insertFamiliarInformFallbackInfo")
    @ApiOperation(value = "知情告知增加")
    @RequestMapping(value = "/insertFamiliarInform", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult insertFamiliarInform(@RequestBody AddInformParam addInformParam){
        return apiPcDmsCaseHistoryDistributionService.insertFamiliarInform(addInformParam);
    }
    private CommonResult insertFamiliarInformFallbackInfo(AddInformParam addInformParam){
        return CommonResult.success(null,"请检查您的网络") ;
    }

    @HystrixCommand(fallbackMethod = "deleteFamiliarInformFallbackInfo")
    @ApiOperation(value = "知情告知删除")
    @RequestMapping(value = "/deleteFamiliarInform", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult deleteFamiliarInform(@RequestBody AddInformParam addInformParam){
        return apiPcDmsCaseHistoryDistributionService.deleteFamiliarInform(addInformParam);
    }
    private CommonResult deleteFamiliarInformFallbackInfo(AddInformParam addInformParam){
        return CommonResult.success(null,"请检查您的网络") ;
    }

    @HystrixCommand(fallbackMethod = "updateFamiliarInformFallbackInfo")
    @ApiOperation(value = "知情告知修改")
    @RequestMapping(value = "/updateFamiliarInform", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateFamiliarInform(@RequestBody AddInformParam addInformParam){
        return apiPcDmsCaseHistoryDistributionService.updateFamiliarInform(addInformParam);
    }
    private CommonResult updateFamiliarInformFallbackInfo(AddInformParam addInformParam){
        return CommonResult.success(null,"请检查您的网络") ;
    }

    @HystrixCommand(fallbackMethod = "selectFamiliarInformFallbackInfo")
    @ApiOperation(value = "知情告知查询")
    @RequestMapping(value = "/selectFamiliarInform", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult selectFamiliarInform(@RequestBody AddInformParam addInformParam){
        return apiPcDmsCaseHistoryDistributionService.selectFamiliarInform(addInformParam);
    }
    private CommonResult selectFamiliarInformFallbackInfo(AddInformParam addInformParam){
        return CommonResult.success(null,"请检查您的网络") ;
    }

    /**
     * 描述：根据挂号id查询已结束就诊的历史病历（病历首页显示各种串）
     * <p>author:赵煜
     */
    @HystrixCommand(fallbackMethod = "selectEndCaseHistoryFallbackInfo")
    @ApiOperation(value = "根据挂号id查询已结束就诊的历史病历（病历首页显示各种串）")
    @RequestMapping(value = "/selectEndCaseHistory/{registrationId}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<DmsCaseHistoryResult> selectEndCaseHistory(@PathVariable("registrationId") Long registrationId,@RequestParam(value = "status",required =
            false)Integer status){
        CommonResult<DmsCaseHistoryResult> dmsCaseHistoryResultCommonResult = apiPcDmsCaseHistoryDistributionService.selectEndCaseHistory(registrationId, status);
        return dmsCaseHistoryResultCommonResult;
    }
    private CommonResult<DmsCaseHistoryResult> selectEndCaseHistoryFallbackInfo(Long registrationId,Integer status){
        return CommonResult.success(null,"请检查您的网络") ;
    }

}
