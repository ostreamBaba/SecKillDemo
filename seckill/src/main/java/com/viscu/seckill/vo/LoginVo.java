package com.viscu.seckill.vo;

import com.viscu.seckill.validator.IsMobile;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @ Create by ostreamBaba on 18-12-16
 * @ 参数进行校验
 */
public class LoginVo {

    @NotNull //不能为空
    @IsMobile //自定义手机格式验证
    private String mobile;

    @NotNull
    @Length(min = 32) //最短为32
    private String password;

    public String getMobile() {
        return mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginVo{" +
                "mobile='" + mobile + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
