package com.neu.his.cloud.service.dms.dto.dms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 挂号医生费用查询
 */
@Setter
@Getter
@ToString
public class DmsDoctorBillsResult {

    @ApiModelProperty(value = "医生Id")
    private Long staffId;
    @ApiModelProperty(value = "医生姓名")
    private String staffName;
    @ApiModelProperty(value = "头像")
    private String picture;
    @ApiModelProperty(value = "医生级别title")
    private String title;
    @ApiModelProperty(value = "医生特长")
    private String advantages;
    @ApiModelProperty(value = "医生介绍")
    private String description;

    @ApiModelProperty(value = "科室Id")
    private Long deptId;
    @ApiModelProperty(value = "科室名字")
    private String deptName;
    @ApiModelProperty(value = "金额")
    private BigDecimal amount;
    @ApiModelProperty(value="病人Id")
    private Long patientId;
    @ApiModelProperty(value="病人名字")
    private String patientName;
    @ApiModelProperty(value="医保卡号")
    private String card;

}
