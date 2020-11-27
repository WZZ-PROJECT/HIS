package com.neu.his.cloud.zuul.model;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

public class PmsPatient implements Serializable {
    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "病人名称")
    private String name;

    @ApiModelProperty(value = "出生日期")
    private Date dateOfBirth;

    @ApiModelProperty(value = "身份证号码")
    private String identificationNo;

    @ApiModelProperty(value = "家庭住址")
    private String homeAddress;

    @ApiModelProperty(value = "联系方式")
    private String phoneNo;

    @ApiModelProperty(value = "性别")
    private Integer gender;

    @ApiModelProperty(value = "医疗记录编号")
    private String medicalRecordNo;

    @ApiModelProperty(value = "医保卡")
    private String card;

    @ApiModelProperty(value = "微信唯一码")
    private String openId;

    @ApiModelProperty(value = "头像")
    private String picture;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getIdentificationNo() {
        return identificationNo;
    }

    public void setIdentificationNo(String identificationNo) {
        this.identificationNo = identificationNo;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getMedicalRecordNo() {
        return medicalRecordNo;
    }

    public void setMedicalRecordNo(String medicalRecordNo) {
        this.medicalRecordNo = medicalRecordNo;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", dateOfBirth=").append(dateOfBirth);
        sb.append(", identificationNo=").append(identificationNo);
        sb.append(", homeAddress=").append(homeAddress);
        sb.append(", phoneNo=").append(phoneNo);
        sb.append(", gender=").append(gender);
        sb.append(", medicalRecordNo=").append(medicalRecordNo);
        sb.append(", card=").append(card);
        sb.append(", openId=").append(openId);
        sb.append(", picture=").append(picture);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}