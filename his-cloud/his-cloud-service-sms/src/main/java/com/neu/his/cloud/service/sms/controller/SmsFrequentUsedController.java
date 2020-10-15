package com.neu.his.cloud.service.sms.controller;

import com.neu.his.cloud.service.sms.common.CommonResult;
import com.neu.his.cloud.service.sms.dto.sms.SmsFrequentUsedResult;
import com.neu.his.cloud.service.sms.service.SmsFrequentUsedService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "SmsFrequentUsedController", description = "常用项管理")
@RequestMapping("/frequentUsed")
@CrossOrigin(allowCredentials = "true")
public class SmsFrequentUsedController {

    @Autowired
    private SmsFrequentUsedService smsFrequentUsedService;

    /**
     * 描述:添加一个常用项
     * <p>author: ma
     */
    @ApiOperation("添加一个常用项")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult addFrequent(@RequestParam("staffId") Long staffId,
                                    @RequestParam("addType") int addType,
                                    @RequestParam("addId") Long addId){
        int count = smsFrequentUsedService.addFrequent(staffId,addType,addId);
        if(count > 0){
            return CommonResult.success(count, "添加常用项成功");
        }
        return CommonResult.failed("添加常用项失败");
    }

    /**
     * 描述:删除一个常用项
     * <p>author: ma
     */
    @ApiOperation("删除一个常用项")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult deleteFrequent(@RequestParam("staffId") Long staffId,
                               @RequestParam("deleteType") int deleteType,
                               @RequestParam("deleteId") Long deleteId){
        int count = smsFrequentUsedService.deleteFrequent(staffId,deleteType,deleteId);
        if(count > 0){
            return CommonResult.success(count,"删除成功");
        }
        return CommonResult.failed("删除失败");
    }


    /**
     * 描述:查询指定常用项
     * <p>author: ma
     */
    @ApiOperation("查询指定常用项")
    @RequestMapping(value = "/selectByType", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<SmsFrequentUsedResult> selectFrequent(@RequestParam("staffId") Long staffId,
                                                              @RequestParam("selectType") int selectType){
        SmsFrequentUsedResult smsFrequentUsedResult = smsFrequentUsedService.selectFrequent(staffId,selectType);
        return CommonResult.success(smsFrequentUsedResult);
    }

}
