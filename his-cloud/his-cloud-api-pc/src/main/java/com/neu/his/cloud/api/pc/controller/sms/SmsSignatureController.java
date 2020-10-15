package com.neu.his.cloud.api.pc.controller.sms;


import com.neu.his.cloud.api.pc.common.CommonPage;
import com.neu.his.cloud.api.pc.common.CommonResult;
import com.neu.his.cloud.api.pc.dto.sms.SmsSignatureParam;
import com.neu.his.cloud.api.pc.dto.sms.SmsSignatureResult;
import com.neu.his.cloud.api.pc.dto.sms.SmsSignatureTypeResult;
import com.neu.his.cloud.api.pc.service.sms.SmsSignatureService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
        return service.queryPersonalWorkload(signatureParam);
    }

    /**
     * 描述:根据ids删除签章
     * <p>author: ma
     */
    @ApiOperation("根据id删除签章")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@RequestParam("ids") List<Long> ids){
        return service.delete(ids);
    }

    /**
     * 描述:更新签章基本信息，启用禁用操作
     * <p>author: ma
     */
    @ApiOperation("更新签章信息")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult update(@PathVariable("id") Long id, @RequestBody SmsSignatureParam signatureParam){
        return service.update(id,signatureParam);
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
        return service.list(signatureParam,pageSize,pageNum);
    }

    /**
     * 描述:查询正在应用的签章
     * <p>author: ma
     */
    @ApiOperation("查询正在应用的签章")
    @RequestMapping(value = "/selectAll", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<SmsSignatureResult> listAll(){
        return service.listAll();
    }

    /**
     * 描述:查询正在应用的签章类型
     * <p>author: ma
     */
    @ApiOperation("查询正在应用的签章类型")
    @RequestMapping(value = "/selectType", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<SmsSignatureTypeResult>> selectType(){
        return service.selectType();
    }

}
