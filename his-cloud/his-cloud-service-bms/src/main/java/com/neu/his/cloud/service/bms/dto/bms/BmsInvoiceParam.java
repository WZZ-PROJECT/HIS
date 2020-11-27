package com.neu.his.cloud.service.bms.dto.bms;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.neu.his.cloud.service.bms.common.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@ToString
public class BmsInvoiceParam extends Page implements Serializable {
    @ApiModelProperty(value = "截止时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(  pattern = "yyyy-MM-dd HH:mm:ss")
    Date endDatetime;
    @ApiModelProperty(value = "起始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(  pattern = "yyyy-MM-dd HH:mm:ss")
    Date startDatetime;
    @ApiModelProperty(value = "收费员ID")
    Long cashierId;


}
