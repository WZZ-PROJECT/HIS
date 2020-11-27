package com.neu.his.cloud.zuul.model;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

public class BmsBillsRecord implements Serializable {
    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "账单类型")
    private Integer type;

    @ApiModelProperty(value = "账单号")
    private String billNo;

    @ApiModelProperty(value = "账单穿件时间")
    private Date createTime;

    @ApiModelProperty(value = "账单状态：1 正常")
    private Integer status;

    @ApiModelProperty(value = "发票数")
    private Integer invoiceNum;

    @ApiModelProperty(value = "挂号ID")
    private String registrationId;

    private String recordList;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getInvoiceNum() {
        return invoiceNum;
    }

    public void setInvoiceNum(Integer invoiceNum) {
        this.invoiceNum = invoiceNum;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public String getRecordList() {
        return recordList;
    }

    public void setRecordList(String recordList) {
        this.recordList = recordList;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", type=").append(type);
        sb.append(", billNo=").append(billNo);
        sb.append(", createTime=").append(createTime);
        sb.append(", status=").append(status);
        sb.append(", invoiceNum=").append(invoiceNum);
        sb.append(", registrationId=").append(registrationId);
        sb.append(", recordList=").append(recordList);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}