package com.neu.his.cloud.zuul.distribution.api.pc.dms;


import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.neu.his.cloud.zuul.common.CommonResult;
import com.neu.his.cloud.zuul.dto.dms.DmsCaseHistoryParam;
import com.neu.his.cloud.zuul.dto.dms.DmsCaseHistoryResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "his-cloud-api-pc")
public interface ApiPcDmsCaseHistoryDistributionService {


    @RequestMapping(value = "/caseHistory/submitPriliminaryDise", method = RequestMethod.POST)
    CommonResult submitPriliminaryDise(@RequestBody DmsCaseHistoryParam dmsCaseHistoryParam);


    @RequestMapping(value = "/caseHistory/selectNotEndCaseHistoryByReg/{registrationId}", method = RequestMethod.GET)
    CommonResult<DmsCaseHistoryResult> selectNotEndCaseHistoryByReg(@PathVariable("registrationId") Long registrationId);

    //状态为2（确诊）病人的信息
    @RequestMapping(value = "/caseHistory/selectMiddleCaseHistoryByReg/{registrationId}", method = RequestMethod.GET)
    CommonResult<DmsCaseHistoryResult> selectMiddleCaseHistoryByReg(@PathVariable("registrationId") Long registrationId);

    @RequestMapping(value = "/caseHistory/selectEndCaseHistoryByReg/{registrationId}", method = RequestMethod.GET)
    CommonResult<DmsCaseHistoryResult> selectEndCaseHistoryByReg(@PathVariable("registrationId") Long registrationId);


    @RequestMapping(value = "/caseHistory/submitDefiniteDise", method = RequestMethod.POST)
    CommonResult submitDefiniteDise(DmsCaseHistoryParam dmsCaseHistoryParam);


    @RequestMapping(value = "/caseHistory/endDiagnosis", method = RequestMethod.POST)
    CommonResult endDiagnosis(DmsCaseHistoryParam dmsCaseHistoryParam);

    //诊中
    @RequestMapping(value = "/caseHistory/selectClinicalCaseHistoryByReg/{registrationId}", method = RequestMethod.GET)
    CommonResult<DmsCaseHistoryResult> selectClinicalCaseHistoryByReg(@PathVariable("registrationId") Long registrationId);

    @ApiOperation(value = "根据病人Id查询历史病历")
    @RequestMapping(value = "/caseHistory/queryCaseHistory", method = RequestMethod.GET)
    @ResponseBody
    CommonResult<DmsCaseHistoryResult> queryCaseHistory(@RequestParam("patientId") long patientId);

}
