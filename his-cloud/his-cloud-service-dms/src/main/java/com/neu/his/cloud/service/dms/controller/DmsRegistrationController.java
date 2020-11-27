package com.neu.his.cloud.service.dms.controller;

import com.neu.his.cloud.service.dms.WxPay.MyWXPay;
import com.neu.his.cloud.service.dms.WxPay.WXPay;
import com.neu.his.cloud.service.dms.common.CommonResult;
import com.neu.his.cloud.service.dms.dto.app.AppRegistrationParam;
import com.neu.his.cloud.service.dms.dto.dms.*;
import com.neu.his.cloud.service.dms.model.BmsAccount;
import com.neu.his.cloud.service.dms.service.DmsRegistrationService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

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
        //获得病人账户信息
        BmsAccount bmsAccount = dmsRegistrationService.returnAccount(wxDmsRegistrationParam);
        //判断是不是账户信息进行挂号
        if(wxDmsRegistrationParam.getSettlementCatId()==7 && dmsRegistrationService.amountSufficient(bmsAccount.getPatientId(),wxDmsRegistrationParam.getAmount())<=0){
            return CommonResult.success(2,"余额不足");
        }

        int returnResult = dmsRegistrationService.createRegistration(wxDmsRegistrationParam);
        if (returnResult == 1 ){
           /* int insertBmsAccount = dmsRegistrationService.insertBmsAccount(wxDmsRegistrationParam);
            if(insertBmsAccount==1){
                return CommonResult.success(returnResult,"挂号成功");
            }*/
            return CommonResult.success(returnResult,"挂号成功");
        }
        else{
            return CommonResult.success(returnResult,"挂号失败");
        }
    }

    //小程序挂号
    //1.调用DmsRegistrationService的createRegistration
    @ApiOperation(value = "挂号")
    @RequestMapping(value = "/programCreateRegistration", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult programCreateRegistration(@RequestBody WxRegisteredPatam wxRegisteredPatam , BindingResult result){
        try {
            Map<String, String> resultsMap = MyWXPay.getResultsMap(wxRegisteredPatam.getOut_trade_no());
            if (!CollectionUtils.isEmpty(resultsMap)) {
                int i = dmsRegistrationService.WxPayResult(resultsMap, wxRegisteredPatam.getPatientId(), -2L);
                if(i>0){
                    WXDmsRegistrationParam wxDmsRegistrationParam = dmsRegistrationService.WxProgramCompletion(wxRegisteredPatam);
                    if (wxDmsRegistrationParam != null) {
                        wxDmsRegistrationParam.setWxResultId((long)i);
                        int returnResult = dmsRegistrationService.createRegistration(wxDmsRegistrationParam);
                        if (returnResult > 0) {
                            return CommonResult.success(1, "挂号成功");
                        }
                    }
                }
                MyWXPay.refund(resultsMap.get("transaction_id"),resultsMap.get("total_fee"),resultsMap.get("total_fee"));
                return CommonResult.success(0, "挂号失败");
            }
            return CommonResult.success(2, "支付失败");
        }catch (Exception e){
            e.printStackTrace();
            return CommonResult.success(2, "支付失败");
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

    //判断是否有账户
    @ApiOperation(value = "判断是否有账户")
    @RequestMapping(value = "/isAccount", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult isAccount(@RequestParam String identificationNo){
        int returnResult = dmsRegistrationService.isAccount(identificationNo);
        if(returnResult>0){
            return CommonResult.success(1,"拥有账户");
        }
        return CommonResult.success(0,"没有账户");
    }

    //修改用户信息
    @ApiOperation(value = "修改用户信息")
    @RequestMapping(value = "/updateInformation", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateInformation(@RequestBody WXDmsRegistrationParam wxProgramResultsParam){
        int returnResult = dmsRegistrationService.updateInformation(wxProgramResultsParam);
        if(returnResult>0){
            return CommonResult.success(1,"修改成功");
        }
        return CommonResult.success(0,"修改失败");
    }

    //获得各个方式的退款金额
    @ApiOperation(value = "获得各个方式的退款金额")
    @RequestMapping(value = "/selectRefundResultsParam", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<RefundResultsParam> selectRefundResultsParam(@RequestBody WXDmsRegistrationParam wxProgramResultsParam){
        RefundResultsParam refundResultsParam = dmsRegistrationService.selectRefundResultsParam(wxProgramResultsParam);
        return CommonResult.success(refundResultsParam);
    }

    //判断是否同一天挂了同医生的号
    @ApiOperation(value = "获得各个方式的退款金额")
    @RequestMapping(value = "/queryCreateRegistration", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult queryCreateRegistration(@RequestBody WXDmsRegistrationParam wxDmsRegistrationParam){
        int i = dmsRegistrationService.queryCreateRegistration(wxDmsRegistrationParam, wxDmsRegistrationParam.getPatientId());
        if(i==3){
            return CommonResult.success(3,"今天已经预约了该医生");
        }else {
            return CommonResult.success(4,"预约成功");
        }

    }

}
