package com.neu.his.cloud.zuul.model;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class BmsInvoiceRecord implements Serializable {
    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "发票创建时间")
    private Date createTime;

    @ApiModelProperty(value = "发票号")
    private String invoiceNo;

    @ApiModelProperty(value = "账单ID")
    private String billId;

    @ApiModelProperty(value = "金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "冻结状态 1 正常")
    private Integer freezeStatus;

    private String associateId;

    @ApiModelProperty(value = "操作人员ID")
    private String operatorId;

    @ApiModelProperty(value = "结算类别ID")
    private String settlementCatId;

    private String settleRecordId;

    @ApiModelProperty(value = "发票状态：1 挂号")
    private Integer type;

    private String itemList;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getFreezeStatus() {
        return freezeStatus;
    }

    public void setFreezeStatus(Integer freezeStatus) {
        this.freezeStatus = freezeStatus;
    }

    public String getAssociateId() {
        return associateId;
    }

    public void setAssociateId(String associateId) {
        this.associateId = associateId;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getSettlementCatId() {
        return settlementCatId;
    }

    public void setSettlementCatId(String settlementCatId) {
        this.settlementCatId = settlementCatId;
    }

    public String getSettleRecordId() {
        return settleRecordId;
    }

    public void setSettleRecordId(String settleRecordId) {
        this.settleRecordId = settleRecordId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getItemList() {
        return itemList;
    }

    public void setItemList(String itemList) {
        this.itemList = itemList;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", createTime=").append(createTime);
        sb.append(", invoiceNo=").append(invoiceNo);
        sb.append(", billId=").append(billId);
        sb.append(", amount=").append(amount);
        sb.append(", freezeStatus=").append(freezeStatus);
        sb.append(", associateId=").append(associateId);
        sb.append(", operatorId=").append(operatorId);
        sb.append(", settlementCatId=").append(settlementCatId);
        sb.append(", settleRecordId=").append(settleRecordId);
        sb.append(", type=").append(type);
        sb.append(", itemList=").append(itemList);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}