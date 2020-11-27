package com.neu.his.cloud.api.pc.service.dms;

import com.neu.his.cloud.api.pc.common.CommonResult;
import com.neu.his.cloud.api.pc.dto.dms.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@FeignClient(value = "his-cloud-service-dms" )
public interface DmsRegistrationService {

    /**
     * 描述：挂号
     */

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
    CommonResult programCreateRegistration(@RequestBody WxRegisteredPatam wxRegisteredPatam);

    /**
     * 描述：小程序充值
     */
    @RequestMapping(value = "/registration/WxProgramResults", method = RequestMethod.POST)
    CommonResult WxProgramResults(@RequestBody WxProgramResultsParam wxProgramResultsParam);


    /**
     * 描述：判断是否有账户
     */
    @RequestMapping(value = "/registration/isAccount", method = RequestMethod.POST)
    CommonResult isAccount(@RequestParam String identificationNo);

    /**
     * 描述：修改病人信息
     */
    @RequestMapping(value = "/registration/updateInformation", method = RequestMethod.POST)
    CommonResult updateInformation(@RequestBody WXDmsRegistrationParam wxDmsRegistrationParam);

    /**
     * 描述：获得各个方式的退款金额
     */
    @RequestMapping(value = "/registration/selectRefundResultsParam", method = RequestMethod.POST)
    CommonResult<RefundResultsParam> selectRefundResultsParam(@RequestBody WXDmsRegistrationParam wxDmsRegistrationParam);

    /**
     * 同一患者在同一午别，未就诊情况下应不能重复挂同一医生
     * @param wxDmsRegistrationParam
     * @return
     */
    @RequestMapping(value = "/registration/queryCreateRegistration", method = RequestMethod.POST)
    CommonResult queryCreateRegistration(WXDmsRegistrationParam wxDmsRegistrationParam);
}
