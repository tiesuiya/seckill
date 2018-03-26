package org.lhpsn.seckill.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

/**
 * 执行秒杀DTO
 *
 * @author lh
 * @since 1.0.0
 */
@ApiModel(value = "执行秒杀DTO")
public class ExecutionRequestDTO {

    @ApiModelProperty(value = "手机号")
    @Pattern(regexp = "^[1][3,4,5,7,8][0-9]{9}$", message = "手机号码有误")
    private String userPhone;

    @ApiModelProperty(value = "md5")
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
