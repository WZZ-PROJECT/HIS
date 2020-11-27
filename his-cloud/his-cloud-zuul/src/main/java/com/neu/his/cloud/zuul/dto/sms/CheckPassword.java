package com.neu.his.cloud.zuul.dto.sms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CheckPassword {

    /**
     * 正则表达式-强密码-【必填字母数字及特殊字符，且以字母开头，8位以上】
     */
    private static final String REGEX_PASSWORD_STRONG = "^(?![0-9]+$)(?![^0-9]+$)(?![a-zA-Z]+$)(?![^a-zA-Z]+$)(?![a-zA-Z0-9]+$)[a-zA-Z0-9\\S]{8,}$";

    @ApiModelProperty(value = "原始密码", required = true)
    private String oldPassword;
    @ApiModelProperty(value = "新密码", required = true)
    private String newPassword;
    @ApiModelProperty(value = "用户ID", required = true)
    private Long staffId;

    public String getOldPassword() {
        return oldPassword;
    }



    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
       if (newPassword.matches(CheckPassword.REGEX_PASSWORD_STRONG)) {
           throw new RuntimeException("密码必须由字母、数字组成，区分大小写 长度在 4 到 16 个字符");
       }
        this.newPassword = newPassword;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }
}
