package com.neu.his.cloud.service.sms.controller;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.neu.his.cloud.service.sms.common.CommonPage;
import com.neu.his.cloud.service.sms.common.CommonResult;
import com.neu.his.cloud.service.sms.dto.sms.SmsDeptResult;
import com.neu.his.cloud.service.sms.dto.sms.SmsWorkloadResult;
import com.neu.his.cloud.service.sms.service.SmsWorkloadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@Api(tags = "SmsWorkloadController", description = "工作量统计")
@RequestMapping("/workload")
@CrossOrigin(allowCredentials = "true")
public class SmsWorkloadController {

    @Autowired
    private SmsWorkloadService smsWorkloadService;

    /**
     * 描述：根据时间段查询某个人工作量
     * <p>author: ma
     */
    @ApiOperation("根据时间段查询某个人工作量")
    @RequestMapping(value = "/queryPersonal", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<SmsWorkloadResult> queryPersonalWorkload(@RequestParam("staffId") Long staffId,
                                                                 @RequestParam("startDatetime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")Date startDatetime,
                                                                 @RequestParam("endDatetime")@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endDatetime){
        return CommonResult.success(smsWorkloadService.queryPersonalWorkloadPeriod(staffId,startDatetime,endDatetime));
    }

    /**
     * 描述：根据时间段统计某个科室工作量
     * <p>author: ma
     */
    @ApiOperation("根据时间段统计某个科室工作量")
    @RequestMapping(value = "/queryDept", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<SmsWorkloadResult>  queryDeptWorkload(@RequestParam("deptId") Long deptId,
                                                              @RequestParam("startDatetime")@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")Date startDatetime,
                                                              @RequestParam("endDatetime")@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endDatetime){
        return CommonResult.success(smsWorkloadService.queryDeptWorkloadPeriod(deptId,startDatetime,endDatetime));
    }

    /**
     * 描述:根据时间段统计所有科室工作量
     * <p>author: ma
     */
    @ApiOperation("根据时间段统计所有科室工作量")
    @RequestMapping(value = "/queryDeptWorkloadList", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<List<SmsWorkloadResult>> queryDeptWorkloadList(@RequestParam("startDatetime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")Date startDatetime,
                                                                       @RequestParam("endDatetime")@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endDatetime){
        return CommonResult.success(smsWorkloadService.queryDeptWorkloadList(startDatetime,endDatetime));
    }

    /**
     * 描述：根据时间段统计科室所有人工作量(一个科室所有人)
     * <p>author: ma
     */
    @ApiOperation("根据时间段统计科室所有人工作量(一个科室所有人)")
    @RequestMapping(value = "/queryDeptPersonalWorkloadList", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<CommonPage<SmsWorkloadResult>>  queryDeptPersonalWorkloadList(@RequestParam("deptId") Long deptId,
                                                                                @RequestParam("startDatetime")@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startDatetime,
                                                                                @RequestParam("endDatetime")@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endDatetime,
                                                                                @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                                                @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum){

        Page page = PageHelper.startPage(pageNum, pageSize);
        List<SmsWorkloadResult> list = smsWorkloadService.queryDeptPersonalWorkloadList(deptId,startDatetime,endDatetime);
        Long pageTotal=page.getTotal();
        return CommonResult.success(CommonPage.restPage(list,pageTotal));
    }

}
