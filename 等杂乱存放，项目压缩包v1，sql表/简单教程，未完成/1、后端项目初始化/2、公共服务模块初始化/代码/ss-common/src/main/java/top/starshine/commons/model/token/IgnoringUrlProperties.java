package top.starshine.commons.model.token;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;

import java.util.Arrays;

/**
 * <h3>忽略认证路径</h3>
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/24  下午 8:33  周日
 * @Description: hello world
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "starshine.ignoring.path")
public class IgnoringUrlProperties {

    /**路径匹配器，支持通配符*/
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    /**路径数组*/
    private String[] urls;

    /**
     * 路径匹配,检查本次请求是否需要放行,支持通配符
     * @param requestURI 要匹配的路径
     * @return true 匹配到, false 未匹配到
     */
    public boolean checkUrl(String requestURI){
        if (null == urls) return false;
        if (null == requestURI) return false;
        if (urls.length == 1) return PATH_MATCHER.match(urls[0], requestURI);
        return Arrays.stream(urls).allMatch(url -> PATH_MATCHER.match(url, requestURI));
    }

}
