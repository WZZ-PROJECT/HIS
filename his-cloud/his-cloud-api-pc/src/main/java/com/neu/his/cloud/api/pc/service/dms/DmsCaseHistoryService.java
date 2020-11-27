package com.neu.his.cloud.api.pc.service.dms;


import com.neu.his.cloud.api.pc.common.CommonResult;
import com.neu.his.cloud.api.pc.dto.dms.DmsCaseHistoryParam;
import com.neu.his.cloud.api.pc.dto.dms.DmsCaseHistoryResult;
import com.neu.his.cloud.api.pc.dto.sms.AddInformParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "his-cloud-service-dms")
public interface DmsCaseHistoryService {

    @RequestMapping(value = "/caseHistory/submitPriliminaryDise", method = RequestMethod.POST)
    CommonResult submitPriliminaryDise(@RequestBody DmsCaseHistoryParam dmsCaseHistoryParam);

    @RequestMapping(value = "/caseHistory/selectNotEndCaseHistoryByReg/{registrationId}", method = RequestMethod.GET)
    CommonResult<DmsCaseHistoryResult> selectNotEndCaseHistoryByReg(@PathVariable("registrationId") Long registrationId);


    @RequestMapping(value = "/caseHistory/selectEndCaseHistoryByReg/{registrationId}", method = RequestMethod.GET)
    CommonResult<DmsCaseHistoryResult> selectEndCaseHistoryByReg(@PathVariable("registrationId") Long registrationId);

    /**
     * 描述：根据挂号id查询已结束就诊的历史病历（病历首页显示各种串）
     * <p>author:赵煜
     */
    @ApiOperation(value = "根据挂号id查询已结束就诊的历史病历（病历首页显示各种串）")
    @RequestMapping(value = "/caseHistory/selectEndCaseHistory/{registrationId}", method = RequestMethod.GET)
    @ResponseBody
    CommonResult<DmsCaseHistoryResult> selectEndCaseHistory(@PathVariable("registrationId") Long registrationId,@RequestParam(value = "status",required =
            false)Integer status);


    //查看状态为2的病人
    @RequestMapping(value = "/caseHistory/selectMiddleCaseHistoryByReg/{registrationId}", method = RequestMethod.GET)
    CommonResult<DmsCaseHistoryResult> selectMiddleCaseHistoryByReg(@PathVariable("registrationId") Long registrationId);

    @RequestMapping(value = "/caseHistory/submitDefiniteDise", method = RequestMethod.POST)
    CommonResult submitDefiniteDise(@RequestBody DmsCaseHistoryParam dmsCaseHistoryParam);

    @RequestMapping(value = "/caseHistory/endDiagnosis", method = RequestMethod.POST)
    CommonResult endDiagnosis(@RequestBody DmsCaseHistoryParam dmsCaseHistoryParam);

    //诊中
    @RequestMapping(value = "/caseHistory/selectClinicalCaseHistoryByReg/{registrationId}", method = RequestMethod.GET)
    CommonResult<DmsCaseHistoryResult> selectClinicalCaseHistoryByReg(@PathVariable("registrationId") Long registrationId);

    @RequestMapping(value = "/caseHistory/queryCaseHistory", method = RequestMethod.GET)
    CommonResult<DmsCaseHistoryResult> queryCaseHistory(@RequestParam("patientId") long patientId);


    //知情告知增加
    @RequestMapping(value = "/caseHistory/insertFamiliarInform", method = RequestMethod.POST)
    CommonResult insertFamiliarInform(@RequestBody AddInformParam addInformParam);

    //知情告知删除
    @RequestMapping(value = "/caseHistory/deleteFamiliarInform", method = RequestMethod.POST)
    CommonResult deleteFamiliarInform(@RequestBody AddInformParam addInformParam);

    //知情告知修改
    @RequestMapping(value = "/caseHistory/updateFamiliarInform", method = RequestMethod.POST)
    CommonResult updateFamiliarInform(@RequestBody AddInformParam addInformParam);

    //知情告知查询
    @RequestMapping(value = "/caseHistory/selectFamiliarInform", method = RequestMethod.POST)
    CommonResult selectFamiliarInform(@RequestBody AddInformParam addInformParam);

}
