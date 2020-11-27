package com.neu.his.cloud.zuul.controller.sms;


import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.neu.his.cloud.zuul.common.CommonResult;
import com.neu.his.cloud.zuul.distribution.api.pc.sms.ApiPcSmsFrequentUsedDistributionService;
import com.neu.his.cloud.zuul.dto.sms.AddInformParam;
import com.neu.his.cloud.zuul.dto.sms.FamiliarInformTemplate;
import com.neu.his.cloud.zuul.dto.sms.InformParam;
import com.neu.his.cloud.zuul.dto.sms.SmsFrequentUsedResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "SmsFrequentUsedDistributionController", description = "常用项管理")
@RequestMapping("/frequentUsed")
@CrossOrigin(allowCredentials = "true")
public class SmsFrequentUsedDistributionController {

    @Autowired
    private ApiPcSmsFrequentUsedDistributionService apiPcSmsFrequentUsedDistributionService;

    @HystrixCommand(fallbackMethod = "addFrequentFallbackInfo")
    @ApiOperation("新增常用项")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult addFrequent(@RequestParam("staffId") Long staffId,
                                    @RequestParam("addType") int addType,
                                    @RequestParam("addId") Long addId){
       return  apiPcSmsFrequentUsedDistributionService.addFrequent(staffId,addType,addId);
    }
    private CommonResult addFrequentFallbackInfo(Long staffId, int addType, Long addId){
        return CommonResult.success(null,"请检查您的网络") ;
    }

    @HystrixCommand(fallbackMethod = "deleteFrequentFallbackInfo")
    @ApiOperation("删除常用项")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult deleteFrequent(@RequestParam("staffId") Long staffId,
                               @RequestParam("deleteType") int deleteType,
                               @RequestParam("deleteId") Long deleteId){
       return  apiPcSmsFrequentUsedDistributionService.deleteFrequent(staffId,deleteType,deleteId);
    }
    private CommonResult deleteFrequentFallbackInfo(Long staffId, int deleteType, Long deleteId){
        return CommonResult.success(null,"请检查您的网络") ;
    }

    @HystrixCommand(fallbackMethod = "selectFrequentFallbackInfo")
    @ApiOperation("查询指定常用项")
    @RequestMapping(value = "/selectByType", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<SmsFrequentUsedResult> selectFrequent(@RequestParam("staffId") Long staffId,
                                                              @RequestParam("selectType") int selectType){
       return  apiPcSmsFrequentUsedDistributionService.selectFrequent(staffId,selectType);
    }
    private CommonResult<SmsFrequentUsedResult> selectFrequentFallbackInfo( Long staffId, int selectType){
        return CommonResult.success(null,"请检查您的网络") ;
    }

    @HystrixCommand(fallbackMethod = "addInformFallbackInfo")
    @ApiOperation("新增知情告知")
    @RequestMapping(value = "/addInform", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult addInform(@RequestBody AddInformParam addInformParam){
        return  apiPcSmsFrequentUsedDistributionService.addInform(addInformParam);
    }
    private CommonResult addInformFallbackInfo(AddInformParam addInformParam){
        return CommonResult.success(null,"请检查您的网络") ;
    }

    @HystrixCommand(fallbackMethod = "deleteInformFallbackInfo")
    @ApiOperation("删除知情告知")
    @RequestMapping(value = "/deleteInform", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult deleteInform(@RequestParam("frequentId") Long frequentId){
        return  apiPcSmsFrequentUsedDistributionService.deleteInform(frequentId);
    }
    private CommonResult deleteInformFallbackInfo(Long frequentId){
        return CommonResult.success(null,"请检查您的网络") ;
    }

    @HystrixCommand(fallbackMethod = "updateInformFallbackInfo")
    @ApiOperation("修改知情告知")
    @RequestMapping(value = "/updateInform", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateInform(@RequestBody AddInformParam addInformParam){
        return  apiPcSmsFrequentUsedDistributionService.updateInform(addInformParam);
    }
    private CommonResult updateInformFallbackInfo(AddInformParam addInformParam){
        return CommonResult.success(null,"请检查您的网络") ;
    }

    @HystrixCommand(fallbackMethod = "selectInformFallbackInfo")
    @ApiOperation("查询知情告知")
    @RequestMapping(value = "/selectInform", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult selectInform(@RequestBody InformParam informParam){
        return  apiPcSmsFrequentUsedDistributionService.selectInform(informParam);
    }
    private CommonResult selectInformFallbackInfo(InformParam informParam){
        return CommonResult.success(null,"请检查您的网络") ;
    }

}
