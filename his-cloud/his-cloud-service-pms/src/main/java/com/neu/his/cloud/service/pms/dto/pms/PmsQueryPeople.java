package com.neu.his.cloud.service.pms.dto.pms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Setter
@Getter
@ToString
public class PmsQueryPeople {

    @ApiModelProperty(value = "开始日期")
    private String  startDate;

    @ApiModelProperty(value = "结束日期")
    private String  endDate;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "身份证号")
    private String cardId;

    @ApiModelProperty(value = "科室Id")
    private Long deptId;
}
