package com.neu.his.cloud.zuul.model;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class DmsNonDrugItemRecord implements Serializable {
    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "门诊号")
    private Long registrationId;

    @ApiModelProperty(value = "状态 :  1 未交费 2 退费")
    private Integer status;

    @ApiModelProperty(value = "目的")
    private String aim;

    @ApiModelProperty(value = "检查检验要求")
    private String demand;

    @ApiModelProperty(value = "登记状态")
    private Integer logStatus;

    @ApiModelProperty(value = "检查检验结果")
    private String checkResult;

    @ApiModelProperty(value = "检查检验结果img")
    private String resultImgUrlList;

    @ApiModelProperty(value = "临床印象")
    private String clinicalImpression;

    @ApiModelProperty(value = "临床诊断")
    private String clinicalDiagnosis;

    @ApiModelProperty(value = "开立时间")
    private Date createTime;

    private Long excuteStaffId;

    private Date logDatetime;

    private Long noDrugId;

    @ApiModelProperty(value = "检查的部位")
    private String checkParts;

    @ApiModelProperty(value = "1检查 2检验 3处置")
    private Integer type;

    @ApiModelProperty(value = "所属科室Id")
    private Long excuteDeptId;

    @ApiModelProperty(value = "医生Id")
    private Long createStaffId;

    private Long logStaffId;

    private Date excuteTime;

    @ApiModelProperty(value = "数额")
    private BigDecimal amount;

    @ApiModelProperty(value = "打发票")
    private Long invoice;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(Long registrationId) {
        this.registrationId = registrationId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAim() {
        return aim;
    }

    public void setAim(String aim) {
        this.aim = aim;
    }

    public String getDemand() {
        return demand;
    }

    public void setDemand(String demand) {
        this.demand = demand;
    }

    public Integer getLogStatus() {
        return logStatus;
    }

    public void setLogStatus(Integer logStatus) {
        this.logStatus = logStatus;
    }

    public String getCheckResult() {
        return checkResult;
    }

    public void setCheckResult(String checkResult) {
        this.checkResult = checkResult;
    }

    public String getResultImgUrlList() {
        return resultImgUrlList;
    }

    public void setResultImgUrlList(String resultImgUrlList) {
        this.resultImgUrlList = resultImgUrlList;
    }

    public String getClinicalImpression() {
        return clinicalImpression;
    }

    public void setClinicalImpression(String clinicalImpression) {
        this.clinicalImpression = clinicalImpression;
    }

    public String getClinicalDiagnosis() {
        return clinicalDiagnosis;
    }

    public void setClinicalDiagnosis(String clinicalDiagnosis) {
        this.clinicalDiagnosis = clinicalDiagnosis;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getExcuteStaffId() {
        return excuteStaffId;
    }

    public void setExcuteStaffId(Long excuteStaffId) {
        this.excuteStaffId = excuteStaffId;
    }

    public Date getLogDatetime() {
        return logDatetime;
    }

    public void setLogDatetime(Date logDatetime) {
        this.logDatetime = logDatetime;
    }

    public Long getNoDrugId() {
        return noDrugId;
    }

    public void setNoDrugId(Long noDrugId) {
        this.noDrugId = noDrugId;
    }

    public String getCheckParts() {
        return checkParts;
    }

    public void setCheckParts(String checkParts) {
        this.checkParts = checkParts;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getExcuteDeptId() {
        return excuteDeptId;
    }

    public void setExcuteDeptId(Long excuteDeptId) {
        this.excuteDeptId = excuteDeptId;
    }

    public Long getCreateStaffId() {
        return createStaffId;
    }

    public void setCreateStaffId(Long createStaffId) {
        this.createStaffId = createStaffId;
    }

    public Long getLogStaffId() {
        return logStaffId;
    }

    public void setLogStaffId(Long logStaffId) {
        this.logStaffId = logStaffId;
    }

    public Date getExcuteTime() {
        return excuteTime;
    }

    public void setExcuteTime(Date excuteTime) {
        this.excuteTime = excuteTime;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getInvoice() {
        return invoice;
    }

    public void setInvoice(Long invoice) {
        this.invoice = invoice;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", registrationId=").append(registrationId);
        sb.append(", status=").append(status);
        sb.append(", aim=").append(aim);
        sb.append(", demand=").append(demand);
        sb.append(", logStatus=").append(logStatus);
        sb.append(", checkResult=").append(checkResult);
        sb.append(", resultImgUrlList=").append(resultImgUrlList);
        sb.append(", clinicalImpression=").append(clinicalImpression);
        sb.append(", clinicalDiagnosis=").append(clinicalDiagnosis);
        sb.append(", createTime=").append(createTime);
        sb.append(", excuteStaffId=").append(excuteStaffId);
        sb.append(", logDatetime=").append(logDatetime);
        sb.append(", noDrugId=").append(noDrugId);
        sb.append(", checkParts=").append(checkParts);
        sb.append(", type=").append(type);
        sb.append(", excuteDeptId=").append(excuteDeptId);
        sb.append(", createStaffId=").append(createStaffId);
        sb.append(", logStaffId=").append(logStaffId);
        sb.append(", excuteTime=").append(excuteTime);
        sb.append(", amount=").append(amount);
        sb.append(", invoice=").append(invoice);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}