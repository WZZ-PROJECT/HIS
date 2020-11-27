package com.neu.his.cloud.service.bms.model;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class BmsRollback implements Serializable {
    private Long id;

    @ApiModelProperty(value = "bms_refund_bill表的主键")
    private Long refundBillId;

    private String accountCode;

    private BigDecimal rbAmount;

    @ApiModelProperty(value = "1.申请退费  2.申请通过  3.退费中  4.退费成功")
    private Integer rbState;

    @ApiModelProperty(value = "申请时间")
    private Date appTime;

    @ApiModelProperty(value = "退费时间")
    private Date rbTime;

    @ApiModelProperty(value = "1.人工  2.微信  3.支付宝  4.银联")
    private Integer rbType;

    private String outTradeId;

    private String rosult;

    private Long createUser;

    private Date createTime;

    private Long updateUser;

    private Date updateTime;

    private Integer isDeleted;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRefundBillId() {
        return refundBillId;
    }

    public void setRefundBillId(Long refundBillId) {
        this.refundBillId = refundBillId;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public BigDecimal getRbAmount() {
        return rbAmount;
    }

    public void setRbAmount(BigDecimal rbAmount) {
        this.rbAmount = rbAmount;
    }

    public Integer getRbState() {
        return rbState;
    }

    public void setRbState(Integer rbState) {
        this.rbState = rbState;
    }

    public Date getAppTime() {
        return appTime;
    }

    public void setAppTime(Date appTime) {
        this.appTime = appTime;
    }

    public Date getRbTime() {
        return rbTime;
    }

    public void setRbTime(Date rbTime) {
        this.rbTime = rbTime;
    }

    public Integer getRbType() {
        return rbType;
    }

    public void setRbType(Integer rbType) {
        this.rbType = rbType;
    }

    public String getOutTradeId() {
        return outTradeId;
    }

    public void setOutTradeId(String outTradeId) {
        this.outTradeId = outTradeId;
    }

    public String getRosult() {
        return rosult;
    }

    public void setRosult(String rosult) {
        this.rosult = rosult;
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
        sb.append(", refundBillId=").append(refundBillId);
        sb.append(", accountCode=").append(accountCode);
        sb.append(", rbAmount=").append(rbAmount);
        sb.append(", rbState=").append(rbState);
        sb.append(", appTime=").append(appTime);
        sb.append(", rbTime=").append(rbTime);
        sb.append(", rbType=").append(rbType);
        sb.append(", outTradeId=").append(outTradeId);
        sb.append(", rosult=").append(rosult);
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