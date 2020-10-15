package com.neu.his.cloud.zuul.controller.sms;


import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.neu.his.cloud.zuul.common.CommonPage;
import com.neu.his.cloud.zuul.common.CommonResult;
import com.neu.his.cloud.zuul.distribution.api.pc.sms.ApiPcSmsDeptDistributionService;
import com.neu.his.cloud.zuul.distribution.api.pc.sms.ApiPcSmssignatureDistributionService;
import com.neu.his.cloud.zuul.dto.sms.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Api(tags = "SmsSignatureController", description = "签章管理")
@RequestMapping("/signature")
@CrossOrigin(allowCredentials = "true")
public class SmsSignatureDistributionController {

    @Autowired
    private ApiPcSmssignatureDistributionService service;


    /**
     * 描述：添加签章
     * <p>author: ma
     */
    @HystrixCommand(fallbackMethod = "queryPersonalWorkloadFallbackInfo")
    @ApiOperation("添加签章")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult queryPersonalWorkload(@RequestBody SmsSignatureParam signatureParam){
        return service.queryPersonalWorkload(signatureParam);
    }
    public CommonResult queryPersonalWorkloadFallbackInfo(SmsSignatureParam signatureParam){
        return CommonResult.success(null,"请检查您的网络") ;
    }

    /**
     * 描述:根据ids删除签章
     * <p>author: ma
     */
    @HystrixCommand(fallbackMethod = "deleteFallbackInfo")
    @ApiOperation("根据id删除签章")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@RequestParam("ids") List<Long> ids){
        return service.delete(ids);
    }
    public CommonResult deleteFallbackInfo(List<Long> ids){
        return CommonResult.success(null,"请检查您的网络") ;
    }

    /**
     * 描述:更新签章基本信息，启用禁用操作
     * <p>author: ma
     */
    @HystrixCommand(fallbackMethod = "updateFallbackInfo")
    @ApiOperation("更新签章信息")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult update(@PathVariable("id") Long id, @RequestBody SmsSignatureParam signatureParam){
        return service.update(id,signatureParam);
    }
    public CommonResult updateFallbackInfo(Long id,SmsSignatureParam signatureParam){
        return CommonResult.success(null,"请检查您的网络") ;
    }

    /**
     * 描述:模糊查询科室、且分页
     * <p>author: ma
     */
    @HystrixCommand(fallbackMethod = "listFallbackInfo")
    @ApiOperation("模糊查询科室、且分页")
    @RequestMapping(value = "/select", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<CommonPage<SmsSignatureResult>> list(@RequestBody SmsSignatureParam signatureParam,
                                                             @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                             @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum){
        return service.list(signatureParam,pageSize,pageNum);
    }
    public CommonResult<CommonPage<SmsSignatureResult>> listFallbackInfo(SmsSignatureParam signatureParam, Integer pageSize, Integer pageNum){
        return CommonResult.success(null,"请检查您的网络") ;
    }

    /**
     * 描述:查询正在应用的签章
     * <p>author: ma
     */
    @HystrixCommand(fallbackMethod = "listAllFallbackInfo")
    @ApiOperation("查询正在应用的签章")
    @RequestMapping(value = "/selectAll", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<SmsSignatureResult> listAll(){
        return service.listAll();
    }
    public CommonResult<SmsSignatureResult> listAllFallbackInfo(){
        return CommonResult.success(null,"请检查您的网络") ;
    }

    /**
     * 描述:查询正在应用的签章类型
     * <p>author: ma
     */
    @HystrixCommand(fallbackMethod = "selectTypeFallbackInfo")
    @ApiOperation("查询正在应用的签章类型")
    @RequestMapping(value = "/selectType", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<SmsSignatureTypeResult>> selectType(){
        return service.selectType();
    }
    public CommonResult<List<SmsSignatureTypeResult>> selectTypeFallbackInfo(){
        return CommonResult.success(null,"请检查您的网络") ;
    }
}
