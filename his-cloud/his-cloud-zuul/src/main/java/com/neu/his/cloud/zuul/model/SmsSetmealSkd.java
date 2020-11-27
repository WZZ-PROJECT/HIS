package com.neu.his.cloud.zuul.model;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

public class SmsSetmealSkd implements Serializable {
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "时间")
    private Date date;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "剩余号数")
    private Long remain;

    @ApiModelProperty(value = "午别")
    private Integer noon;

    @ApiModelProperty(value = "医疗套餐ID")
    private Long nonDrugId;

    @ApiModelProperty(value = "所属科室ID")
    private Long deptId;

    @ApiModelProperty(value = "排班限额")
    private Long skLimit;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getRemain() {
        return remain;
    }

    public void setRemain(Long remain) {
        this.remain = remain;
    }

    public Integer getNoon() {
        return noon;
    }

    public void setNoon(Integer noon) {
        this.noon = noon;
    }

    public Long getNonDrugId() {
        return nonDrugId;
    }

    public void setNonDrugId(Long nonDrugId) {
        this.nonDrugId = nonDrugId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getSkLimit() {
        return skLimit;
    }

    public void setSkLimit(Long skLimit) {
        this.skLimit = skLimit;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", date=").append(date);
        sb.append(", status=").append(status);
        sb.append(", remain=").append(remain);
        sb.append(", noon=").append(noon);
        sb.append(", nonDrugId=").append(nonDrugId);
        sb.append(", deptId=").append(deptId);
        sb.append(", skLimit=").append(skLimit);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}