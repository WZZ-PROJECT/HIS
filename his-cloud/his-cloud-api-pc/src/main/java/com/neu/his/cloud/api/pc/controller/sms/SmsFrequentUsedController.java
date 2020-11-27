package com.neu.his.cloud.api.pc.controller.sms;

import com.neu.his.cloud.api.pc.common.CommonPage;
import com.neu.his.cloud.api.pc.common.CommonResult;
import com.neu.his.cloud.api.pc.dto.sms.AddInformParam;
import com.neu.his.cloud.api.pc.dto.sms.FamiliarInformTemplate;
import com.neu.his.cloud.api.pc.dto.sms.InformParam;
import com.neu.his.cloud.api.pc.dto.sms.SmsFrequentUsedResult;
import com.neu.his.cloud.api.pc.service.sms.SmsFrequentUsedService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "SmsFrequentUsedController", description = "常用项管理")
@RequestMapping("/frequentUsed")
@CrossOrigin(allowCredentials = "true")
public class SmsFrequentUsedController {

    @Autowired
    private SmsFrequentUsedService smsFrequentUsedService;

    @ApiOperation("新增常用项")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult addFrequent(@RequestParam("staffId") Long staffId,
                                    @RequestParam("addType") int addType,
                                    @RequestParam("addId") Long addId){
       return  smsFrequentUsedService.addFrequent(staffId,addType,addId);
    }

    @ApiOperation("删除常用项")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult deleteFrequent(@RequestParam("staffId") Long staffId,
                               @RequestParam("deleteType") int deleteType,
                               @RequestParam("deleteId") Long deleteId){
       return  smsFrequentUsedService.deleteFrequent(staffId,deleteType,deleteId);
    }

    @ApiOperation("查询指定常用项")
    @RequestMapping(value = "/selectByType", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<SmsFrequentUsedResult> selectFrequent(@RequestParam("staffId") Long staffId,
                                                              @RequestParam("selectType") int selectType){
       return  smsFrequentUsedService.selectFrequent(staffId,selectType);
    }

    @ApiOperation("新增知情告知")
    @RequestMapping(value = "/addInform", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult addInform(@RequestBody AddInformParam addInformParam){
        return smsFrequentUsedService.addInform(addInformParam);
    }

    @ApiOperation("删除知情告知")
    @RequestMapping(value = "/deleteInform", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult deleteInform(@RequestParam("frequentId") Long frequentId){
        return  smsFrequentUsedService.deleteInform(frequentId);
    }

    @ApiOperation("修改知情告知")
    @RequestMapping(value = "/updateInform", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateInform(@RequestBody AddInformParam addInformParam){
        return  smsFrequentUsedService.updateInform(addInformParam);
    }

    @ApiOperation("修改知情告知")
    @RequestMapping(value = "/selectInform", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<CommonPage<FamiliarInformTemplate>> selectInform(@RequestBody InformParam informParam){
        return  smsFrequentUsedService.selectInform(informParam);
    }


}
