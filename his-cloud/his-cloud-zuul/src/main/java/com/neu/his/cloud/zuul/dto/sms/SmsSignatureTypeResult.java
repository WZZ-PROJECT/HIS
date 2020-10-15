package com.neu.his.cloud.zuul.dto.sms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Setter
@Getter
@ToString
public class SmsSignatureTypeResult implements Serializable {
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "签章类型名称")
    private String name;

    @ApiModelProperty(value = "签章类型code")
    private String code;

    @ApiModelProperty(value = "签章类型状态")
    private Integer status;
}