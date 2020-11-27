package com.neu.his.cloud.zuul.controller.dms;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.neu.his.cloud.zuul.common.CommonPage;
import com.neu.his.cloud.zuul.common.CommonResult;
import com.neu.his.cloud.zuul.distribution.api.pc.dms.ApiPcDmsMechanicItemRecordDistributionService;
import com.neu.his.cloud.zuul.distribution.api.pc.dms.ApiPcDmsMedicinePrescriptionRecordDistributionService;
import com.neu.his.cloud.zuul.dto.dms.DmsMechanicItemRecordResult;
import com.neu.his.cloud.zuul.dto.dms.ListByDeptChangeParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "DmsMechanicItemRecordDistributionController", description = "医技工作台")
@RequestMapping("/DmsMechanicItemRecord")
@CrossOrigin(allowCredentials = "true")
public class DmsMechanicItemRecordDistributionController {

    @Autowired
    private ApiPcDmsMechanicItemRecordDistributionService apiPcDmsMechanicItemRecordDistributionService;

    @HystrixCommand(fallbackMethod = "listByDeptFallbackInfo")
    @ApiOperation(value = "根据科室id刷新患者列表")
    @RequestMapping(value = "/listByDept", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<CommonPage<DmsMechanicItemRecordResult>> listByDept(@RequestParam("deptId") Long deptId,
                                                                            @RequestParam("name") String name,
                                                                            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum){
        return  apiPcDmsMechanicItemRecordDistributionService.listByDept(deptId,name,pageSize,pageNum);
    }
    private CommonResult<CommonPage<DmsMechanicItemRecordResult>> listByDeptFallbackInfo(Long deptId, String name,Integer pageSize,Integer pageNum){
        return CommonResult.success(null,"请检查您的网络") ;
    }

    @HystrixCommand(fallbackMethod = "logFallbackInfo")
    @ApiOperation(value = "登记")
    @RequestMapping(value = "/log", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult log(@RequestParam("itemRecordId") Long itemRecordId, @RequestParam("logStaffId") Long logStaffId){
        System.err.println("itemRecordId:"+itemRecordId);
        System.err.println("logStaffId:"+logStaffId);

        return apiPcDmsMechanicItemRecordDistributionService.log(itemRecordId,logStaffId);
    }
    private CommonResult<List<DmsMechanicItemRecordResult>> logFallbackInfo( Long itemRecordId, Long logStaffId){
        return CommonResult.success(null,"请检查您的网络") ;
    }

    @HystrixCommand(fallbackMethod = "uploadResultFallbackInfo")
    @ApiOperation(value = "上传结果")
    @RequestMapping(value = "/uploadResult", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult uploadResult(@RequestParam("id") Long id,
                                     @RequestParam("executeStaffId") Long excuteStaffId,
                                     @RequestParam("checkResult") String checkResult,
                                     @RequestParam("resultImgUrlList") String resultImgUrlList){
        return apiPcDmsMechanicItemRecordDistributionService.uploadResult(id,excuteStaffId,checkResult,resultImgUrlList);
    }
    private CommonResult uploadResultFallbackInfo( Long id, Long excuteStaffId,String checkResult, String resultImgUrlList){
        return CommonResult.success(null,"请检查您的网络") ;
    }


    /**
     * 描述:收费员账号： 根据科室id刷新患者列表
     * <p>author: ma
     */
    @HystrixCommand(fallbackMethod = "listByDeptChangeFallbackInfo")
    @ApiOperation(value = "根据科室id刷新患者列表")
    @RequestMapping(value = "/listByDeptChange", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<CommonPage<DmsMechanicItemRecordResult>> listByDeptChange(@RequestBody ListByDeptChangeParam listByDeptChangeParam){
        return apiPcDmsMechanicItemRecordDistributionService.listByDeptChange(listByDeptChangeParam);
    }
    private CommonResult<CommonPage<DmsMechanicItemRecordResult>> listByDeptChangeFallbackInfo(ListByDeptChangeParam listByDeptChangeParam){
        return CommonResult.success(null,"请检查您的网络") ;
    }

}
