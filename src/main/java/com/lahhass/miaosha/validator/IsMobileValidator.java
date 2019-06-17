package com.lahhass.miaosha.validator;


import com.lahhass.miaosha.util.ValidatorUtil;
import org.apache.commons.lang3.StringUtils;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


//需要extends ConstraintValidator，String为校验器校验的字段类型
public class IsMobileValidator implements ConstraintValidator<IsMobile,String> {

    private boolean required = false;

    @Override
    public void initialize(IsMobile isMobile) {
        required = isMobile.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (required) {
            return ValidatorUtil.isMobile(value);
        } else {
            if (StringUtils.isEmpty(value)) {
                return true;
            } else {
                return ValidatorUtil.isMobile(value);
            }
        }
    }
}
