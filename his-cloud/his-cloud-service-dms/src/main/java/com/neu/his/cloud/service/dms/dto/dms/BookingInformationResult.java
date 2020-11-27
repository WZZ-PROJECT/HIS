package com.neu.his.cloud.service.dms.dto.dms;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

@Setter
@Getter
@ToString
public class BookingInformationResult implements Serializable {

    @ApiModelProperty(value = "Id" )
    private Long Id;

    @ApiModelProperty(value = "科室Id" )
    private Long deptId;
    @ApiModelProperty(value = "科室名字" )
    private String deptName;

    @ApiModelProperty(value = "医生Id" )
    private Long staffId;
    @ApiModelProperty(value = "医生名字" )
    private String staffName;
    @ApiModelProperty(value = "医生介绍" )
    private String description;
    @ApiModelProperty(value = "头像" )
    private String picture;
    @ApiModelProperty(value = "病人Id" )
    private Long patientId;
    @ApiModelProperty(value = "病人名字" )
    private String patientName;

    @ApiModelProperty(value = "诊费" )
    private BigDecimal amount;

    @ApiModelProperty(value = "就诊时间" )
    private String date;

    @ApiModelProperty(value = "是否预约1/0:预约/未预约" )
    private Integer status;

    @ApiModelProperty(value = "系统当前时间" )
    private String currentTime;


}
