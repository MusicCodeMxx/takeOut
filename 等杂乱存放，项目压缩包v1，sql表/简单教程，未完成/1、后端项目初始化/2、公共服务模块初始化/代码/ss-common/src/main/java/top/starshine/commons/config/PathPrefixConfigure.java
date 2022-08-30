package top.starshine.commons.config;

import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.starshine.commons.aspect.ApiRestController;

/**
 * <h3>请求路径添加统一前缀</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/24  下午 5:26  周日
 * @Description: hello world
 */
//@Configuration
public class PathPrefixConfigure implements WebMvcConfigurer {

    /**
     * 请求路径添加统一前缀
     * 来源 https://blog.csdn.net/yzh_1346983557/article/details/115130575
     * @param configurer 路径匹配配置器
     */
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix("/api", c -> c.isAnnotationPresent(ApiRestController.class));
    }

    /**
     * SpringBoot 2.x要重写该方法，
     * 来源: https://blog.csdn.net/qq_27621651/article/details/109105410
     * @param registry 资源处理程序注册表
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/META-INF/resources/")
                .addResourceLocations("classpath:/META-INF/resources/image/")
                .addResourceLocations("classpath:/resources/")
                .addResourceLocations("classpath:/resources/upload/")
                .addResourceLocations("classpath:/upload/");
    }

}
