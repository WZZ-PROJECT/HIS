package com.neu.his.cloud.zuul.dto.dms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Setter
@Getter
@ToString
public class SmsStaffRecord implements Serializable {
    @ApiModelProperty(value = "医生Id")
    private Long staffId;
    @ApiModelProperty(value = "医生名字")
    private String staffName;
    @ApiModelProperty(value = "部门Id")
    private Long   deptId;
    @ApiModelProperty(value = "部门名字")
    private String deptName;
    @ApiModelProperty(value = "头像")
    private String picture;
    @ApiModelProperty(value = "医生擅长")
    private String advantages;
    @ApiModelProperty(value = "医生介绍")
    private String description;
}
