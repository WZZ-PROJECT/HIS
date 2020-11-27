package com.neu.his.cloud.api.pc.service.bms;

import com.neu.his.cloud.api.pc.common.CommonPage;
import com.neu.his.cloud.api.pc.common.CommonResult;
import com.neu.his.cloud.api.pc.dto.bms.BmsInvoiceParam;
import com.neu.his.cloud.api.pc.dto.bms.BmsInvoiceResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@FeignClient(value = "his-cloud-service-bms")
public interface BmsInvoiceService {

    @RequestMapping(value = "/invoice/supplementPrintInvoice", method = RequestMethod.GET)
    CommonResult supplementPrintInvoice(@RequestParam("newInvoiceNo") Long newInvoiceNo, @RequestParam("registrationId") Long registrationId);

    @RequestMapping(value = "/invoice/reprintInvoice", method = RequestMethod.GET)
    CommonResult  reprintInvoice(@RequestParam("newInvoiceNo") Long newInvoiceNo, @RequestParam("oldInvoiceNo") Long oldInvoiceNo);


    @RequestMapping(value = "/invoice/queryInvoiceInfo", method = RequestMethod.POST)
    CommonResult<CommonPage<BmsInvoiceResult>>  queryInvoiceInfo(@RequestBody BmsInvoiceParam bmsInvoiceParam);


    @RequestMapping(value = "/invoice/queryInvoiceInfoBySettleId", method = RequestMethod.GET)
    CommonResult<List<BmsInvoiceResult>>  queryInvoiceInfoBySettleId(@RequestParam("settleRecordId") Long settleRecordId);
}
