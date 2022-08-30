package top.starshine.commons.handle;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import top.starshine.commons.aspect.DoWriteBody;
import top.starshine.commons.status.BasisStatus;
import top.starshine.commons.status.Result;

import java.lang.reflect.Method;

/**
 * <h3>全局结果处理 - 全局响应处理程序</h3>
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/6/22  下午 5:54  周三
 * @Description: hello world
 */
//@RestControllerAdvice(annotations = RestController.class,basePackages = {"top.starshine.musiccomponents.controller"})
@RestControllerAdvice
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {

    //private static final MediaType MEDIA_TYPE = MediaType.valueOf("application/json;charset=utf-8");
    /**如果方法返回值是非 String 的转换序列化处理器*/
    //private static final String JACKSON_CONVERTER = "org.springframework.http.converter.json.MappingJackson2HttpMessageConverter";
    /**如果方法返回值是 String 的转换序列化处理器*/
    private static final String STRING_CONVERTER = "org.springframework.http.converter.StringHttpMessageConverter";

    /** 增强 */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    /**
     * 前置响应体写入处理
     * @param body 方法返回的数据体
     * @param returnType 方法的返回类型, 比如: void, string....
     * @param selectedContentType 序列化类型, 比如 application/json, text/plain
     * @param selectedConverterType 转换器-序列化对象, 比如 MappingJackson2HttpMessageConverter
     * @param request 请求对象
     * @param response 响应对象
     * @return 数据体
     */
    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        try{
            Method method = returnType.getMethod();
            if (null!=method){
                // 针对需要直接写入到响应体的需要
                DoWriteBody annotation = method.getAnnotation(DoWriteBody.class);
                if (null != annotation && annotation.value()) {
                    if (null != body) {
                        // 如果是非字符串就直接返回
                        if (!(body instanceof String)) return body;
                        // 针对字符串特殊处理, 设置为文本类型
                        response.getHeaders().setContentType(MediaType.TEXT_PLAIN);
                        // 将字符串写入流
                        response.getBody().write(String.valueOf(body).getBytes());
                    }
                    // 如果没有返回, 就按照没有值直接返回,必须返回空, 不进行交给 Jackson 序列化
                    return null;
                }
            }
        } catch (Exception ignored) {}

        try {

            if (null == body) {

                // 针对方法返回值是 void 和 string, spring 有不同处理
                if (STRING_CONVERTER.equals(selectedConverterType.getName())) {
                    // 由于 String 是 String 转换序列化处理器, 默认是 text/plain, 需要转成 application/json
                    response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                    // 这里的 toString 有修改
                    return Result.success().toString();
                }
                // 若不是 String 转换序列化处理器 就是 Jackson 转换序列化处理器
                //if (selectedConverterType.getName().equals(JACKSON_CONVERTER)) ;
                return Result.success();

            }else if (body instanceof String) {

                // 由于 String 是 String 转换序列化处理器, 默认是 text/plain, 需要转成 application/json
                response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                //return new Result<>(body).toString();// 这里的 toString 有修改
                // 将字符串写入流
                response.getBody().write(new ObjectMapper().writeValueAsBytes(new Result<>(body)));
                return null;

            }else if (body instanceof Result)
                return body;// 直接返回

            return new Result<>(body);// 将原本的数据进行包装

        } catch (Exception e) {
            if (STRING_CONVERTER.equals(selectedConverterType.getName())) {
                response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                return Result.error(BasisStatus.RESULT_ERROR).toString();
            }
            return Result.error(BasisStatus.RESULT_ERROR);
        }
    }

}