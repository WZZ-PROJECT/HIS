package com.neu.his.cloud.service.dms.controller;

import com.neu.his.cloud.service.dms.common.CommonResult;
import com.neu.his.cloud.service.dms.dto.app.AppRegistrationParam;
import com.neu.his.cloud.service.dms.dto.dms.DmsRegHistoryResult;
import com.neu.his.cloud.service.dms.dto.dms.DmsRegistrationParam;
import com.neu.his.cloud.service.dms.dto.dms.WXDmsRegistrationParam;
import com.neu.his.cloud.service.dms.dto.dms.WxProgramResultsParam;
import com.neu.his.cloud.service.dms.service.DmsRegistrationService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@Controller
@Api(tags = "DmsRegistrationController", description = "挂号信息管理")
@RequestMapping("/registration")
@CrossOrigin(allowCredentials = "true")
public class DmsRegistrationController {

    @Autowired
    DmsRegistrationService dmsRegistrationService;

    @ApiOperation("查询历史挂号信息")
    @RequestMapping(value = "/listAllRegistration", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<List<DmsRegHistoryResult>> listAllRegistration(@RequestParam("identificationNo") String identificationNo){
        List<DmsRegHistoryResult> list = dmsRegistrationService.listRegHistory(identificationNo);
        return CommonResult.success(list);
    }

    //his挂号
    //1.调用DmsRegistrationService的createRegistration
    @ApiOperation(value = "挂号")
    @RequestMapping(value = "/createRegistration", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult createRegistration(@RequestBody WXDmsRegistrationParam wxDmsRegistrationParam , BindingResult result){
        int returnResult = dmsRegistrationService.createRegistration(wxDmsRegistrationParam);
        if (returnResult == 1 ){
           /* int insertBmsAccount = dmsRegistrationService.insertBmsAccount(wxDmsRegistrationParam);
            if(insertBmsAccount==1){
                return CommonResult.success(returnResult,"挂号成功");
            }*/
            return CommonResult.success(1,"挂号成功");
        }
        else{
            return CommonResult.success(0,"挂号失败");
        }
    }

    //小程序挂号
    //1.调用DmsRegistrationService的createRegistration
    @ApiOperation(value = "挂号")
    @RequestMapping(value = "/programCreateRegistration", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult programCreateRegistration(@RequestBody WXDmsRegistrationParam wxDmsRegistrationParam , BindingResult result){
        Long patientId=Long.parseLong(wxDmsRegistrationParam.getName());
        if(dmsRegistrationService.amountSufficient(patientId,wxDmsRegistrationParam.getAmount())<=0){
            return CommonResult.success(2,"余额不足");
        }
        wxDmsRegistrationParam = dmsRegistrationService.WxProgramCompletion(wxDmsRegistrationParam);
        int returnResult = dmsRegistrationService.createRegistration(wxDmsRegistrationParam);
        if (returnResult == 1){
            int insertBmsAccount = dmsRegistrationService.subBmsAccount(patientId,wxDmsRegistrationParam.getAmount());
            if(insertBmsAccount==1){
                return CommonResult.success(1,"挂号成功");

            }
            return CommonResult.success(0,"挂号失败");
        }
        else{
            return CommonResult.success(0,"挂号失败");
        }
    }


    @ApiOperation("挂号")
    @RequestMapping(value = "/appReg", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult registration(@RequestBody AppRegistrationParam appRegistrationParam, BindingResult result){
        System.err.println(appRegistrationParam.toString());
        int count = dmsRegistrationService.appRegistration(appRegistrationParam);
        if(count > 0){
            return CommonResult.success(count, "挂号成功");
        }
        return CommonResult.failed("挂号失败");
    }

    @ApiOperation("排班间隔")
    @RequestMapping(value = "/TimeDifference", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<String>> TimeDifference(@RequestParam String ruletime, @RequestParam Long skdId,@RequestParam int noon){
        List<String> strings = dmsRegistrationService.TimeDifference(ruletime,skdId,noon);
        return CommonResult.success(strings);
    }

    //充值
    //1.调用DmsRegistrationService的createRegistration
    @ApiOperation(value = "充值")
    @RequestMapping(value = "/recharge", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult recharge(@RequestBody WXDmsRegistrationParam wxDmsRegistrationParam , BindingResult result){
        int returnResult = dmsRegistrationService.recharge(wxDmsRegistrationParam);
        if(returnResult>0){
            return CommonResult.success(1,"充值成功");
        }
        return CommonResult.success(0,"充值失败");
    }

    //退费
    //1.调用DmsRegistrationService的createRegistration
    @ApiOperation(value = "退费")
    @RequestMapping(value = "/rollback", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult rollback(@RequestBody WXDmsRegistrationParam wxDmsRegistrationParam , BindingResult result){
        int returnResult = dmsRegistrationService.rollback(wxDmsRegistrationParam);
        if(returnResult>0){
            return CommonResult.success(1,"退费成功");
        }
        return CommonResult.success(0,"退费失败");
    }

    //小程序结果封装
    //1.调用DmsRegistrationService的createRegistration
    @ApiOperation(value = "结果封装")
    @RequestMapping(value = "/WxProgramResults", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult WxProgramResults(@RequestBody WxProgramResultsParam wxProgramResultsParam , BindingResult result){
        int returnResult = dmsRegistrationService.WxProgramResults(wxProgramResultsParam);
        if(returnResult>0){
            return CommonResult.success(1,"充值成功");
        }
        return CommonResult.success(0,"充值成功");
    }
}
