package top.starshine.commons.model.web.method;

import com.alibaba.fastjson.JSONPath;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodArgumentResolver;
import top.starshine.commons.aspect.AesDecrypt;
import top.starshine.commons.properties.AesProperties;
import top.starshine.commons.util.AesUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.lang.reflect.Field;
import java.util.List;

/**
 * <h3>自定义 解密 处理程序方法参数解析器</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/30  下午 10:32  周六
 * @Description: hello world
 */
@Slf4j
@Order(1)// 必须为 1, JSR303 才能校验到
public class AesDecryptArgumentResolver extends AbstractMessageConverterMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private final AesProperties aesProperties;

    public AesDecryptArgumentResolver(List<HttpMessageConverter<?>> converters, AesProperties aesProperties) {
        super(converters);
        this.aesProperties = aesProperties;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AesDecrypt.class);
    }

    @Override
    public Object resolveArgument(@NotNull MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        // 获取请求头
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        if (null == servletRequest) return null;

        // 获取注解
        AesDecrypt annotation = parameter.getParameterAnnotation(AesDecrypt.class);
        if (null == annotation) return null;

        // 检查是否传入
        boolean valueIsNotNull = annotation.value().length > 1 || StringUtils.hasText(annotation.value()[0]);

        // 针对字符串处理
        if (parameter.getParameterType() == String.class){
            // 读前端传入的参数流转成 JSON
            BufferedReader br = servletRequest.getReader();
            if (null == br) return null;
            String str;
            StringBuilder reqStr = new StringBuilder();
            while ((str = br.readLine()) != null) {
                reqStr.append(str);
            }
            br.close();// 释放资源
            str = reqStr.toString();
            reqStr = null;
            Object value;// 转成值

            // 检查是否传入字段名
            if (!valueIsNotNull) {
                value = str;
            }else{
                try {
                    value = JSONPath.read(str, "$." + annotation.value()[0]);
                } catch (Exception ignored) {
                    value = str;
                }
            }
            str = null;

            if (null == value) return null;
            // 解密操作
            if (value instanceof String) return AesUtils.decryptFromBase64((String) value, aesProperties.getClientInput());
            return value;// 若不是字符串之间返回
        }

        // 针对对象处理
        ServletServerHttpRequest inputMessage = new ServletServerHttpRequest(servletRequest);
        Object arg = this.readWithMessageConverters(inputMessage, parameter, parameter.getParameterType());
        inputMessage = null;
        if (null == arg) return null;
        // 反射对象
        Class<?> aClass = arg.getClass();
        // 检查是否有指定字段
        if (valueIsNotNull){
            // 针对指定字段解密
            for (String fieldName : annotation.value()) {
                Field declaredField = aClass.getDeclaredField(fieldName);
                exchange(declaredField,arg);
            }
        }else {
            // 反射全部字符串
            Field[] declaredFields = aClass.getDeclaredFields();
            // 针对全部解密
            for (Field declaredField : declaredFields) {
                exchange(declaredField,arg);
            }
        }
        return arg;// 返回映射对象
    }

    /**
     * 获取出字段值, 解密, 替换
     * @param declaredField 字段
     * @param arg 对象
     * @throws IllegalAccessException 异常
     */
    private void exchange(Field declaredField,  Object arg) throws IllegalAccessException {
        declaredField.setAccessible(true);// 禁止访问控制检查
        Object field = declaredField.get(arg);// 获取值
        if (field == null) return;// 空值跳过
        if (!(field instanceof String))  return; // 非字符串跳过
        field = AesUtils.decryptFromBase64((String) field, aesProperties.getClientInput());// 解密
        declaredField.set(arg, field);// 回写
    }
}
