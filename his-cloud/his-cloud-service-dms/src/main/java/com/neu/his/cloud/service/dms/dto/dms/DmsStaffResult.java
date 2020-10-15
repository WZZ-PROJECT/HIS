package com.neu.his.cloud.service.dms.dto.dms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@ToString
public class DmsStaffResult implements Serializable {

    @ApiModelProperty(value = "医生Id")
    private Long staffId;
    @ApiModelProperty(value = "医生姓名")
    private String staffName;
    @ApiModelProperty(value = "科室Id")
    private Long deptId;
    @ApiModelProperty(value = "科室名字")
    private String deptName;
    @ApiModelProperty(value = "医生级别title")
    private String title;
    @ApiModelProperty(value = "医生特长")
    private String advantages;
    @ApiModelProperty(value = "医生介绍")
    private String description;
    @ApiModelProperty(value = "头像")
    private String picture;

    @ApiModelProperty(value = "是否关注")
    private Long state;

    @ApiModelProperty(value = "排班信息")
    private List<SmsSkdResult> skdList;
}

