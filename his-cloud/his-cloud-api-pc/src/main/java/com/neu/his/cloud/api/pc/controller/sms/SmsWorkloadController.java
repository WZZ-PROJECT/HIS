package com.neu.his.cloud.api.pc.controller.sms;

import com.neu.his.cloud.api.pc.common.CommonPage;
import com.neu.his.cloud.api.pc.common.CommonResult;
import com.neu.his.cloud.api.pc.dto.sms.SmsWorkloadResult;
import com.neu.his.cloud.api.pc.service.sms.SmsWorkloadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@Api(tags = "SmsWorkloadController", description = "工作量统计")
@RequestMapping("/workload")
@CrossOrigin(allowCredentials = "true")
public class SmsWorkloadController {

    @Autowired
    private SmsWorkloadService smsWorkloadService;

    @ApiOperation("根据时间段查询某个人工作量")
    @RequestMapping(value = "/queryPersonal", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<SmsWorkloadResult> queryPersonalWorkload(@RequestParam("staffId") Long staffId,
                                                                 @RequestParam("startDatetime")String startDatetime,
                                                                 @RequestParam("endDatetime")String endDatetime){
        return smsWorkloadService.queryPersonalWorkload(staffId,startDatetime,endDatetime);
    }


    @ApiOperation("根据时间段统计某个科室工作量")
    @RequestMapping(value = "/queryDept", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<SmsWorkloadResult>  queryDeptWorkload(@RequestParam("deptId") Long deptId,
                                                              @RequestParam("startDatetime")String startDatetime,
                                                              @RequestParam("endDatetime")String endDatetime){
        return smsWorkloadService.queryDeptWorkload(deptId,startDatetime,endDatetime);
    }


    @ApiOperation("根据时间段统计所有科室工作量")
    @RequestMapping(value = "/queryDeptWorkloadList", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<List<SmsWorkloadResult>> queryDeptWorkloadList(@RequestParam("startDatetime")String startDatetime,
                                                                        @RequestParam("endDatetime") String endDatetime){
        return smsWorkloadService.queryDeptWorkloadList(startDatetime,endDatetime);
    }


    @ApiOperation("根据时间段统计科室所有人工作量(一个科室所有人)")
    @RequestMapping(value = "/queryDeptPersonalWorkloadList", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<CommonPage<SmsWorkloadResult>>  queryDeptPersonalWorkloadList(@RequestParam("deptId") Long deptId,
                                                                                      @RequestParam("startDatetime") String startDatetime,
                                                                                      @RequestParam("endDatetime") String endDatetime,
                                                                                      @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                                                      @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum){
        return smsWorkloadService.queryDeptPersonalWorkloadList(deptId,startDatetime,endDatetime,pageSize,pageNum);
    }

}
