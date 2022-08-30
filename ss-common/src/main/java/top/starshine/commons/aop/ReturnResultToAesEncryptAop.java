package top.starshine.commons.aop;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import top.starshine.commons.aspect.ReturnResultToAesEncrypt;
import top.starshine.commons.properties.AesProperties;
import top.starshine.commons.util.AesUtils;

import java.lang.reflect.Field;

/**
 * <h3>AES 解密切面</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/30  下午 8:18  周六
 * @Description: hello world
 */
@Aspect
@Component
@RequiredArgsConstructor
public class ReturnResultToAesEncryptAop {

    private final AesProperties aesProperties;

    @Around("@annotation(top.starshine.commons.aspect.ReturnResultToAesEncrypt)")
    public Object doLockAfterReturning(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object result = proceedingJoinPoint.proceed();
        if (null == result) return null;
        // 获取注解
        ReturnResultToAesEncrypt annotation = ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod().getAnnotation(ReturnResultToAesEncrypt.class);
        if (null == annotation) return result;
        try{
            if (result instanceof String) return AesUtils.encryptToBase64((String) result, aesProperties.getClientOut());
            // 反射对象
            Class<?> aClass = result.getClass();
            // 检查是否有指定字段
            if (annotation.value().length > 1 || StringUtils.hasText(annotation.value()[0])){
                // 针对指定字段解密
                for (String fieldName : annotation.value()) {
                    Field declaredField = aClass.getDeclaredField(fieldName);
                    exchange(declaredField,result);
                }
            }else {
                // 反射全部字符串
                Field[] declaredFields = aClass.getDeclaredFields();
                // 针对全部解密
                for (Field declaredField : declaredFields) {
                    exchange(declaredField,result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;// 返回执行结果
    }

    /**
     * 获取出字段值, 解密, 替换
     * @param declaredField 字段
     * @param arg 对象
     * @throws IllegalAccessException 异常
     */
    private void exchange(Field declaredField, Object arg) throws IllegalAccessException {
        declaredField.setAccessible(true);// 禁止访问控制检查
        Object field = declaredField.get(arg);// 获取值
        if (field == null) return;// 空值跳过
        if (!(field instanceof String)) return; // 非字符串跳过
        field = AesUtils.encryptToBase64((String) field, aesProperties.getClientOut());// 解密
        declaredField.set(arg, field);// 回写
    }

}
