package com.neu.his.cloud.zuul.controller.pms;



import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.neu.his.cloud.zuul.common.CommonResult;
import com.neu.his.cloud.zuul.distribution.api.pc.pms.ApiPcPmsDiagnosisPatientDistributionService;
import com.neu.his.cloud.zuul.dto.dms.DmsCaseHistoryResult;
import com.neu.his.cloud.zuul.dto.pms.PmsDiagnosisPatientListResult;
import com.neu.his.cloud.zuul.dto.pms.PmsDiagnosisPatientResult;
import com.neu.his.cloud.zuul.dto.pms.PmsPatientResult;
import com.neu.his.cloud.zuul.dto.pms.PmsQueryPeople;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "PmsDiagnosisPatientDistributionController", description = "诊断病人管理")
@RequestMapping("/diagnosisPatient")
@CrossOrigin(allowCredentials = "true")
public class PmsDiagnosisPatientDistributionController {
    @Autowired
    ApiPcPmsDiagnosisPatientDistributionService apiPcPmsDiagnosisPatientDistributionService;

    @HystrixCommand(fallbackMethod = "selectPatientByIdNoFallbackInfo")
    @ApiOperation(value = "根据身份证号查询患者基本信息")
    @RequestMapping(value = "/selectPatientByIdNo", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<PmsPatientResult> selectPatientByIdNo(@RequestParam("identificationNo") String identificationNo){
        return  apiPcPmsDiagnosisPatientDistributionService.selectPatientByIdNo(identificationNo);
    }
    private CommonResult<PmsPatientResult> selectPatientByIdNoFallbackInfo(String identificationNo){
        return CommonResult.success(null,"请检查您的网络") ;
    }

    @HystrixCommand(fallbackMethod = "refreshPatientFallbackInfo")
    @ApiOperation(value = "根据医生id查询所有患者")
    @RequestMapping(value = "/refreshPatient", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<PmsDiagnosisPatientListResult> refreshPatient(@RequestParam("staffId") Long staffId){
       return apiPcPmsDiagnosisPatientDistributionService.refreshPatient(staffId);
    }
    private CommonResult<PmsDiagnosisPatientListResult> refreshPatientFallbackInfo(Long staffId){
        return CommonResult.success(null,"请检查您的网络") ;
    }

    @HystrixCommand(fallbackMethod = "bindPatientFallbackInfo")
    @ApiOperation(value = "医生添加患者")
    @RequestMapping(value = "/bindPatient", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult bindPatient(@RequestParam("registrationId") Long registrationId, @RequestParam("staffId") Long staffId){
         return apiPcPmsDiagnosisPatientDistributionService.bindPatient(registrationId,staffId);
    }
    private CommonResult bindPatientFallbackInfo(Long registrationId, Long staffId){
        return CommonResult.success(null,"请检查您的网络") ;
    }

    @HystrixCommand(fallbackMethod = "startDiagnosisFallbackInfo")
    @ApiOperation(value = "医生开始诊断患者")
    @RequestMapping(value = "/startDiagnosis", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<DmsCaseHistoryResult> startDiagnosis(@RequestParam("registrationId") Long registrationId){
       return apiPcPmsDiagnosisPatientDistributionService.startDiagnosis(registrationId);
    }
    private CommonResult<DmsCaseHistoryResult> startDiagnosisFallbackInfo(Long registrationId){
        return CommonResult.success(null,"请检查您的网络") ;
    }

    //根据openID查询患者基本信息
    //1.调用PmsPatientService的selectPatientByIdNo
    @HystrixCommand(fallbackMethod = "getPatientFallbackInfo")
    @ApiOperation(value = "根据openId查询患者基本信息")
    @RequestMapping(value = "/getPatient", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<PmsPatientResult> getPatient(@RequestParam("openId") String openId){
        return apiPcPmsDiagnosisPatientDistributionService.getPatient(openId);
    }
    public CommonResult<PmsPatientResult> getPatientFallbackInfo(@RequestParam("openId") String openId){
        return CommonResult.success(null,"请检查您的网络") ;
    }

    @HystrixCommand(fallbackMethod = "queryPeopleFallbackInfo")
    @ApiOperation("根据条件查询病人")
    @RequestMapping(value = "/queryPeople", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<PmsDiagnosisPatientListResult> queryPeople(@RequestBody PmsQueryPeople pmsQueryPeople){
        return apiPcPmsDiagnosisPatientDistributionService.queryPeople(pmsQueryPeople);
    }
    public CommonResult<PmsDiagnosisPatientListResult> queryPeopleFallbackInfo(PmsQueryPeople pmsQueryPeople){
        return CommonResult.success(null,"请检查您的网络");
    }

    @HystrixCommand(fallbackMethod = "selectPeopleByRegistrationIdFallbackInfo")
    @ApiOperation("查询病人信息用于查看病例信息")
    @RequestMapping(value = "/selectPeopleByRegistrationId", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<PmsDiagnosisPatientResult> selectPeopleByRegistrationId(@RequestParam("registrationId") Long registrationId){
        return apiPcPmsDiagnosisPatientDistributionService.selectPeopleByRegistrationId(registrationId);
    }
    public CommonResult<PmsDiagnosisPatientResult> selectPeopleByRegistrationIdFallbackInfo(Long registrationId){
        return CommonResult.success(null,"请检查您的网络");
    }

}
