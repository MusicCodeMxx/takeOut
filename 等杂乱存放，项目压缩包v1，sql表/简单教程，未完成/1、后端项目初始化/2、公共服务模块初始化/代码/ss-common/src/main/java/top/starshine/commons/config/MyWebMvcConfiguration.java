package top.starshine.commons.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.starshine.commons.model.token.TokenInterceptor;
import top.starshine.commons.model.web.method.AesDecryptArgumentResolver;
import top.starshine.commons.model.web.method.JsonFieldArgumentResolver;
import top.starshine.commons.properties.AesProperties;

import java.util.Collections;
import java.util.List;

/**
 * <h3>配置类</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/24  下午 11:20  周日
 * @Description: hello world
 */
@EnableWebMvc
@Configuration
@RequiredArgsConstructor
public class MyWebMvcConfiguration implements WebMvcConfigurer {

    /** 令牌拦截器 */
    private final TokenInterceptor tokenInterceptor;

    /**解密配置*/
    private final AesProperties aesProperties;

    /**
     * 注册拦截器
     * @param registry 拦截器注册表
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        /* 令牌拦截器优先,令牌认证再到安全认证 */
        registry.addInterceptor(tokenInterceptor).addPathPatterns("/**").order(-1);// 所有请求都要经过令牌拦截检查

    }

    /**
     * 添加自定义 处理程序方法参数解析器
     * @param resolvers 解析器
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        // Json 字段参数解析器
        resolvers.add(new JsonFieldArgumentResolver());
        // Json 字段参数解析器之后进行解密操作
        resolvers.add(new AesDecryptArgumentResolver(Collections.singletonList(new MappingJackson2HttpMessageConverter()), aesProperties));
        // 传入解析器
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
    }

}
