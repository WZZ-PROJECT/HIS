package com.neu.his.cloud.zuul.dto.sms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Setter
@Getter
@ToString
public class InformParam implements Serializable {

    @ApiModelProperty(value = "常用项名字")
    private String name;

    @ApiModelProperty(value = "常用项内容")
    private String content;

    @ApiModelProperty(value = "分页")
    private int pageSize;

    @ApiModelProperty(value = "分页")
    private int pageNum;
}
