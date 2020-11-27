package com.neu.his.cloud.service.pms.dto.pms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@ToString
public class PmsPatients implements Serializable {
    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "病人名称")
    private String name;

    @ApiModelProperty(value = "出生日期")
    private Date dateOfBirth;

    @ApiModelProperty(value = "身份证号码")
    private String identificationNo;

    @ApiModelProperty(value = "家庭住址")
    private String homeAddress;

    @ApiModelProperty(value = "联系方式")
    private String phoneNo;

    @ApiModelProperty(value = "性别")
    private Integer gender;

    @ApiModelProperty(value = "医疗记录编号")
    private String medicalRecordNo;

    @ApiModelProperty(value = "医保卡")
    private String card;

    @ApiModelProperty(value = "微信唯一码")
    private String openId;

    @ApiModelProperty(value = "头像")
    private String picture;

    @ApiModelProperty(value = "token")
    private String token;
}
