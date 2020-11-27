package com.neu.his.cloud.service.dms.model;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class SmsStaff implements Serializable {
    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "性别")
    private Integer gender;

    private Integer skdFlag;

    @ApiModelProperty(value = "备注")
    private String title;

    @ApiModelProperty(value = "工作人员姓名")
    private String name;

    @ApiModelProperty(value = "所属科室ID")
    private Long deptId;

    @ApiModelProperty(value = "角色ID")
    private Long roleId;

    private Long registrationRankId;

    @ApiModelProperty(value = "头像")
    private String picture;

    @ApiModelProperty(value = "挂号医生金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "医生擅长")
    private String advantages;

    @ApiModelProperty(value = "医生介绍")
    private String description;

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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", username=").append(username);
        sb.append(", password=").append(password);
        sb.append(", status=").append(status);
        sb.append(", createTime=").append(createTime);
        sb.append(", gender=").append(gender);
        sb.append(", skdFlag=").append(skdFlag);
        sb.append(", title=").append(title);
        sb.append(", name=").append(name);
        sb.append(", deptId=").append(deptId);
        sb.append(", roleId=").append(roleId);
        sb.append(", registrationRankId=").append(registrationRankId);
        sb.append(", picture=").append(picture);
        sb.append(", amount=").append(amount);
        sb.append(", advantages=").append(advantages);
        sb.append(", description=").append(description);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}