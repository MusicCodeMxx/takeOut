package top.starshine.commons.util;

import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

/**
 * 检查手机号
 * @author: starshine
 * @version: 1.0
 * @since: 2022/6/22  下午 9:18  周三
 * @Description:
 */
public class PhoneNumberUtils {

    /** 正则表达式,检查是否满足11位并且为 1(3-9) 开头的手机号 */
    private static final Pattern pattern = Pattern.compile("^1[13456789]\\d{9}$");

    /**
     * 匹配是否为手机号
     * <li>真-是手机号</li>
     * <li>假-不是手机号</li>
     * @param phoneNumber 要校验的手机号
     * @return 布尔类型
     */
    public static boolean isPhoneNumber(String phoneNumber){
        if (!StringUtils.hasText(phoneNumber)){
            return false;
        }
        return pattern.matcher(phoneNumber).matches();
    }

}
