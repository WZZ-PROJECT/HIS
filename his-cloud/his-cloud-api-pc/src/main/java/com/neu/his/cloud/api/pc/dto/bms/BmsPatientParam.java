package com.neu.his.cloud.api.pc.dto.bms;

import com.neu.his.cloud.api.pc.common.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class BmsPatientParam extends Page {

    @ApiModelProperty(value = "病历号")
    String medicalRecordNo;

}
