package com.neu.his.cloud.zuul.dto.dms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

@Setter
@Getter
@ToString
public class WxProgramResultsParam implements Serializable {

    @ApiModelProperty(value = "病人Id")
    private long patientid;

    @ApiModelProperty(value = "商户订单号")
    private String  out_trade_no;

    @ApiModelProperty(value = "用户标识")
    private String  openid;

    @ApiModelProperty(value = "微信支付结果")
    private Map<String ,String> results;


}
