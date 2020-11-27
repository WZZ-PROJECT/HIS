package com.neu.his.cloud.service.pms.dto.pms;


import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Setter
@Getter
@ToString
public class PmsDiagnosisPatientResult {
    @ApiModelProperty(value = "病人Id")
    Long patientId;
    @ApiModelProperty(value = "病人姓名")
    String patientName;
    @ApiModelProperty(value = "病人年龄")
    String patientAge;
    @ApiModelProperty(value = "病人家庭住址")
    String patientHomeAdress;
    @ApiModelProperty(value = "病人性别")
    Integer patientGender;
    @ApiModelProperty(value = "病人病历号")
    String patientMedicalRecordNo;
    @ApiModelProperty(value = "就诊号(挂号号)")
    Long registrationId;
    @ApiModelProperty(value = "就诊状态")
    Integer registrationStatus;
    @ApiModelProperty(value = "看诊日期")
    Date attendanceDate;
    @ApiModelProperty(value = "手机号")
    String phoneNo;
    @ApiModelProperty(value = "身份证号")
    String identificationNo;

}
