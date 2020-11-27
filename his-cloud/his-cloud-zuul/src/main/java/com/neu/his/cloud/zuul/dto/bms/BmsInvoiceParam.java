package com.neu.his.cloud.zuul.dto.bms;

import com.neu.his.cloud.zuul.common.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@ToString
public class BmsInvoiceParam extends Page implements Serializable {
    @ApiModelProperty(value = "截止时间")
    String endDatetime;
    @ApiModelProperty(value = "起始时间")
    String startDatetime;
    @ApiModelProperty(value = "收费员ID")
    Long cashierId;


}
