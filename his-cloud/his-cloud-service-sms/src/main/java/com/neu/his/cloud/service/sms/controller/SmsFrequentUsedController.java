package com.neu.his.cloud.service.sms.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.neu.his.cloud.service.sms.common.CommonPage;
import com.neu.his.cloud.service.sms.common.CommonResult;
import com.neu.his.cloud.service.sms.dto.sms.AddInformParam;
import com.neu.his.cloud.service.sms.dto.sms.InformParam;
import com.neu.his.cloud.service.sms.dto.sms.SmsFrequentUsedResult;
import com.neu.his.cloud.service.sms.model.FamiliarInformTemplate;
import com.neu.his.cloud.service.sms.service.SmsFrequentUsedService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
            return CommonResult.success(1, "添加常用项成功");
        }
        return CommonResult.success(0,"添加常用项失败");
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

    /**
     * 描述:添加一个知情告知
     *
     */
    @ApiOperation("添加一个知情告知")
    @RequestMapping(value = "/addInform", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult addInform(@RequestBody AddInformParam addInformParam){
        int count = smsFrequentUsedService.addInform(addInformParam);
        if(count > 0){
            return CommonResult.success(count, "添加知情告知成功");
        }
        return CommonResult.success(count, "添加知情告知失败");
    }

    /**
     * 描述:删除一个知情告知
     *
     */
    @ApiOperation("删除知情告知")
    @RequestMapping(value = "/deleteInform", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult deleteInform(@RequestParam("frequentId") Long frequentId){
        int count = smsFrequentUsedService.deleteInform(frequentId);
        if(count > 0){
            return CommonResult.success(count, "删除知情告知成功");
        }
        return CommonResult.success(count, "删除知情告知失败");
    }

    /**
     * 描述:修改一个知情告知
     *
     */
    @ApiOperation("修改知情告知")
    @RequestMapping(value = "/updateInform", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateInform(@RequestBody AddInformParam addInformParam){
        int count = smsFrequentUsedService.updateInform(addInformParam);
        if(count > 0){
            return CommonResult.success(count, "修改知情告知成功");
        }
        return CommonResult.success(count, "修改知情告知失败");
    }

    /**
     * 描述:查询知情告知
     *
     */
    @ApiOperation("查询知情告知")
    @RequestMapping(value = "/selectInform", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<CommonPage<FamiliarInformTemplate>>  selectInform(@RequestBody InformParam informParam){
        Page page = PageHelper.startPage(informParam.getPageNum(),informParam.getPageSize());

        List<FamiliarInformTemplate> familiarInformTemplates = smsFrequentUsedService.selectInform(informParam);
        Long pageTotal=page.getTotal();
        if(!CollectionUtils.isEmpty(familiarInformTemplates)){
            return CommonResult.success(CommonPage.restPage(familiarInformTemplates,pageTotal));
        }
        return CommonResult.success(CommonPage.restPage(new ArrayList<>(),pageTotal));
    }


}
