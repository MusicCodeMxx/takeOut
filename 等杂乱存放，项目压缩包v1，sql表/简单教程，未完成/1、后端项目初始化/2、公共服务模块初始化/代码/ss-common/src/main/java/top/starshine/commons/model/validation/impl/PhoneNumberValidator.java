package top.starshine.commons.model.validation.impl;

import top.starshine.commons.model.validation.IsPhoneNumber;
import top.starshine.commons.util.PhoneNumberUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * <h3>手机号校验处理</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/27  上午 12:04  周三
 * @Description: hello world
 */
public class PhoneNumberValidator implements ConstraintValidator<IsPhoneNumber, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (Objects.isNull(value)) return true;
        return PhoneNumberUtils.isPhoneNumber(value);
    }

}
