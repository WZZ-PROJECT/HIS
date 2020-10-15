package com.neu.his.cloud.api.pc.dto.bms;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.neu.his.cloud.api.pc.model.DmsDrug;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@ToString
public class BmsResult {

    @ApiModelProperty(value = "缴费项id")
    Long id;
    @ApiModelProperty(value = "缴费项名称")
    String name;
    @ApiModelProperty(value = "总金额")
    BigDecimal amount;
    @ApiModelProperty(value = "类型：0 挂号 1检查 2检验 3处置 4草药 5 成药")
    Integer type;
    @ApiModelProperty(value = "状态")
    Integer status;

    @ApiModelProperty(value = "开立时间")
    String createTime;
    @ApiModelProperty(value = "药品")
    List<DmsDrug> dmsDrug;

    @ApiModelProperty(value = "病历号")
    String medicalRecordNo;

    @ApiModelProperty(value = "看诊时间")
    String attendanceDate;
}
