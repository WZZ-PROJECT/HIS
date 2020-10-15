package com.neu.his.cloud.zuul.controller.sms;


import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.neu.his.cloud.zuul.common.CommonPage;
import com.neu.his.cloud.zuul.common.CommonResult;
import com.neu.his.cloud.zuul.distribution.api.pc.sms.ApiPcSmsSetmealDistributionService;
import com.neu.his.cloud.zuul.dto.dms.DmsNonDrugResult;
import com.neu.his.cloud.zuul.dto.sms.*;
import com.neu.his.cloud.zuul.dto.sms.SmsSetmealResult;
import com.neu.his.cloud.zuul.dto.sms.SmsSetmealRuleResult;
import com.neu.his.cloud.zuul.dto.sms.SmsSetmealSkdRuleParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Date;
import java.util.List;

/**
 * wzongzheng
 */
@RestController
@Api(tags = "SmsSetmealController", description = "医疗排班管理")
@RequestMapping("/setmeal")
@CrossOrigin(allowCredentials = "true")
public class SmsSetmealDistributionController {

    @Resource
    ApiPcSmsSetmealDistributionService smsSetmealService;




    /**
     * 描述:查询所有医疗套餐
     * <p>author: ma
     */
    @HystrixCommand(fallbackMethod = "selectSetmealFallbackInfo")
    @ApiOperation("查询所有医疗套餐")
    @RequestMapping(value = "/selectSetmeal", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<CommonPage<SmsSetmealResult>> selectSetmeal(@RequestBody DmsNonDrugResult dmsNonDrugResult,
                                                                    @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                                    @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum){
        return smsSetmealService.selectSetmeal(dmsNonDrugResult,pageSize,pageNum);
    }

    private CommonResult<CommonPage<SmsSetmealResult>> selectSetmealFallbackInfo(DmsNonDrugResult dmsNonDrugResult, Integer pageSize, Integer pageNum){
        return CommonResult.success(null,"请检查您的网络") ;
    }

    /**
     * 描述:根据deptId查询医疗套餐
     *
     */

    @HystrixCommand(fallbackMethod = "SetmealListRuleFallbackInfo")
    @ApiOperation("根据部门id筛选排版规则基本信息、分页")
    @RequestMapping(value = "/SetmealListRule", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<CommonPage<SmsSetmealRuleResult>> SetmealListRule(@RequestParam("deptId")  Long deptId,
                                                               @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                               @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum){

        return smsSetmealService.SetmealListRule(deptId,pageSize,pageNum);
    }
    private CommonResult<CommonPage<SmsSetmealRuleResult>> SetmealListRuleFallbackInfo( Long deptId, Integer pageSize, Integer pageNum){
        return CommonResult.success(null,"请检查您的网络") ;
    }

    /**
     * 描述:根据List<Long> ids删除医疗套餐
     *
     */
    @HystrixCommand(fallbackMethod = "deleteSetmealRuleFallbackInfo")
    @ApiOperation("删除排班规则")
    @RequestMapping(value = "/deleteSetmealRule", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult deleteSetmealRule(@RequestParam("ids") List<Long> ids){
        return smsSetmealService.deleteSetmealRule(ids);
    }
    private CommonResult deleteSetmealRuleFallbackInfo(List<Long> ids){
        return CommonResult.success(null,"请检查您的网络") ;
    }

    @HystrixCommand(fallbackMethod = "listSetmealSkdFallbackInfo")
    @ApiOperation("查找排班记录、分页")
    @RequestMapping(value = "/listSetmealSkd", method = RequestMethod.POST)
    @ResponseBody
    public  CommonResult<CommonPage<SmsSetMealSkdResult>> listSetmealSkd(@RequestBody SmsSetmealSkdParam queryParam){

        return smsSetmealService.listSetmealSkd(queryParam);
    }
    private  CommonResult<CommonPage<SmsSetMealSkdResult>> listSetmealSkdFallbackInfo(SmsSetmealSkdParam queryParam){
        return CommonResult.success(null,"请检查您的网络") ;
    }
    /**
     * 描述:创建医疗套餐排班规则
     * <p>author: ma
     */
    @HystrixCommand(fallbackMethod = "createSetmealRuleFallbackInfo")
    @ApiOperation("新增医疗套餐排班规则")
    @RequestMapping(value = "/createSetmealRule", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult createSetmealRule(@RequestBody SmsSetmealSkdRuleParam param, BindingResult result){
        return smsSetmealService.createSetmealRule(param);
    }
    private CommonResult createSetmealRuleFallbackInfo(SmsSetmealSkdRuleParam param, BindingResult result){
        return CommonResult.success(null,"请检查您的网络") ;
    }

    /**
     * 描述:根据多条排班规则生成医疗套餐skd
     * <p>author: ma
     */
    @HystrixCommand(fallbackMethod = "generateSetMealSkdFallbackInfo")
    @ApiOperation("根据多条排班规则生成医疗套餐skd")
    @RequestMapping(value = "/generateSetMealSkd", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult generateSetMealSkd(@RequestParam("ruleIds") List<Long> ruleIds,
                                           @RequestParam("startDate") String startDate,
                                           @RequestParam("endDate") String endDate){
        return smsSetmealService.generateSetMealSkd(ruleIds,startDate,endDate);
    }

    public CommonResult generateSetMealSkdFallbackInfo(List<Long> ruleIds, String startDate, String endDate){
        return CommonResult.success(null,"请检查您的网络") ;
    }

    /**
     * 描述:根据规则id查询一条规则详情
     * <p>author: ma
     */
    @HystrixCommand(fallbackMethod = "getSetmealRuleDetailFallbackInfo")
    @ApiOperation("根据规则id查询一条规则详情")
    @RequestMapping(value = "/getSetmealRuleDetail", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<SmsSetmealRuleResult> getSetmealRuleDetail(@RequestParam("ruleId") Long ruleId){
        return smsSetmealService.getSetmealRuleDetail(ruleId);
    }
    public CommonResult<SmsSetmealRuleResult> getSetmealRuleDetailFallbackInfo(Long ruleId){
        return smsSetmealService.getSetmealRuleDetail(ruleId);
    }

    /**
     * 更新排班规则
     * @param id
     * @param param
     * @param result
     * @return
     */
    @HystrixCommand(fallbackMethod = "updateSetmealRuleFallbackInfo")
    @ApiOperation("更新排班规则")
    @RequestMapping(value = "/updateSetmealRule/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateSetmealRule(@PathVariable Long id, @RequestBody SmsSetmealSkdRuleParam param , BindingResult result){
        return smsSetmealService.updateSetmealRule(id,param);
    }
    public CommonResult updateSetmealRuleFallbackInfo(Long id,SmsSetmealSkdRuleParam param,BindingResult result){
        return smsSetmealService.updateSetmealRule(id,param);
    }
}
