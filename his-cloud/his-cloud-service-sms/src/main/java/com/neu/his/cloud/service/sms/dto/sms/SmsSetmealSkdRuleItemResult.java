package com.neu.his.cloud.service.sms.dto.sms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SmsSetmealSkdRuleItemResult {

    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "医生id")
    private Long nonDrugId;
    @ApiModelProperty(value = "医生name")
    private String nonDrugName;
    @ApiModelProperty(value = "一周中的排班时间")
    private String daysOfWeek;
    @ApiModelProperty(value = "状态")
    private Integer status;
    @ApiModelProperty(value = "挂号限额")
    private Long skLimit;
    @ApiModelProperty(value = "排版规则id")
    private Long skRuleId;
}
