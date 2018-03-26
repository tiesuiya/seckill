package org.lhpsn.seckill.dto;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

/**
 * 秒杀执行apiDTO
 *
 * @author lh
 * @since 1.0.0
 */
public class ExecutionRequestDTO {

    @Pattern(regexp = "^[1][3,4,5,7,8][0-9]{9}$", message = "手机号码有误")
    private String userPhone;

    @NotEmpty(message = "md5不能为空")
    private String md5;

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    @Override
    public String toString() {
        return "ExecutionRequestDTO{" +
                "userPhone='" + userPhone + '\'' +
                ", md5='" + md5 + '\'' +
                '}';
    }
}
