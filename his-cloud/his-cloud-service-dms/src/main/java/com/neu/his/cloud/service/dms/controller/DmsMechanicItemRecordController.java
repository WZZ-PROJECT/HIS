package com.neu.his.cloud.service.dms.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.util.StringUtil;
import com.neu.his.cloud.service.dms.common.CommonPage;
import com.neu.his.cloud.service.dms.common.CommonResult;
import com.neu.his.cloud.service.dms.dto.dms.DmsMechanicItemRecordResult;
import com.neu.his.cloud.service.dms.dto.dms.ListByDeptChangeParam;
import com.neu.his.cloud.service.dms.service.DmsMechanicItemRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@Api(tags = "DmsMechanicItemRecordController", description = "医技工作台管理")
@RequestMapping("/DmsMechanicItemRecord")
@CrossOrigin(allowCredentials = "true")
public class DmsMechanicItemRecordController {

    @Autowired
    private DmsMechanicItemRecordService dmsMechanicItemRecordService;

    /**
     * 描述:根据科室id刷新患者列表
     * <p>author: ma
     */
    @ApiOperation(value = "根据科室id刷新患者列表")
    @RequestMapping(value = "/listByDept", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<CommonPage<DmsMechanicItemRecordResult>> listByDept(@RequestParam("deptId") Long deptId,
                                                                            @RequestParam("name") String name,
                                                                      @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                                      @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum){

        List<DmsMechanicItemRecordResult> list = dmsMechanicItemRecordService.listByDept(deptId,name);
        Long pageTotal=new Long((long)list.size()) ;
        Stream<DmsMechanicItemRecordResult> streDatas = list.stream();
        Long skipNumber = (pageNum - 1) * pageSize.longValue();
        List<DmsMechanicItemRecordResult> collect = streDatas.skip(skipNumber).limit(pageSize).collect(Collectors.toList());
        return CommonResult.success(CommonPage.restPage(collect,pageTotal));
    }

    /**
     * 描述:收费员账号： 根据科室id刷新患者列表
     * <p>author: ma
     */
    @ApiOperation(value = "根据科室id刷新患者列表")
    @RequestMapping(value = "/listByDeptChange", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<CommonPage<DmsMechanicItemRecordResult>> listByDeptChange(@RequestBody ListByDeptChangeParam listByDeptChangeParam){

        List<DmsMechanicItemRecordResult> list = dmsMechanicItemRecordService.listByDeptChange(listByDeptChangeParam.getDeptId(),listByDeptChangeParam.getIdentificationNo());
        Long pageTotal=new Long((long)list.size()) ;
        Stream<DmsMechanicItemRecordResult> streDatas = list.stream();
        Long skipNumber = (listByDeptChangeParam.getPageNum() - 1) * listByDeptChangeParam.getPageSize().longValue();
        List<DmsMechanicItemRecordResult> collect = streDatas.skip(skipNumber).limit(listByDeptChangeParam.getPageSize()).collect(Collectors.toList());
        return CommonResult.success(CommonPage.restPage(collect,pageTotal));
    }
    /**
     * 描述:医技登记
     * <p>author: ma
     */
    @ApiOperation(value = "医技登记")
    @RequestMapping(value = "/log", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult log(@RequestParam("itemRecordId") Long itemRecordId, @RequestParam("logStaffId") Long logStaffId){
        int count = dmsMechanicItemRecordService.log(itemRecordId,logStaffId);
        if(count > 0){
            return CommonResult.success(count, "登记成功");
        }
        return CommonResult.failed("登记失败");
    }

    /**
     * 描述:上传结果
     * <p>author: ma
     */
    @ApiOperation(value = "上传结果")
    @RequestMapping(value = "/uploadResult", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult uploadResult(@RequestParam("id") Long id,
                                     @RequestParam("executeStaffId") Long excuteStaffId,
                                     @RequestParam("checkResult") String checkResult,
                                     @RequestParam("resultImgUrlList") String resultImgUrlList){
        int count = dmsMechanicItemRecordService.uploadResult(id,excuteStaffId,checkResult,resultImgUrlList);
        if(count > 0){
            return CommonResult.success(count, "上传成功");
        }
        return CommonResult.failed("上传失败");
    }



}
