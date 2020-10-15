package com.neu.his.cloud.api.pc.dto.sms;



import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 挂号医生费用查询
 */
@Setter
@Getter
@ToString
public class SmsStopFollowDoctorParam {

    @ApiModelProperty(value = "医生Id")
    private Long staffId;
    @ApiModelProperty(value="病人Id")
    private Long patientId;

}
