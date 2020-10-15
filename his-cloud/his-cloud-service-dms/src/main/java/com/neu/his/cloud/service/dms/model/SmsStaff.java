package com.neu.his.cloud.service.dms.model;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

public class SmsStaff implements Serializable {
    private Long id;

    private String username;

    private String password;

    private Integer status;

    private Date createTime;

    private Integer gender;

    private Integer skdFlag;

    private String title;

    private String name;

    private Long deptId;

    private Long roleId;

    private Long registrationRankId;

    private String advantages;
    private String description;

    private String picture;

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getSkdFlag() {
        return skdFlag;
    }

    public void setSkdFlag(Integer skdFlag) {
        this.skdFlag = skdFlag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getRegistrationRankId() {
        return registrationRankId;
    }

    public void setRegistrationRankId(Long registrationRankId) {
        this.registrationRankId = registrationRankId;
    }

    public String getAdvantages() {
        return advantages;
    }

    public void setAdvantages(String advantages) {
        this.advantages = advantages;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "SmsStaff{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                ", gender=" + gender +
                ", skdFlag=" + skdFlag +
                ", title='" + title + '\'' +
                ", name='" + name + '\'' +
                ", deptId=" + deptId +
                ", roleId=" + roleId +
                ", registrationRankId=" + registrationRankId +
                ", advantages='" + advantages + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}