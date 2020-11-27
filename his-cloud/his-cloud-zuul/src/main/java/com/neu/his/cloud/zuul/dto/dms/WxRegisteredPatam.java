package com.neu.his.cloud.zuul.dto.dms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

@Setter
@Getter
@ToString
public class WxRegisteredPatam implements Serializable {

    @ApiModelProperty(value = "排班id")
    private Long skdId;

    @ApiModelProperty(value = "科室id")
    private Long deptId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "就诊日期")
    private Date attendanceDate;

    @ApiModelProperty(value = "病人ID")
    private String patientId;

    @ApiModelProperty(value = "应收金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "选择的时间段")
    private String time;

    @ApiModelProperty(value = "商户订单号")
    private String  out_trade_no;

    @ApiModelProperty(value = "用户标识")
    private String  openid;

    @ApiModelProperty(value = "微信支付结果")
    private Map<String ,String> results;

}
