package com.neu.his.cloud.service.sms.dto.sms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Setter
@Getter
@ToString
public class AddInformParam implements Serializable {

    @ApiModelProperty(value = "知情模板Id")
    private Long id;

    @ApiModelProperty(value = "知情模板名字")
    private String name;

    @ApiModelProperty(value = "知情模板内容")
    private String content;


}
