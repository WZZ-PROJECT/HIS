package com.neu.his.cloud.zuul.distribution.api.pc.bms;


import com.neu.his.cloud.zuul.common.CommonPage;
import com.neu.his.cloud.zuul.common.CommonResult;
import com.neu.his.cloud.zuul.dto.bms.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@FeignClient(value = "his-cloud-api-pc")
public interface ApiPcBmsFeeDistributionService {


    @RequestMapping(value = "/fee/listRegisteredPatient", method = RequestMethod.GET)
    CommonResult<CommonPage<BmsRegistrationPatientResult>> listRegisteredPatient(@RequestParam(required=false,name = "medicalRecordNo") String  medicalRecordNo,
                                                                                        @RequestParam(required=false,name="queryDate")@DateTimeFormat(pattern = "yyyy-MM-dd") Date queryDate,
                                                                                        @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                                                        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum);
    @RequestMapping(value = "/fee/charge", method = RequestMethod.POST)
    CommonResult charge(@RequestBody List<BmsChargeParam> bmsChargeParamList);

    @RequestMapping(value = "/fee/listRefundByRegistrationId", method = RequestMethod.GET)
    CommonResult<List<BmsRefundChargeResult>>  listRefundByRegistrationId(@RequestParam("registrationId") Long registrationId);


    @RequestMapping(value = "/fee/refundCharge", method = RequestMethod.POST)
    CommonResult refundCharge(@RequestBody List<BmsRefundChargeParam> bmsRefundChargeParamList);


    @RequestMapping(value = "/fee/refundRegistrationCharge", method = RequestMethod.POST)
    CommonResult refundRegistrationCharge(@RequestBody BmsRefundRegChargeParam bmsRefundRegChargeParam);


    @RequestMapping(value = "/fee/listChargeByRegistrationId", method = RequestMethod.GET)
    CommonResult<List<BmsChargeResult>>  listChargeByRegistrationId(@RequestParam("registrationId") Long registrationId);

    @ApiOperation(value = "根据患者id查询所有项目")
    @RequestMapping(value = "/fee/listCharge", method = RequestMethod.POST)
    @ResponseBody
    CommonResult<List<BmsResult>>  listCharge(@RequestBody BmsParam bmsParam);
}
