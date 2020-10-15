package com.neu.his.cloud.api.pc.controller.sms;


import com.neu.his.cloud.api.pc.common.CommonPage;
import com.neu.his.cloud.api.pc.common.CommonResult;
import com.neu.his.cloud.api.pc.dto.dms.DmsNonDrugResult;
import com.neu.his.cloud.api.pc.dto.sms.*;
import com.neu.his.cloud.api.pc.dto.sms.SmsSetmealResult;
import com.neu.his.cloud.api.pc.dto.sms.SmsSetmealSkdRuleParam;
import com.neu.his.cloud.api.pc.service.sms.SmsSetmealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import javax.annotation.Resource;
import java.util.Date;


/**
 * wzongzheng
 */
@RestController
@Api(tags = "SmsSetmealController", description = "医疗排班管理")
@RequestMapping("/setmeal")
@CrossOrigin(allowCredentials = "true")
public class SmsSetmealController {

    @Resource
    private SmsSetmealService smsSetmealService;

    /**
     * 描述:查询所有医疗套餐
     * <p>author: ma
     */
    @ApiOperation("查询所有医疗套餐")
    @RequestMapping(value = "/selectSetmeal", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<CommonPage<SmsSetmealResult>> selectSetmeal(@RequestBody DmsNonDrugResult dmsNonDrugResult,
                                                                    @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                                    @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum){
      return smsSetmealService.selectSetmeal(dmsNonDrugResult,pageSize,pageNum);
    }

    @ApiOperation("根据部门id筛选排版规则基本信息、分页")
    @RequestMapping(value = "/SetmealListRule", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<CommonPage<SmsSetmealRuleResult>> SetmealListRule(@RequestParam("deptId")  Long deptId,
                                                                          @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                                          @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum){
        return smsSetmealService.SetmealListRule(deptId,pageSize,pageNum);

    }

    @ApiOperation("删除排班规则")
    @RequestMapping(value = "/deleteSetmealRule", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult deleteRule(@RequestParam("ids") List<Long> ids){
        return smsSetmealService.deleteSetmealRule(ids);
    }

    @ApiOperation("查找排班记录、分页")
    @RequestMapping(value = "/listSetmealSkd", method = RequestMethod.POST)
    @ResponseBody
    public  CommonResult<CommonPage<SmsSetMealSkdResult>> listSetmealSkd(@RequestBody SmsSetmealSkdParam queryParam){
        return smsSetmealService.listSetmealSkd(queryParam);
    }

    /**
     * 描述:创建医疗套餐排班规则
     * <p>author: ma
     */
    @ApiOperation("新增医疗套餐排班规则")
    @RequestMapping(value = "/createSetmealRule", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult createSetmealRule(@RequestBody SmsSetmealSkdRuleParam param, BindingResult result){
        return smsSetmealService.createSetmealRule(param);
    }

    /**
     * 描述:根据多条排班规则生成医疗套餐skd
     * <p>author: ma
     */
    @ApiOperation("根据多条排班规则生成医疗套餐skd")
    @RequestMapping(value = "/generateSetMealSkd", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult generateSetMealSkd(@RequestParam("ruleIds") List<Long> ruleIds,
                                           @RequestParam("startDate")String startDate,
                                           @RequestParam("endDate")String endDate){
        return smsSetmealService.generateSetMealSkd(ruleIds,startDate,endDate);
    }

    /**
     * 描述:根据规则id查询一条规则详情
     * <p>author: ma
     */
    @ApiOperation("根据规则id查询一条规则详情")
    @RequestMapping(value = "/getSetmealRuleDetail", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<SmsSetmealRuleResult> getSetmealRuleDetail(@RequestParam("ruleId") Long ruleId){
        return smsSetmealService.getSetmealRuleDetail(ruleId);
    }

    @ApiOperation("更新排班规则")
    @RequestMapping(value = "/updateSetmealRule/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateSetmealRule(@PathVariable Long id, @RequestBody SmsSetmealSkdRuleParam param , BindingResult result){
        return smsSetmealService.updateSetmealRule(id,param);
    }
}
