package com.neu.his.cloud.api.pc.dto.bms;


import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class BmsParam {

    @ApiModelProperty(value = "openId")
    String openId;
    @ApiModelProperty(value = "病人Id")
    Long patientId;
}
