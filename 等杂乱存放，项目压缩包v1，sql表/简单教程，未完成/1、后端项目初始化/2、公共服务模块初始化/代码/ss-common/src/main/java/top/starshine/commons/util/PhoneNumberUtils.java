package top.starshine.commons.util;

import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

/**
 * <h3>检查是否是手机号</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/26  下午 6:36  周二
 * @Description: hello world
 */
public final class PhoneNumberUtils {

    private static final Pattern PATTERN = Pattern.compile("^0?(13[0-9]|14[0-9]|15[0-9]|16[0-9]|17[0-9]|18[0-9]|19[0-9])[0-9]{8}$");

    /** 正则表达式,检查是否满足11位并且为 1(3-9) 开头的手机号 */
    // private static final Pattern PATTERN = Pattern.compile("^1[13456789]\\d{9}$");

    /**
     * <h3>匹配是否为手机号</h3>
     * @param phoneNumber {@link String} 要校验的手机号
     * @return {@link boolean} true 是手机号, false 不是手机号
     */
    public static boolean isPhoneNumber(String phoneNumber){
        if (!StringUtils.hasText(phoneNumber)) return false;
        return PATTERN.matcher(phoneNumber).matches();
    }

}
