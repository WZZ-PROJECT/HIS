package com.neu.his.cloud.api.pc.dto.bms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Setter
@Getter
@ToString
public class BmsSettlementCatResult  implements Serializable {
    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "结算类别名")
    private String name;
//    @ApiModelProperty(value = "比例")
//    private BigDecimal ratio;
    @ApiModelProperty(value = "状态：0->无效;1->有效")
    private Integer status;
    @ApiModelProperty(value = "编码")
    private String code;
}
