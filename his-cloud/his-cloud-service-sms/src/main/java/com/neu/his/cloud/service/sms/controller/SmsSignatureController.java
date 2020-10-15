package com.neu.his.cloud.service.sms.controller;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.neu.his.cloud.service.sms.common.CommonPage;
import com.neu.his.cloud.service.sms.common.CommonResult;
import com.neu.his.cloud.service.sms.dto.sms.*;
import com.neu.his.cloud.service.sms.service.SmsSignatureService;
import com.neu.his.cloud.service.sms.service.SmsWorkloadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@Api(tags = "SmsSignatureController", description = "签章管理")
@RequestMapping("/signature")
@CrossOrigin(allowCredentials = "true")
public class SmsSignatureController {

    @Autowired
    private SmsSignatureService service;

    /**
     * 描述：添加签章
     * <p>author: ma
     */
    @ApiOperation("添加签章")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult queryPersonalWorkload(@RequestBody SmsSignatureParam signatureParam){
        int count = service.create(signatureParam);
        if(count > 0){
            return CommonResult.success(count, "添加签章成功");
        }
        return CommonResult.failed("添加签章失败，");
    }

    /**
     * 描述:根据ids删除签章
     * <p>author: ma
     */
    @ApiOperation("根据id删除签章")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@RequestParam("ids") List<Long> ids){
        int count = service.delete(ids);
        if(count > 0){
            return CommonResult.success(count,"删除签章成功");
        }
        return CommonResult.failed("删除签章失败");
    }

    /**
     * 描述:更新签章基本信息，启用禁用操作
     * <p>author: ma
     */
    @ApiOperation("更新签章信息")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult update(@PathVariable("id") Long id, @RequestBody SmsSignatureParam signatureParam){
        int count = service.update(id,signatureParam);
        if(id > 0){
            return CommonResult.success(count,"更新成功");
        }
        return CommonResult.failed("更新失败");
    }

    /**
     * 描述:模糊查询科室、且分页
     * <p>author: ma
     */
    @ApiOperation("模糊查询科室、且分页")
    @RequestMapping(value = "/select", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<CommonPage<SmsSignatureResult>> list(@RequestBody SmsSignatureParam signatureParam,
                                                             @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                             @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum){
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<SmsSignatureResult> list = service.select(signatureParam);
        Long pageTotal=page.getTotal();

        return CommonResult.success(CommonPage.restPage(list,pageTotal));
    }

    /**
     * 描述:查询正在应用的签章
     * <p>author: ma
     */
    @ApiOperation("查询正在应用的签章")
    @RequestMapping(value = "/selectAll", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<SmsSignatureResult> listAll(){
        SmsSignatureResult smsSignatureResult = service.selectAll();
        return CommonResult.success(smsSignatureResult);
    }

    /**
     * 描述:查询正在应用的签章类型
     * <p>author: ma
     */
    @ApiOperation("查询正在应用的签章类型")
    @RequestMapping(value = "/selectType", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<SmsSignatureTypeResult>> selectType(){
        List<SmsSignatureTypeResult> result = service.selectType();
        return CommonResult.success(result);
    }

}
