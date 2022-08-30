package top.starshine.commons.model.web.method;

import com.alibaba.fastjson.JSONPath;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import top.starshine.commons.aspect.JsonField;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.util.Objects;

/**
 * <h3></h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/30  下午 10:32  周六
 * @Description: hello world
 */
@Slf4j
public class JsonFieldArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(JsonField.class);
    }

    @Override
    public Object resolveArgument(@NotNull MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) (webRequest.getNativeRequest());
        BufferedReader br = request.getReader();
        String str;
        StringBuilder reqStr = new StringBuilder();
        while ((str = br.readLine()) != null) {
            reqStr.append(str);
        }
        return JSONPath.read(reqStr.toString(), "$." + Objects.requireNonNull(parameter.getParameterAnnotation(JsonField.class)).value());
    }

}
