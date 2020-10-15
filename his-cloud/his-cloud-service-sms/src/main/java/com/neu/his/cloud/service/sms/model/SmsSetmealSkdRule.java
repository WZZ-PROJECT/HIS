package com.neu.his.cloud.service.sms.model;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

public class SmsSetmealSkdRule implements Serializable {
    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "操作人ID")
    private Long operatorId;

    @ApiModelProperty(value = "操作时间")
    private Date operateTime;

    @ApiModelProperty(value = "规则名称")
    private String ruleName;

    @ApiModelProperty(value = "规则描述")
    private String description;

    @ApiModelProperty(value = "科室ID")
    private Long deptId;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", status=").append(status);
        sb.append(", operatorId=").append(operatorId);
        sb.append(", operateTime=").append(operateTime);
        sb.append(", ruleName=").append(ruleName);
        sb.append(", description=").append(description);
        sb.append(", deptId=").append(deptId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}