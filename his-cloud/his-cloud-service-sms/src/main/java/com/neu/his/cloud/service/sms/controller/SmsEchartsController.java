package com.neu.his.cloud.service.sms.controller;


import com.neu.his.cloud.service.sms.common.CommonResult;
import com.neu.his.cloud.service.sms.dto.sms.SmsDeptAmountStatisticsResult;
import com.neu.his.cloud.service.sms.dto.sms.SmsPatientsStatisticsResult;
import com.neu.his.cloud.service.sms.dto.sms.SmsStaffAmountStatisticsResult;
import com.neu.his.cloud.service.sms.service.SmsEchartsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "SmsEchartsController", description = "图表统计")
@RequestMapping("/charts")
@CrossOrigin(allowCredentials = "true")
public class SmsEchartsController {
    @Autowired
    private SmsEchartsService smsEchartsService;

    @ApiOperation(value = "统计个人每日接待患者人数（近7日）")
    @RequestMapping(value = "/staffPatients", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<SmsPatientsStatisticsResult> staffPatients(@RequestParam Long staffId) {
        SmsPatientsStatisticsResult smsPatientsStatisticsResult = smsEchartsService.staffPatients(staffId);
        return CommonResult.success(smsPatientsStatisticsResult);
    }

    @ApiOperation(value = "统计个人每日分类收费信息（近7日）")
    @RequestMapping(value = "/staffClassifyStatistics", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<SmsStaffAmountStatisticsResult> staffClassifyStatistics(@RequestParam Long staffId) {
        SmsStaffAmountStatisticsResult smsStaffAmountStatisticsResult = smsEchartsService.staffClassifyStatistics(staffId);
        return CommonResult.success(smsStaffAmountStatisticsResult);
    }

    @ApiOperation(value = "统计各科室每日接待患者人数（近7日）")
    @RequestMapping(value = "/deptPatients", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<SmsPatientsStatisticsResult> deptPatients(@RequestParam Long deptId) {
        SmsPatientsStatisticsResult smsPatientsStatisticsResult = smsEchartsService.deptPatients(deptId);
        return CommonResult.success(smsPatientsStatisticsResult);
    }

    @ApiOperation(value = "统计各科室每日分类收费信息（近7日）")
    @RequestMapping(value = "/deptClassifyStatistics", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<SmsDeptAmountStatisticsResult> deptClassifyStatistics(@RequestParam Long deptId) {
        SmsDeptAmountStatisticsResult smsDeptAmountStatisticsResult = smsEchartsService.deptClassifyStatistics(deptId);
        return CommonResult.success(smsDeptAmountStatisticsResult);
    }

    @ApiOperation(value = "统计全院每日接待患者人数（近7日）")
    @RequestMapping(value = "/totalClassifyStatistics", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<SmsDeptAmountStatisticsResult> totalClassifyStatistics() {
        SmsDeptAmountStatisticsResult smsDeptAmountStatisticsResult = smsEchartsService.totalClassifyStatistics();
        return CommonResult.success(smsDeptAmountStatisticsResult);
    }

    @ApiOperation(value = "统计全院每日分类收费信息（近7日）")
    @RequestMapping(value = "/totalPatients", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<SmsPatientsStatisticsResult> totalPatients() {
        SmsPatientsStatisticsResult smsPatientsStatisticsResult = smsEchartsService.totalPatients();
        return CommonResult.success(smsPatientsStatisticsResult);
    }
}
