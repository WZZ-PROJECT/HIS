package com.neu.his.cloud.api.pc.controller.sms;

import com.neu.his.cloud.api.pc.common.CommonPage;
import com.neu.his.cloud.api.pc.common.CommonResult;
import com.neu.his.cloud.api.pc.dto.sms.SmsSkdDocParam;
import com.neu.his.cloud.api.pc.dto.sms.SmsSkdDocResult;
import com.neu.his.cloud.api.pc.dto.sms.SmsStaffParam;
import com.neu.his.cloud.api.pc.dto.sms.SmsStaffResult;
import com.neu.his.cloud.api.pc.model.PmsPatient;
import com.neu.his.cloud.api.pc.service.sms.SmsSkdService;
import com.neu.his.cloud.api.pc.service.sms.SmsStaffService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "SmsStaffController", description = "用户管理")
@RequestMapping("/staff")
@CrossOrigin(allowCredentials = "true")
public class SmsStaffController {

    @Autowired
    private SmsStaffService smsStaffService;
    @Autowired
    private SmsSkdService smsSkdService;


    @ApiOperation(value = "查询上班医生")
    @RequestMapping(value = "/listDocBySkd", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<List<SmsSkdDocResult>> listDocBySkd(@RequestBody SmsSkdDocParam smsSkdDocParam, BindingResult result){
       return smsSkdService.listDocBySkd(smsSkdDocParam);
    }



    @ApiOperation("根据ids删除用户")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@RequestParam("ids") List<Long> ids) {
        return smsStaffService.delete(ids);
    }

    /**
     * 描述:更新一个用户的全部信息
     * <p>author: ma
     */
    @ApiOperation("更新用户信息")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult update(@PathVariable Long id, @RequestBody SmsStaffParam smsStaffParam, BindingResult result){
        return smsStaffService.update(id,smsStaffParam);
    }


    @ApiOperation("模糊查询用户、且分页")
    @RequestMapping(value = "/select", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<CommonPage<SmsStaffResult>> list(@RequestBody SmsStaffParam queryParam){
       return smsStaffService.list(queryParam);
    }


    @ApiOperation("查询所有用户")
    @RequestMapping(value = "/selectAll", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<SmsStaffResult>> listAll(){
        return  smsStaffService.listAll();
    }


    @ApiOperation("将患者和微信号绑定")
    @RequestMapping(value = "/bindPatient", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult bindPatient(@RequestBody PmsPatient pmsPatient){
        return smsStaffService.bindPatient(pmsPatient);
    }


}
