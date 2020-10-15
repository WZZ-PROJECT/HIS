package com.neu.his.cloud.zuul.dto.sms;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.neu.his.cloud.zuul.common.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@ToString
public class SmsSkdParam extends Page implements Serializable {

    @ApiModelProperty(value = "医生id")
    private Long staffId;
    @ApiModelProperty(value = "医生id")
    private String staffName;
    @ApiModelProperty(value = "科室id")
    private Long deptId;
    @ApiModelProperty(value = "起始时间")
    private String startDate;
    @ApiModelProperty(value = "截止时间")
    private Date endDate;
    @ApiModelProperty(value = "午别")
    private Integer noon;
    @ApiModelProperty(value = "挂号级别id")
    private Long registrationRankId;
}
