package top.starshine.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * <h3>跨域配置</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/24  下午 5:23  周日
 * @Description: hello world
 */
@Configuration
public class MyCrossOriginConfigure {

    /**
     * 配置跨域
     * @return 跨域配置对象
     */
    @Bean
    public CorsWebFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("*");// 允许所有域名进行跨域调用
        config.setAllowCredentials(true);// 允许跨越发送cookie
        config.addAllowedHeader("*");// 放行全部原始头信息
        config.addAllowedMethod("*");// 允许所有请求方法跨域调用
        config.addExposedHeader("*");// 添加响应头
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);// 所有路径都允许跨域
        return new CorsWebFilter(source);
    }

}
