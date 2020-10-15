package com.neu.his.cloud.service.sms.dto.sms;


import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SmsSetmealSkdRuleItemParam {

    @ApiModelProperty(value = "医疗套餐ID")
    private Long nonDrugId;

    private String daysOfWeek;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "排班限额")
    private Long skLimit;

    @ApiModelProperty(value = "排班规则ID")
    private Long skRuleId;
}
