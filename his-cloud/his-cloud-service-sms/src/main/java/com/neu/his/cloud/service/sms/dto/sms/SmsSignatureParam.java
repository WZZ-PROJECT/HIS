package com.neu.his.cloud.service.sms.dto.sms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Setter
@Getter
@ToString
public class SmsSignatureParam implements Serializable {

    @ApiModelProperty(value = "签章编码")
    private String code;

    @ApiModelProperty(value = "签章名称")
    private String name;

    @ApiModelProperty(value = "签章类型:0:财务专用章")
    private Integer type;

    @ApiModelProperty(value = "签章状态: 1启用 2禁用")
    private Integer state;


    private static final long serialVersionUID = 1L;

}