package com.neu.his.cloud.api.pc.dto.sms;


import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@ToString
public class SmsSetmealSkdRuleParam {

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "操作人ID")
    private Long operatorId;

    @ApiModelProperty(value = "操作时间")
    private Date operateTime;

    @ApiModelProperty(value = "规则名称")
    private String ruleName;

    @ApiModelProperty(value = "规则描述")
    private String description;

    @ApiModelProperty(value = "科室ID")
    private Long deptId;

    @ApiModelProperty(value = "排班项")
    private List<SmsSetmealSkdRuleItemParam> smsSetmealSkdRuleItemParams;
}
