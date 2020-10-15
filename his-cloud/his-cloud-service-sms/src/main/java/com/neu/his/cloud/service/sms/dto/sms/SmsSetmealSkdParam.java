package com.neu.his.cloud.service.sms.dto.sms;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.neu.his.cloud.service.sms.common.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@ToString
public class SmsSetmealSkdParam extends Page implements Serializable {

    @ApiModelProperty(value = "套餐名称")
    private String nonDrugName;
    @ApiModelProperty(value = "套餐id")
    private Long nonDrugId;
    @ApiModelProperty(value = "起始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startDate;
    @ApiModelProperty(value = "截止时间")
    private Date endDate;
    @ApiModelProperty(value = "午别")
    private Integer noon;
    @ApiModelProperty(value = "科室id")
    private Long deptId;
}
