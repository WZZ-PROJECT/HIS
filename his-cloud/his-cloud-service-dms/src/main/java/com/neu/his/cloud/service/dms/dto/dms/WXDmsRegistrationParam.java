package com.neu.his.cloud.service.dms.dto.dms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
@ToString
public class WXDmsRegistrationParam implements Serializable {

    @ApiModelProperty(value = "排班id")
    private Long skdId;

    @ApiModelProperty(value = "是否需要病历本")
    private Integer needBook;

    @ApiModelProperty(value = "科室id")
    private Long deptId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "就诊日期")
    private Date attendanceDate;

    @ApiModelProperty(value = "姓名")
    private String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "出生日期")
    private Date dateOfBirth;

    @ApiModelProperty(value = "身份证号")
    private String identificationNo;

    @ApiModelProperty(value = "家庭住址")
    private String homeAddress;

    @ApiModelProperty(value = "电话号码")
    private String phoneNo;

    @ApiModelProperty(value = "性别")
    private Integer gender;

    @ApiModelProperty(value = "病历号")
    private String medicalRecordNo;

    @ApiModelProperty(value = "结算类别id")
    private Long settlementCatId;

    @ApiModelProperty(value = "应收金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "发票号")
    private Long invoiceNo;

    @ApiModelProperty(value = "操作员id")
    private Long opratorId;

    @ApiModelProperty(value = "选择的时间段")
    private String time;

    @ApiModelProperty(value = "付款码")
    private String barCode;

    @ApiModelProperty(value = "总金额")
    private BigDecimal summery;

    @ApiModelProperty(value = "可用金额")
    private BigDecimal blance;

    @ApiModelProperty(value = "冻结金额")
    private BigDecimal frozen;

    @ApiModelProperty(value = "充值金额")
    private BigDecimal topUp;

    @ApiModelProperty(value = "退费金额")
    private BigDecimal refund;

}
