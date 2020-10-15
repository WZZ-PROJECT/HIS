package com.neu.his.cloud.zuul.distribution.api.pc.sms;


import com.neu.his.cloud.zuul.common.CommonPage;
import com.neu.his.cloud.zuul.common.CommonResult;
import com.neu.his.cloud.zuul.dto.dms.DmsNonDrugResult;
import com.neu.his.cloud.zuul.dto.sms.SmsSetmealResult;
import com.neu.his.cloud.zuul.dto.sms.SmsSetmealRuleResult;
import com.neu.his.cloud.zuul.dto.sms.SmsSetmealSkdRuleParam;
import io.swagger.annotations.ApiOperation;
import com.neu.his.cloud.zuul.dto.sms.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import java.util.List;

@FeignClient(value = "his-cloud-api-pc")
public interface ApiPcSmsSetmealDistributionService {

    @RequestMapping(value = "/setmeal/selectSetmeal", method = RequestMethod.POST)
    CommonResult<CommonPage<SmsSetmealResult>> selectSetmeal(@RequestBody DmsNonDrugResult dmsNonDrugResult,
                                                             @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                             @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum);

    @RequestMapping(value = "/setmeal/SetmealListRule", method = RequestMethod.POST)
    CommonResult<CommonPage<SmsSetmealRuleResult>> SetmealListRule(@RequestParam("deptId")  Long deptId,
                                                                   @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                                   @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum);

    @RequestMapping(value = "/setmeal/deleteSetmealRule", method = RequestMethod.POST)
    CommonResult deleteSetmealRule(@RequestParam("ids") List<Long> ids);

    @RequestMapping(value = "/setmeal/listSetmealSkd", method = RequestMethod.POST)
    CommonResult<CommonPage<SmsSetMealSkdResult>> listSetmealSkd(@RequestBody SmsSetmealSkdParam queryParam);

    /**
     * 描述:创建医疗套餐排班规则
     * <p>author: ma
     */
    @ApiOperation("新增医疗套餐排班规则")
    @RequestMapping(value = "/setmeal/createSetmealRule", method = RequestMethod.POST)
    @ResponseBody
    CommonResult createSetmealRule(@RequestBody SmsSetmealSkdRuleParam param);

    /**
     * 描述:根据多条排班规则生成医疗套餐skd
     * <p>author: ma
     */
    @ApiOperation("根据多条排班规则生成医疗套餐skd")
    @RequestMapping(value = "/setmeal/generateSetMealSkd", method = RequestMethod.POST)
    @ResponseBody
    CommonResult generateSetMealSkd(@RequestParam("ruleIds") List<Long> ruleIds,
                                           @RequestParam("startDate") String startDate,
                                           @RequestParam("endDate")String endDate);

    /**
     * 描述:根据规则id查询一条规则详情
     * <p>author: ma
     */
    @ApiOperation("根据规则id查询一条规则详情")
    @RequestMapping(value = "/setmeal/getSetmealRuleDetail", method = RequestMethod.POST)
    @ResponseBody
    CommonResult<SmsSetmealRuleResult> getSetmealRuleDetail(@RequestParam("ruleId") Long ruleId);

    @ApiOperation("更新排班规则")
    @RequestMapping(value = "/setmeal/updateSetmealRule/{id}", method = RequestMethod.POST)
    @ResponseBody
    CommonResult updateSetmealRule(@PathVariable Long id, @RequestBody SmsSetmealSkdRuleParam param);
}
