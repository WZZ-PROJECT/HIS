package com.neu.his.cloud.zuul.distribution.api.pc.sms;


import com.neu.his.cloud.zuul.common.CommonPage;
import com.neu.his.cloud.zuul.common.CommonResult;
import com.neu.his.cloud.zuul.dto.sms.AddInformParam;
import com.neu.his.cloud.zuul.dto.sms.FamiliarInformTemplate;
import com.neu.his.cloud.zuul.dto.sms.InformParam;
import com.neu.his.cloud.zuul.dto.sms.SmsFrequentUsedResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "his-cloud-api-pc")
public interface ApiPcSmsFrequentUsedDistributionService {

    @RequestMapping(value = "/frequentUsed/add", method = RequestMethod.POST)
    CommonResult addFrequent(@RequestParam("staffId") Long staffId,
                                    @RequestParam("addType") int addType,
                                    @RequestParam("addId") Long addId);

    @RequestMapping(value = "/frequentUsed/delete", method = RequestMethod.POST)
    CommonResult deleteFrequent(@RequestParam("staffId") Long staffId,
                               @RequestParam("deleteType") int deleteType,
                               @RequestParam("deleteId") Long deleteId);


    @RequestMapping(value = "/frequentUsed/selectByType", method = RequestMethod.POST)
    CommonResult<SmsFrequentUsedResult> selectFrequent(@RequestParam("staffId") Long staffId,
                                                              @RequestParam("selectType") int selectType);

    //添加知情告知
    @RequestMapping(value = "/frequentUsed/addInform", method = RequestMethod.POST)
    public CommonResult addInform(@RequestBody AddInformParam addInformParam);

    //删除知情告知
    @RequestMapping(value = "/frequentUsed/deleteInform", method = RequestMethod.POST)
    public CommonResult deleteInform(@RequestParam("frequentId") Long frequentId);

    //修改知情告知
    @RequestMapping(value = "/frequentUsed/updateInform", method = RequestMethod.POST)
    public CommonResult updateInform(@RequestBody AddInformParam addInformParam);

    //查询知情告知
    @RequestMapping(value = "/frequentUsed/selectInform", method = RequestMethod.POST)
    public CommonResult<CommonPage<FamiliarInformTemplate>> selectInform(@RequestBody InformParam informParam);

}
