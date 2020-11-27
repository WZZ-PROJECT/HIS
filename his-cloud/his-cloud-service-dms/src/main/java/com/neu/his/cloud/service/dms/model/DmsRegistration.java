package com.neu.his.cloud.service.dms.model;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

public class DmsRegistration implements Serializable {
    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "病人Id")
    private Long patientId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    private Integer endAttendance;

    @ApiModelProperty(value = "登记状态 : 1 未看诊 2 待收费 3 诊毕 4 已退号 5:已取消")
    private Integer status;

    @ApiModelProperty(value = "排班号")
    private Long skdId;

    @ApiModelProperty(value = "病历本")
    private Integer needBook;

    @ApiModelProperty(value = "绑定状态 : 0：非专家号 1: 专家号")
    private Integer bindStatus;

    @ApiModelProperty(value = "所属科室ID")
    private Long deptId;

    @ApiModelProperty(value = "看诊日期")
    private Date attendanceDate;

    private String patientAgeStr;

    @ApiModelProperty(value = "微信充值记录ID")
    private Long wxResultsId;

    @ApiModelProperty(value = "挂号级别")
    private Long registeredlevel;

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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getEndAttendance() {
        return endAttendance;
    }

    public void setEndAttendance(Integer endAttendance) {
        this.endAttendance = endAttendance;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getSkdId() {
        return skdId;
    }

    public void setSkdId(Long skdId) {
        this.skdId = skdId;
    }

    public Integer getNeedBook() {
        return needBook;
    }

    public void setNeedBook(Integer needBook) {
        this.needBook = needBook;
    }

    public Integer getBindStatus() {
        return bindStatus;
    }

    public void setBindStatus(Integer bindStatus) {
        this.bindStatus = bindStatus;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Date getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(Date attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public String getPatientAgeStr() {
        return patientAgeStr;
    }

    public void setPatientAgeStr(String patientAgeStr) {
        this.patientAgeStr = patientAgeStr;
    }

    public Long getWxResultsId() {
        return wxResultsId;
    }

    public void setWxResultsId(Long wxResultsId) {
        this.wxResultsId = wxResultsId;
    }

    public Long getRegisteredlevel() {
        return registeredlevel;
    }

    public void setRegisteredlevel(Long registeredlevel) {
        this.registeredlevel = registeredlevel;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", patientId=").append(patientId);
        sb.append(", createTime=").append(createTime);
        sb.append(", endAttendance=").append(endAttendance);
        sb.append(", status=").append(status);
        sb.append(", skdId=").append(skdId);
        sb.append(", needBook=").append(needBook);
        sb.append(", bindStatus=").append(bindStatus);
        sb.append(", deptId=").append(deptId);
        sb.append(", attendanceDate=").append(attendanceDate);
        sb.append(", patientAgeStr=").append(patientAgeStr);
        sb.append(", wxResultsId=").append(wxResultsId);
        sb.append(", registeredlevel=").append(registeredlevel);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}