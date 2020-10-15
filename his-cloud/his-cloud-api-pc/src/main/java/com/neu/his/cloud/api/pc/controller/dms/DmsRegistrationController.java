package com.neu.his.cloud.api.pc.controller.dms;

import com.neu.his.cloud.api.pc.common.CommonResult;
import com.neu.his.cloud.api.pc.dto.dms.DmsRegistrationParam;
import com.neu.his.cloud.api.pc.dto.dms.WXDmsRegistrationParam;
import com.neu.his.cloud.api.pc.dto.dms.WxProgramResultsParam;
import com.neu.his.cloud.api.pc.service.dms.DmsRegistrationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@Api(tags = "DmsRegistrationController", description = "挂号管理")
@RequestMapping("/registration")
@CrossOrigin(allowCredentials = "true")
public class DmsRegistrationController {

    @Autowired
    DmsRegistrationService dmsRegistrationService;

    @ApiOperation(value = "挂号")
    @RequestMapping(value = "/createRegistration", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult createRegistration(@RequestBody WXDmsRegistrationParam wxDmsRegistrationParam , BindingResult result){
        return dmsRegistrationService.createRegistration(wxDmsRegistrationParam);

    }

    @ApiOperation(value = "排班列表")
    @RequestMapping(value = "/TimeDifference", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<String>> TimeDifference(@RequestParam String ruletime, @RequestParam Long skdId, @RequestParam int noon){
        return dmsRegistrationService.TimeDifference(ruletime,skdId,noon);
    }

    @ApiOperation(value = "充值")
    @RequestMapping(value = "/recharge", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult recharge(@RequestBody WXDmsRegistrationParam wxDmsRegistrationParam , BindingResult result){
        CommonResult commonResult = dmsRegistrationService.recharge(wxDmsRegistrationParam);
        return commonResult;
    }


    @ApiOperation(value = "退费")
    @RequestMapping(value = "/rollback", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult rollback(@RequestBody WXDmsRegistrationParam wxDmsRegistrationParam , BindingResult result){
        return dmsRegistrationService.rollback(wxDmsRegistrationParam);

    }


    @ApiOperation(value = "小程序挂号")
    @RequestMapping(value = "/programCreateRegistration", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult programCreateRegistration(@RequestBody WXDmsRegistrationParam wxDmsRegistrationParam , BindingResult result){
        return dmsRegistrationService.programCreateRegistration(wxDmsRegistrationParam);
    }

    @ApiOperation(value = "充值")
    @RequestMapping(value = "/WxProgramResults", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult WxProgramResults(@RequestBody WxProgramResultsParam wxProgramResultsParam , BindingResult result){
        return dmsRegistrationService.WxProgramResults(wxProgramResultsParam);

    }
}
