package com.neu.his.cloud.zuul.dto.dms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

@Setter
@Getter
@ToString
public class WxProgramResultsParam implements Serializable {

    @ApiModelProperty(value = "时间戳")
    private String timeStamp;

    @ApiModelProperty(value = "32位随机字符串")
    private String nonceStr;

    @ApiModelProperty(value = "数据包")
    private String packages;

    @ApiModelProperty(value = "加密方式")
    private String signType;

    @ApiModelProperty(value = "签名")
    private String paySign;

    @ApiModelProperty(value = "充值金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "病人Id")
    private long patientid;

}
