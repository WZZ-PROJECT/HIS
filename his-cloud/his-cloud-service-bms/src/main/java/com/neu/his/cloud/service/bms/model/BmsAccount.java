package com.neu.his.cloud.service.bms.model;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class BmsAccount implements Serializable {
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "病人Id")
    private Long patientId;

    private String accountCode;

    @ApiModelProperty(value = "可用")
    private BigDecimal blance;

    @ApiModelProperty(value = "冻结")
    private BigDecimal frozen;

    @ApiModelProperty(value = "总金额")
    private BigDecimal summery;

    @ApiModelProperty(value = "卡号")
    private String cardId;

    @ApiModelProperty(value = "级别")
    private Integer level;

    @ApiModelProperty(value = "1.正常 2.冻结  3.注销  4.懒账户")
    private Integer accountStatus;

    private Long createUser;

    private Date createTime;

    private Long updateUser;

    private Date updateTime;

    @ApiModelProperty(value = "0.未删除  1.已删除")
    private Integer isDeleted;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public BigDecimal getBlance() {
        return blance;
    }

    public void setBlance(BigDecimal blance) {
        this.blance = blance;
    }

    public BigDecimal getFrozen() {
        return frozen;
    }

    public void setFrozen(BigDecimal frozen) {
        this.frozen = frozen;
    }

    public BigDecimal getSummery() {
        return summery;
    }

    public void setSummery(BigDecimal summery) {
        this.summery = summery;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(Integer accountStatus) {
        this.accountStatus = accountStatus;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Long updateUser) {
        this.updateUser = updateUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", patientId=").append(patientId);
        sb.append(", accountCode=").append(accountCode);
        sb.append(", blance=").append(blance);
        sb.append(", frozen=").append(frozen);
        sb.append(", summery=").append(summery);
        sb.append(", cardId=").append(cardId);
        sb.append(", level=").append(level);
        sb.append(", accountStatus=").append(accountStatus);
        sb.append(", createUser=").append(createUser);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateUser=").append(updateUser);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", isDeleted=").append(isDeleted);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}