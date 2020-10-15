package com.neu.his.cloud.zuul.model;

import java.io.Serializable;
import java.util.Date;

public class PmsPatient implements Serializable {
    private Long id;

    private String name;

    private Date dateOfBirth;

    private String identificationNo;

    private String homeAddress;

    private String phoneNo;

    private Integer gender;

    private String medicalRecordNo;

    private String card;
    private String openId;

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
        return "PmsPatient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", identificationNo='" + identificationNo + '\'' +
                ", homeAddress='" + homeAddress + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", gender=" + gender +
                ", medicalRecordNo='" + medicalRecordNo + '\'' +
                ", card='" + card + '\'' +
                ", openId='" + openId + '\'' +
                ", picture='" + picture + '\'' +
                '}';
    }
}