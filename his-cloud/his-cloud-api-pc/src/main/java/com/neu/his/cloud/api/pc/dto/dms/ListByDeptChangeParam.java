package com.neu.his.cloud.api.pc.dto.dms;


import com.neu.his.cloud.api.pc.common.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ListByDeptChangeParam extends Page {

    @ApiModelProperty(value = "科室Id")
    Long deptId;
    @ApiModelProperty(value = "身份证号")
    String identificationNo;
}
