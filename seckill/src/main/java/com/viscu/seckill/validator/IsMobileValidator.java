package com.viscu.seckill.validator;


import com.viscu.seckill.util.ValidatorUtil;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @ Create by ostreamBaba on 18-12-16
 * @ 自定义手机验证
 */
public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {

    //该检验是不是必须的
    private boolean required = false;

    @Override
    public void initialize(IsMobile isMobile) {
        required = isMobile.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if(required){
            return ValidatorUtil.isMobile(value); //校验
        }else {
            //判断是否为空
            if(StringUtils.isEmpty(value)){
                return true;
            }else {
                return ValidatorUtil.isMobile(value);
            }
        }
    }
}
