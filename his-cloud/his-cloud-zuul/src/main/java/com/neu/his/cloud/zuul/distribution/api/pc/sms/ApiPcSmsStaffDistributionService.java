package com.neu.his.cloud.zuul.distribution.api.pc.sms;

import com.neu.his.cloud.zuul.common.CommonPage;
import com.neu.his.cloud.zuul.common.CommonResult;
import com.neu.his.cloud.zuul.dto.sms.SmsSkdDocParam;
import com.neu.his.cloud.zuul.dto.sms.SmsSkdDocResult;
import com.neu.his.cloud.zuul.dto.sms.SmsStaffParam;
import com.neu.his.cloud.zuul.dto.sms.SmsStaffResult;
import com.neu.his.cloud.zuul.model.PmsPatient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "his-cloud-api-pc")
public interface ApiPcSmsStaffDistributionService {


    @RequestMapping(value = "/staff/listDocBySkd", method = RequestMethod.POST)
    CommonResult<List<SmsSkdDocResult>> listDocBySkd(@RequestBody SmsSkdDocParam smsSkdDocParam);

    @RequestMapping(value = "/staff/delete", method = RequestMethod.POST)
    CommonResult delete(@RequestParam("ids") List<Long> ids);


    @RequestMapping(value = "/staff/update/{id}", method = RequestMethod.POST)
    CommonResult update(@PathVariable Long id, @RequestBody SmsStaffParam smsStaffParam);


    @RequestMapping(value = "/staff/select", method = RequestMethod.POST)
    CommonResult<CommonPage<SmsStaffResult>> list(@RequestBody SmsStaffParam queryParam);

    @RequestMapping(value = "/staff/selectAll", method = RequestMethod.GET)
    CommonResult<List<SmsStaffResult>> listAll();

    /**
     * 描述：将患者和微信号绑定（更新Patient表的openID字段）
     */
    @RequestMapping(value = "/staff/bindPatient", method = RequestMethod.POST)
    CommonResult bindPatient(@RequestBody PmsPatient pmsPatient);
}
