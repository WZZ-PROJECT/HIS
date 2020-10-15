package com.neu.his.cloud.api.pc.dto.sms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@ToString
public class SmsSetMealSkdResult implements Serializable {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "时间")
    private Date date;

    @ApiModelProperty(value = "午别")
    private Integer noon;

    @ApiModelProperty(value = "科室名")
    private String deptName;

    @ApiModelProperty(value = "套餐名称")
    private String nonDrugName;

    @ApiModelProperty(value = "排班限额")
    private Long skLimit;

    @ApiModelProperty(value = "剩余号数")
    private Long remain;

}
