package com.neu.his.cloud.api.pc.dto.sms;


import com.neu.his.cloud.api.pc.model.SmsDept;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Setter
@Getter
@ToString
public class SmsSetmealResult {

    @ApiModelProperty(value = "医疗套餐ID")
    private long id;
    @ApiModelProperty(value = "医疗套餐名称")
    private String name;
    @ApiModelProperty(value = "所在科室")
    private SmsDept dept;
    @ApiModelProperty(value = "状态")
    private Integer status;
    @ApiModelProperty(value = "创建时间")
    private Date createDate;
}
