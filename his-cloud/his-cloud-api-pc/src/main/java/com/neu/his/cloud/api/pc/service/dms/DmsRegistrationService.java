package com.neu.his.cloud.api.pc.service.dms;

import com.neu.his.cloud.api.pc.common.CommonResult;
import com.neu.his.cloud.api.pc.dto.dms.DmsRegistrationParam;
import com.neu.his.cloud.api.pc.dto.dms.WXDmsRegistrationParam;
import com.neu.his.cloud.api.pc.dto.dms.WxProgramResultsParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@FeignClient(value = "his-cloud-service-dms" )
public interface DmsRegistrationService {



    @RequestMapping(value = "/registration/createRegistration", method = RequestMethod.POST)
    CommonResult createRegistration(@RequestBody WXDmsRegistrationParam wxDmsRegistrationParam);

    /**
     * 描述：时间间隔
     */
    @RequestMapping(value = "/registration/TimeDifference", method = RequestMethod.GET)
    CommonResult<List<String>> TimeDifference(@RequestParam String ruletime,@RequestParam Long skdId,@RequestParam int noon);

    /**
     * 描述：充值
     */
    @RequestMapping(value = "/registration/recharge", method = RequestMethod.POST)
    CommonResult recharge(@RequestBody WXDmsRegistrationParam wxDmsRegistrationParam);

    /**
     * 描述：退费
     */
    @RequestMapping(value = "/registration/rollback", method = RequestMethod.POST)
    CommonResult rollback(@RequestBody WXDmsRegistrationParam wxDmsRegistrationParam);

    /**
     * 描述：小程序挂号
     */
    @RequestMapping(value = "/registration/programCreateRegistration", method = RequestMethod.POST)
    CommonResult programCreateRegistration(@RequestBody WXDmsRegistrationParam wxDmsRegistrationParam);

    /**
     * 描述：小程序充值
     */
    @RequestMapping(value = "/registration/WxProgramResults", method = RequestMethod.POST)
    CommonResult WxProgramResults(@RequestBody WxProgramResultsParam wxProgramResultsParam);

}
