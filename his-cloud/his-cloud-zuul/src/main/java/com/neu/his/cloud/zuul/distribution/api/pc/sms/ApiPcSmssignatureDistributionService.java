package com.neu.his.cloud.zuul.distribution.api.pc.sms;

import com.neu.his.cloud.zuul.common.CommonPage;
import com.neu.his.cloud.zuul.common.CommonResult;
import com.neu.his.cloud.zuul.dto.sms.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "his-cloud-api-pc")
public interface ApiPcSmssignatureDistributionService {

    /**
     * 描述：添加签章
     * <p>author: ma
     */
    @ApiOperation("添加签章")
    @RequestMapping(value = "/signature/create", method = RequestMethod.POST)
    @ResponseBody
    CommonResult queryPersonalWorkload(@RequestBody SmsSignatureParam signatureParam);

    /**
     * 描述:根据ids删除签章
     * <p>author: ma
     */
    @ApiOperation("根据id删除签章")
    @RequestMapping(value = "/signature/delete", method = RequestMethod.POST)
    @ResponseBody
    CommonResult delete(@RequestParam("ids") List<Long> ids);

    /**
     * 描述:更新签章基本信息，启用禁用操作
     * <p>author: ma
     */
    @ApiOperation("更新签章信息")
    @RequestMapping(value = "/signature/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    CommonResult update(@PathVariable("id") Long id, @RequestBody SmsSignatureParam signatureParam);

    /**
     * 描述:模糊查询科室、且分页
     * <p>author: ma
     */
    @ApiOperation("模糊查询科室、且分页")
    @RequestMapping(value = "/signature/select", method = RequestMethod.POST)
    @ResponseBody
    CommonResult<CommonPage<SmsSignatureResult>> list(@RequestBody SmsSignatureParam signatureParam,
                                                      @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                      @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum);

    /**
     * 描述:查询正在应用的签章
     * <p>author: ma
     */
    @ApiOperation("查询正在应用的签章")
    @RequestMapping(value = "/signature/selectAll", method = RequestMethod.GET)
    @ResponseBody
    CommonResult<SmsSignatureResult> listAll();

    @ApiOperation("查询正在应用的签章类型")
    @RequestMapping(value = "/signature/selectType", method = RequestMethod.GET)
    @ResponseBody
    CommonResult<List<SmsSignatureTypeResult>> selectType();

}
