package com.neu.his.cloud.zuul.dto.dms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@ToString
public class DmsDrugRedisParam implements Serializable {

    @ApiModelProperty(value = "名称" )
    private String name;

    @ApiModelProperty(value = "金额" )
    private BigDecimal amount;

    @ApiModelProperty(value = "状态" )
    private Integer status;

    @ApiModelProperty(value = "嘱托" )
    private String medicalAdvice;

    @ApiModelProperty(value = "治法" )
    private String therapy;

    @ApiModelProperty(value = "付数" )
    private Long pairNum;

    @ApiModelProperty(value = "用法" )
    private Integer usageMeans;

    @ApiModelProperty(value = "药品列表" )
    private List<DmsDrugItem> druglist;

}
