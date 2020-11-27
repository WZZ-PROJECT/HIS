package com.neu.his.cloud.service.dms.dto.dms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

@Setter
@Getter
@ToString
public class RefundResultsParam implements Serializable {

    @ApiModelProperty(value = "现金退款金额")
    private BigDecimal cash;

    @ApiModelProperty(value = "银行卡退款金额")
    private BigDecimal bankCard;

    @ApiModelProperty(value = "微信退款金额")
    private BigDecimal weChat;
}
