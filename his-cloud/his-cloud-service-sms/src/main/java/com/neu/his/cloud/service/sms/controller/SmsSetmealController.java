package com.neu.his.cloud.service.sms.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.neu.his.cloud.service.sms.common.CommonPage;
import com.neu.his.cloud.service.sms.common.CommonResult;
import com.neu.his.cloud.service.sms.dto.dms.DmsNonDrugResult;
import com.neu.his.cloud.service.sms.dto.sms.*;
import com.neu.his.cloud.service.sms.service.SmsSetmealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = {"SmsSetmealController"}, description = "医疗排班管理")
@RequestMapping({"/setmeal"})
@CrossOrigin(allowCredentials = "true")
public class SmsSetmealController {
    @Autowired
    SmsSetmealService smsSetmealService;

    @ApiOperation("查询所有医疗套餐")
    @RequestMapping(value = {"/selectSetmeal"}, method = {RequestMethod.POST})
    @ResponseBody
    public CommonResult<CommonPage<SmsSetmealResult>> selectSetmeal(@RequestBody DmsNonDrugResult dmsNonDrugResult, @RequestParam(value = "pageSize",defaultValue = "5") Integer pageSize, @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum) {
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<SmsSetmealResult> list = this.smsSetmealService.selectSetmeal(dmsNonDrugResult, pageSize, pageNum);
        Long pageTotal = page.getTotal();
        return CommonResult.success(CommonPage.restPage(list, pageTotal));
    }

    /**
     * 描述:根据部门id筛选排版规则基本信息、分页
     * <p>author: ma
     * <p>author:  修改查询不到科室排班规则500错误,并返回创建人name
     */
    @ApiOperation("根据部门id筛选排版规则基本信息、分页")
    @RequestMapping(value = "/SetmealListRule", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<CommonPage<SmsSetmealRuleResult>> SetmealListRule(@RequestParam("deptId")  Long deptId,
                                                               @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                               @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum){
        Page page =PageHelper.startPage(pageNum, pageSize);
        List<SmsSetmealRuleResult> list = smsSetmealService.selectRuleByDept(deptId);
        if( CollectionUtils.isEmpty(list)){
            return CommonResult.success(null,"不存在排班规则");
        }
        Long pageTotal=page.getTotal();
        return CommonResult.success(CommonPage.restPage(list,pageTotal));
    }

    /**
     * 描述:删除排班规则
     * <p>author: ma
     */
    @ApiOperation("删除排班规则")
    @RequestMapping(value = "/deleteSetmealRule", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult deleteRule(@RequestParam("ids") List<Long> ids){
        int count = smsSetmealService.deleteSetmealRule(ids);
        if(count > 0){
            return CommonResult.success(count,"删除成功");
        }
        return CommonResult.failed("删除失败");
    }

    /**
     * 描述:查找排班记录、分页
     * <p>
     */
    @ApiOperation("查找排班记录、分页")
    @RequestMapping(value = "/listSetmealSkd", method = RequestMethod.POST)
    @ResponseBody
    public  CommonResult<CommonPage<SmsSetMealSkdResult>> listSkd(@RequestBody SmsSetmealSkdParam SmsSetmealSkdParam){
        Page page =PageHelper.startPage(SmsSetmealSkdParam.getPageNum(), SmsSetmealSkdParam.getPageSize());

        List<SmsSetMealSkdResult> smsSkdResultList = smsSetmealService.listSetmealSkd(SmsSetmealSkdParam);
        Long pageTotal=page.getTotal();
        return CommonResult.success(CommonPage.restPage(smsSkdResultList,pageTotal));
    }



    /**
     * 描述:创建医疗套餐排班规则
     * <p>author: ma
     */
    @ApiOperation("新增医疗套餐排班规则")
    @RequestMapping(value = "/createSetmealRule", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult createSetmealRule(@RequestBody SmsSetmealSkdRuleParam param, BindingResult result){
        int count = smsSetmealService.createSetmealRule(param);
        if(count > 0){
            return CommonResult.success(count, "新增排班规则成功");
        }
        return CommonResult.failed("新增排班规则失败");
    }

    /**
     * 描述:根据多条排班规则生成医疗套餐skd
     * <p>author: ma
     */
    @ApiOperation("根据多条排班规则生成医疗套餐skd")
    @RequestMapping(value = "/generateSetMealSkd", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult generateSetMealSkd(@RequestParam("ruleIds") List<Long> ruleIds,
                                           @RequestParam("startDate")@DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                           @RequestParam("endDate")@DateTimeFormat(pattern = "yyyy-MM-dd")Date endDate){
        int count = smsSetmealService.generateSetMealSkd(ruleIds,startDate,endDate);
        if(count > 0){
            return CommonResult.success(count,"生成成功");
        }
        return CommonResult.failed("生成失败");
    }

    /**
     * 描述:根据规则id查询一条规则详情
     * <p>author: ma
     */
    @ApiOperation("根据规则id查询一条规则详情")
    @RequestMapping(value = "/getSetmealRuleDetail", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<SmsSetmealRuleResult> getSetmealRuleDetail(@RequestParam("ruleId") Long ruleId){
        SmsSetmealRuleResult result = smsSetmealService.getSetmealRuleDetail(ruleId);
        return CommonResult.success(result);
    }

    @ApiOperation("更新排班规则")
    @RequestMapping(value = "/updateSetmealRule/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateSetmealRule(@PathVariable Long id, @RequestBody SmsSetmealSkdRuleParam param , BindingResult result){
        int count = smsSetmealService.updateSetmealRule(id,param);
        if(count > 0){
            return CommonResult.success(count,"更新成功");
        }
        return CommonResult.failed("更新失败");
    }
}
