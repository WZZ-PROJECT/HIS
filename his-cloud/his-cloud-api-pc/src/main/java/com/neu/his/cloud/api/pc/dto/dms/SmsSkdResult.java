package com.neu.his.cloud.api.pc.dto.dms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

@Setter
@Getter
@ToString
public class SmsSkdResult implements Serializable {
    @ApiModelProperty(value = "日期")
    private String date;
    @ApiModelProperty(value = "星期几")
    private String week  ;
    @ApiModelProperty(value = "午别")
    private Integer noon;
    @ApiModelProperty(value = "剩余号数")
    private Integer remain;
    @ApiModelProperty(value = "挂号费用")
    private BigDecimal amount;
    @ApiModelProperty(value = "skdId")
    private Long skdId;

}
