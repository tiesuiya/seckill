package org.lhpsn.seckill.param;


import org.hibernate.validator.constraints.NotEmpty;

/**
 * 秒杀表单
 *
 * @author lh
 * @since 1.0.0
 */
public class SeckillParam {
    @NotEmpty
    private Long userPhone;
    @NotEmpty
    private String md5;

    public Long getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(Long userPhone) {
        this.userPhone = userPhone;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }
}
