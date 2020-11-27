package com.neu.his.cloud.zuul.controller.dms;


import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.neu.his.cloud.zuul.common.CommonResult;
import com.neu.his.cloud.zuul.controller.bms.GetPackageController;
import com.neu.his.cloud.zuul.distribution.api.pc.dms.ApiPcDmsRegistrationDistributionService;
import com.neu.his.cloud.zuul.dto.dms.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "DmsRegistrationDistributionController", description = "挂号管理")
@RequestMapping("/registration")
@CrossOrigin(allowCredentials = "true")
public class DmsRegistrationDistributionController {

    @Autowired
    ApiPcDmsRegistrationDistributionService apiPcDmsRegistrationDistributionService;

    @HystrixCommand(fallbackMethod = "createRegistrationFallbackInfo")
    @ApiOperation(value = "挂号")
    @RequestMapping(value = "/createRegistration", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult createRegistration(@RequestBody WXDmsRegistrationParam wxDmsRegistrationParam, BindingResult result){
       return apiPcDmsRegistrationDistributionService.createRegistration(wxDmsRegistrationParam);

    }
    private CommonResult createRegistrationFallbackInfo( WXDmsRegistrationParam dmsRegistrationParam , BindingResult result){
        return CommonResult.success(null,"请检查您的网络") ;
    }

    @HystrixCommand(fallbackMethod = "TimeDifferenceFallbackInfo")
    @ApiOperation(value = "排班管理")
    @RequestMapping(value = "/TimeDifference", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<String>> TimeDifference(@RequestParam String ruletime, @RequestParam Long skdId , @RequestParam int noon){
        return apiPcDmsRegistrationDistributionService.TimeDifference(ruletime,skdId,noon);

    }
    private CommonResult<List<String>> TimeDifferenceFallbackInfo( String ruletime, Long skdId,int noon){
        return CommonResult.success(null,"请检查您的网络") ;
    }

    @HystrixCommand(fallbackMethod = "rechargeFallbackInfo")
    @ApiOperation(value = "充值")
    @RequestMapping(value = "/recharge", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult recharge(@RequestBody WXDmsRegistrationParam wxDmsRegistrationParam, BindingResult result){
        return apiPcDmsRegistrationDistributionService.recharge(wxDmsRegistrationParam);

    }
    private CommonResult rechargeFallbackInfo( WXDmsRegistrationParam dmsRegistrationParam , BindingResult result){
        return CommonResult.success(null,"请检查您的网络") ;
    }


    @HystrixCommand(fallbackMethod = "rollbackFallbackInfo")
    @ApiOperation(value = "退费")
    @RequestMapping(value = "/rollback", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult rollback(@RequestBody WXDmsRegistrationParam wxDmsRegistrationParam, BindingResult result){
        return apiPcDmsRegistrationDistributionService.rollback(wxDmsRegistrationParam);

    }
    private CommonResult rollbackFallbackInfo( WXDmsRegistrationParam dmsRegistrationParam , BindingResult result){
        return CommonResult.success(null,"请检查您的网络") ;
    }


    @HystrixCommand(fallbackMethod = "programCreateRegistrationFallbackInfo")
    @ApiOperation(value = "小程序挂号")
    @RequestMapping(value = "/programCreateRegistration", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult programCreateRegistration(@RequestBody WxRegisteredPatam wxRegisteredPatam, BindingResult result) throws Exception {
        /*Map<String, String> resultsMap = GetPackageController.getResultsMap(wxRegisteredPatam.getOut_trade_no());
        wxRegisteredPatam.setResults(resultsMap);*/
        return apiPcDmsRegistrationDistributionService.programCreateRegistration(wxRegisteredPatam);

    }
    private CommonResult programCreateRegistrationFallbackInfo( WxRegisteredPatam wxRegisteredPatam , BindingResult result){
        return CommonResult.success(null,"请检查您的网络") ;
    }




    @HystrixCommand(fallbackMethod = "WxProgramResultsFallbackInfo")
    @ApiOperation(value = "小程序充值结果封装")
    @RequestMapping(value = "/WxProgramResults", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult WxProgramResults(@RequestBody WxProgramResultsParam wxProgramResultsParam, BindingResult result){
        try {
            Map<String, String> resultsMap = GetPackageController.getResultsMap(wxProgramResultsParam.getOut_trade_no());
            wxProgramResultsParam.setResults(resultsMap);
            return apiPcDmsRegistrationDistributionService.WxProgramResults(wxProgramResultsParam);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private CommonResult WxProgramResultsFallbackInfo(WxProgramResultsParam wxProgramResultsParam , BindingResult result){
        return CommonResult.success(null,"请检查您的网络") ;
    }

    @HystrixCommand(fallbackMethod = "isAccountFallbackInfo")
    @ApiOperation(value = "判断是否有账户")
    @RequestMapping(value = "/isAccount", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult isAccount(@RequestParam String identificationNo){
        return apiPcDmsRegistrationDistributionService.isAccount(identificationNo);

    }
    private CommonResult isAccountFallbackInfo(String identificationNo){
        return CommonResult.success(null,"请检查您的网络") ;
    }

    @HystrixCommand(fallbackMethod = "updateInformationFallbackInfo")
    @ApiOperation(value = "修改病人信息")
    @RequestMapping(value = "/updateInformation", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateInformation(@RequestBody WXDmsRegistrationParam wxDmsRegistrationParam){
        return apiPcDmsRegistrationDistributionService.updateInformation(wxDmsRegistrationParam);

    }
    private CommonResult updateInformationFallbackInfo( WXDmsRegistrationParam wxDmsRegistrationParam){
        return CommonResult.success(null,"请检查您的网络") ;
    }

    @HystrixCommand(fallbackMethod = "selectRefundResultsParamFallbackInfo")
    @ApiOperation(value = "获得各个方式的退款金额")
    @RequestMapping(value = "/selectRefundResultsParam", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<RefundResultsParam> selectRefundResultsParam(@RequestBody WXDmsRegistrationParam wxDmsRegistrationParam){
        return apiPcDmsRegistrationDistributionService.selectRefundResultsParam(wxDmsRegistrationParam);

    }
    private CommonResult<RefundResultsParam> selectRefundResultsParamFallbackInfo( WXDmsRegistrationParam wxDmsRegistrationParam){
        return CommonResult.success(null,"请检查您的网络") ;
    }

    @HystrixCommand(fallbackMethod = "queryCreateRegistrationFallbackInfo")
    @ApiOperation(value = "同一患者在同一午别，未就诊情况下应不能重复挂同一医生")
    @RequestMapping(value = "/queryCreateRegistration", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult queryCreateRegistration(@RequestBody WXDmsRegistrationParam wxDmsRegistrationParam){
        return apiPcDmsRegistrationDistributionService.queryCreateRegistration(wxDmsRegistrationParam);

    }
    private CommonResult queryCreateRegistrationFallbackInfo(WXDmsRegistrationParam wxDmsRegistrationParam){
        return CommonResult.success(null,"请检查您的网络") ;
    }
}
