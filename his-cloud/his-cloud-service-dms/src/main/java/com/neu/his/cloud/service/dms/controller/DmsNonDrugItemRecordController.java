package com.neu.his.cloud.service.dms.controller;


import com.neu.his.cloud.service.dms.common.CommonResult;
import com.neu.his.cloud.service.dms.component.RabbitMQOrderSender;
import com.neu.his.cloud.service.dms.dto.app.AppCheckTestResult;
import com.neu.his.cloud.service.dms.dto.dms.DmsNonDrugItemRecordListParam;
import com.neu.his.cloud.service.dms.dto.dms.DmsNonDrugItemRecordParam;
import com.neu.his.cloud.service.dms.dto.dms.DmsNonDrugItemRecordResult;
import com.neu.his.cloud.service.dms.service.DmsNonDrugItemRecordService;
import com.neu.his.cloud.service.dms.service.DmsRegistrationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Api(tags = "DmsNonDrugItemRecordController", description = "非药品收费项目记录管理")
@RequestMapping("/nonDrugItemRecord")
@CrossOrigin(allowCredentials = "true")
public class DmsNonDrugItemRecordController {

    @Autowired
    DmsNonDrugItemRecordService dmsNonDrugItemRecordService;

    @Autowired
    RabbitMQOrderSender rabbitMQOrderSender;

    @Autowired
    DmsRegistrationService service;
    //开立项目
    //1.调用DmsNonDrugItemRecordService的apply
    @ApiOperation(value = "开立项目")
    @RequestMapping(value = "/apply", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult apply(@RequestBody DmsNonDrugItemRecordListParam dmsNonDrugItemRecordListParam, BindingResult result){
//        List<BigDecimal> collect = dmsNonDrugItemRecordListParam.getDmsNonDrugItemRecordParamList()
//                .stream().map(DmsNonDrugItemRecordParam::getAmount)
//                .collect(Collectors.toList());
//        CommonResult commonResult = service.amountSufficient(dmsNonDrugItemRecordListParam.getRegistrationId(),collect);
//        if (commonResult.getCode() == 500) {
//            return commonResult;
//        }
        List<Long> currentIdList = dmsNonDrugItemRecordService.apply(dmsNonDrugItemRecordListParam);
        if(currentIdList == null || currentIdList.size() <= 0){
            return CommonResult.failed("开立失败");
        }else{
            //获取订单超时时间，假设为30s
            long delayTimes = 60 * 1000 *60 *12;
            //发送延迟消息
            for(Long id : currentIdList){
                rabbitMQOrderSender.sendMessage(id, 1, delayTimes);
            }

            return CommonResult.success("开立成功");
        }
    }
    //作废项目
    //1.调用DmsNonDrugItemRecordService的invalid
    @ApiOperation(value = "作废项目")
    @RequestMapping(value = "/invalid", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult invalid(@RequestParam("ids") List<Long> ids){
        int returnResult = dmsNonDrugItemRecordService.invalid(ids);
        if (returnResult == 1){
            return CommonResult.success(returnResult);
        }
        else{
            return CommonResult.failed();
        }
    }

    //删除项目
    //1.调用DmsNonDrugItemRecordService的delectById
    @ApiOperation(value = "作废项目")
    @RequestMapping(value = "/deleteById", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult deleteById(@RequestParam("ids") List<Long> ids){
        int returnResult = dmsNonDrugItemRecordService.deleteById(ids);
        if (returnResult == 1){
            return CommonResult.success(returnResult);
        }
        else{
            return CommonResult.failed();
        }
    }

    //刷新项目
    //1.调用DmsNonDrugItemRecordService的listByRegAndType
    @ApiOperation(value = "刷新项目")
    @RequestMapping(value = "/listByRegAndType", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<List<DmsNonDrugItemRecordResult>> listByRegAndType(@RequestParam("registrationId") Long registrationId, @RequestParam("type") Integer type){
        List<DmsNonDrugItemRecordResult> dmsNonDrugItemRecordResultList = dmsNonDrugItemRecordService.listByRegAndType(registrationId,type);
        return CommonResult.success(dmsNonDrugItemRecordResultList);
    }


    @ApiOperation("查询检查检验结果")
    @RequestMapping(value = "/getResult", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<AppCheckTestResult> getResult(@RequestParam("id") Long id){
        AppCheckTestResult result = dmsNonDrugItemRecordService.appGetResult(id);
        return CommonResult.success(result);
    }


}
