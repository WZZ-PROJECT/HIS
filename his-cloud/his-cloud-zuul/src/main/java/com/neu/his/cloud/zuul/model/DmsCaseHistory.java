package com.neu.his.cloud.zuul.model;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

public class DmsCaseHistory implements Serializable {
    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "主诉")
    private String chiefComplaint;

    @ApiModelProperty(value = "现病史")
    private String historyOfPresentIllness;

    @ApiModelProperty(value = "现治疗情况")
    private String historyOfTreatment;

    @ApiModelProperty(value = "既往史")
    private String pastHistory;

    @ApiModelProperty(value = "过敏史")
    private String allergies;

    @ApiModelProperty(value = "体格检查")
    private String healthCheckup;

    @ApiModelProperty(value = "门诊号(挂号号)")
    private Integer registrationId;

    @ApiModelProperty(value = "初诊结果str串")
    private String priliminaryDiseStrList;

    @ApiModelProperty(value = "发病时间")
    private Date startDate;

    @ApiModelProperty(value = "病人姓名")
    private String name;

    @ApiModelProperty(value = "病人性别")
    private Integer gender;

    @ApiModelProperty(value = "病人年龄")
    private String ageStr;

    @ApiModelProperty(value = "检查:  name<>部位<>result<>url ><")
    private String checkStrList;

    private String dispositionStrList;

    private String herbalPrescriptionStrList;

    private Date createTime;

    @ApiModelProperty(value = "确诊诊断串")
    private String definiteDiseStrList;

    private Integer patientId;

    private String testStrList;

    private String medicinePrescriptionStrList;

    @ApiModelProperty(value = "1 初诊结束")
    private Integer status;

    @ApiModelProperty(value = "初诊结果Id串")
    private String priliminaryDiseIdList;

    @ApiModelProperty(value = "检查结果（门诊医生填写）")
    private String checkResult;

    @ApiModelProperty(value = "检验结果（门诊医生填写）")
    private String testResult;

    @ApiModelProperty(value = "医嘱")
    private String testAdvice;

    @ApiModelProperty(value = "知情告知Id串用，号隔开  形式 1，2，3")
    private String familiarinform;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChiefComplaint() {
        return chiefComplaint;
    }

    public void setChiefComplaint(String chiefComplaint) {
        this.chiefComplaint = chiefComplaint;
    }

    public String getHistoryOfPresentIllness() {
        return historyOfPresentIllness;
    }

    public void setHistoryOfPresentIllness(String historyOfPresentIllness) {
        this.historyOfPresentIllness = historyOfPresentIllness;
    }

    public String getHistoryOfTreatment() {
        return historyOfTreatment;
    }

    public void setHistoryOfTreatment(String historyOfTreatment) {
        this.historyOfTreatment = historyOfTreatment;
    }

    public String getPastHistory() {
        return pastHistory;
    }

    public void setPastHistory(String pastHistory) {
        this.pastHistory = pastHistory;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getHealthCheckup() {
        return healthCheckup;
    }

    public void setHealthCheckup(String healthCheckup) {
        this.healthCheckup = healthCheckup;
    }

    public Integer getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(Integer registrationId) {
        this.registrationId = registrationId;
    }

    public String getPriliminaryDiseStrList() {
        return priliminaryDiseStrList;
    }

    public void setPriliminaryDiseStrList(String priliminaryDiseStrList) {
        this.priliminaryDiseStrList = priliminaryDiseStrList;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getAgeStr() {
        return ageStr;
    }

    public void setAgeStr(String ageStr) {
        this.ageStr = ageStr;
    }

    public String getCheckStrList() {
        return checkStrList;
    }

    public void setCheckStrList(String checkStrList) {
        this.checkStrList = checkStrList;
    }

    public String getDispositionStrList() {
        return dispositionStrList;
    }

    public void setDispositionStrList(String dispositionStrList) {
        this.dispositionStrList = dispositionStrList;
    }

    public String getHerbalPrescriptionStrList() {
        return herbalPrescriptionStrList;
    }

    public void setHerbalPrescriptionStrList(String herbalPrescriptionStrList) {
        this.herbalPrescriptionStrList = herbalPrescriptionStrList;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getDefiniteDiseStrList() {
        return definiteDiseStrList;
    }

    public void setDefiniteDiseStrList(String definiteDiseStrList) {
        this.definiteDiseStrList = definiteDiseStrList;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public String getTestStrList() {
        return testStrList;
    }

    public void setTestStrList(String testStrList) {
        this.testStrList = testStrList;
    }

    public String getMedicinePrescriptionStrList() {
        return medicinePrescriptionStrList;
    }

    public void setMedicinePrescriptionStrList(String medicinePrescriptionStrList) {
        this.medicinePrescriptionStrList = medicinePrescriptionStrList;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPriliminaryDiseIdList() {
        return priliminaryDiseIdList;
    }

    public void setPriliminaryDiseIdList(String priliminaryDiseIdList) {
        this.priliminaryDiseIdList = priliminaryDiseIdList;
    }

    public String getCheckResult() {
        return checkResult;
    }

    public void setCheckResult(String checkResult) {
        this.checkResult = checkResult;
    }

    public String getTestResult() {
        return testResult;
    }

    public void setTestResult(String testResult) {
        this.testResult = testResult;
    }

    public String gettestAdvice() {
        return testAdvice;
    }

    public void settestAdvice(String testAdvice) {
        this.testAdvice = testAdvice;
    }

    public String getFamiliarinform() {
        return familiarinform;
    }

    public void setFamiliarinform(String familiarinform) {
        this.familiarinform = familiarinform;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", chiefComplaint=").append(chiefComplaint);
        sb.append(", historyOfPresentIllness=").append(historyOfPresentIllness);
        sb.append(", historyOfTreatment=").append(historyOfTreatment);
        sb.append(", pastHistory=").append(pastHistory);
        sb.append(", allergies=").append(allergies);
        sb.append(", healthCheckup=").append(healthCheckup);
        sb.append(", registrationId=").append(registrationId);
        sb.append(", priliminaryDiseStrList=").append(priliminaryDiseStrList);
        sb.append(", startDate=").append(startDate);
        sb.append(", name=").append(name);
        sb.append(", gender=").append(gender);
        sb.append(", ageStr=").append(ageStr);
        sb.append(", checkStrList=").append(checkStrList);
        sb.append(", dispositionStrList=").append(dispositionStrList);
        sb.append(", herbalPrescriptionStrList=").append(herbalPrescriptionStrList);
        sb.append(", createTime=").append(createTime);
        sb.append(", definiteDiseStrList=").append(definiteDiseStrList);
        sb.append(", patientId=").append(patientId);
        sb.append(", testStrList=").append(testStrList);
        sb.append(", medicinePrescriptionStrList=").append(medicinePrescriptionStrList);
        sb.append(", status=").append(status);
        sb.append(", priliminaryDiseIdList=").append(priliminaryDiseIdList);
        sb.append(", checkResult=").append(checkResult);
        sb.append(", testResult=").append(testResult);
        sb.append(", testAdvice=").append(testAdvice);
        sb.append(", familiarinform=").append(familiarinform);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}