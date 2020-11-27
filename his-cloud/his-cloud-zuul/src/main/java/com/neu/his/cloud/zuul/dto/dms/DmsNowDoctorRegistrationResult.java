package com.neu.his.cloud.zuul.dto.dms;

import com.neu.his.cloud.zuul.model.SmsStaff;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@ToString
public class DmsNowDoctorRegistrationResult implements Serializable {
    @ApiModelProperty("医生Id")
    private Long staffId;
    @ApiModelProperty("医生名字")
    private String staffName;
    @ApiModelProperty(value = "头像")
    private String picture;

    @ApiModelProperty("科室Id")
    private Long deptId;
    @ApiModelProperty("科室名字")
    private String deptName;

    @ApiModelProperty("剩余号数")
    private Long remain;
    @ApiModelProperty("总的号数")
    private Long skLimit;

    @ApiModelProperty("挂号费用")
    private BigDecimal amount;
    @ApiModelProperty("医生特长")
    private String advantages;

    @ApiModelProperty("午别")
    private Integer noon;

    @ApiModelProperty("备注")
    private String title;

    @ApiModelProperty("skdId")
    private Long skdId;

    @ApiModelProperty("医生介绍")
    private String description;

    @ApiModelProperty("科室的医生")
    List<SmsStaff> stafflist;
}
