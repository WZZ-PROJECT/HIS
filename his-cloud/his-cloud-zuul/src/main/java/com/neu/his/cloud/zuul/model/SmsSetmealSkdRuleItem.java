package com.neu.his.cloud.zuul.model;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

public class SmsSetmealSkdRuleItem implements Serializable {
    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "医疗套餐ID")
    private Long nonDrugId;

    @ApiModelProperty(value = "一周中的排班时间")
    private String daysOfWeek;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "排班限额")
    private Long skLimit;

    @ApiModelProperty(value = "排班规则ID")
    private Long skRuleId;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNonDrugId() {
        return nonDrugId;
    }

    public void setNonDrugId(Long nonDrugId) {
        this.nonDrugId = nonDrugId;
    }

    public String getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(String daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getSkLimit() {
        return skLimit;
    }

    public void setSkLimit(Long skLimit) {
        this.skLimit = skLimit;
    }

    public Long getSkRuleId() {
        return skRuleId;
    }

    public void setSkRuleId(Long skRuleId) {
        this.skRuleId = skRuleId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", nonDrugId=").append(nonDrugId);
        sb.append(", daysOfWeek=").append(daysOfWeek);
        sb.append(", status=").append(status);
        sb.append(", skLimit=").append(skLimit);
        sb.append(", skRuleId=").append(skRuleId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}