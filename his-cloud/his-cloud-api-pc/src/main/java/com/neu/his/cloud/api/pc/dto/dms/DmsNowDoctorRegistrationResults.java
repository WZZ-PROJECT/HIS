package com.neu.his.cloud.api.pc.dto.dms;

import com.neu.his.cloud.api.pc.model.SmsStaff;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@ToString
public class DmsNowDoctorRegistrationResults implements Serializable {
    @ApiModelProperty("预约这科室的医生")
    List<DmsNowDoctorRegistrationResult> DmsNowDoctorRegistrationResultList;
    @ApiModelProperty("科室的有医生")
    List<SmsStaff> stafflist;
}
