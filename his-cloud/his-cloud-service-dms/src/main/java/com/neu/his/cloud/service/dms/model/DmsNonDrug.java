package com.neu.his.cloud.service.dms.model;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class DmsNonDrug implements Serializable {
    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "缴费项编码")
    private String code;

    @ApiModelProperty(value = "缴费项名称")
    private String name;

    private String format;

    private BigDecimal price;

    private Long expClassId;

    private String mnemonicCode;

    private Integer recordType;

    private Date createDate;

    private Integer status;

    private Long deptId;

    @ApiModelProperty(value = "是否为套餐 : 1 是 2 否")
    private Integer isSetmeal;

    @ApiModelProperty(value = "检查检验处置的地点")
    private String place;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getExpClassId() {
        return expClassId;
    }

    public void setExpClassId(Long expClassId) {
        this.expClassId = expClassId;
    }

    public String getMnemonicCode() {
        return mnemonicCode;
    }

    public void setMnemonicCode(String mnemonicCode) {
        this.mnemonicCode = mnemonicCode;
    }

    public Integer getRecordType() {
        return recordType;
    }

    public void setRecordType(Integer recordType) {
        this.recordType = recordType;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Integer getIsSetmeal() {
        return isSetmeal;
    }

    public void setIsSetmeal(Integer isSetmeal) {
        this.isSetmeal = isSetmeal;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", code=").append(code);
        sb.append(", name=").append(name);
        sb.append(", format=").append(format);
        sb.append(", price=").append(price);
        sb.append(", expClassId=").append(expClassId);
        sb.append(", mnemonicCode=").append(mnemonicCode);
        sb.append(", recordType=").append(recordType);
        sb.append(", createDate=").append(createDate);
        sb.append(", status=").append(status);
        sb.append(", deptId=").append(deptId);
        sb.append(", isSetmeal=").append(isSetmeal);
        sb.append(", place=").append(place);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}