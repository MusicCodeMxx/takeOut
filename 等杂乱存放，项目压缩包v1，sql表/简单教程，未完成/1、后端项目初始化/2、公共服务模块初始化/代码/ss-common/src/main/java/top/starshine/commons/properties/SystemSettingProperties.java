package top.starshine.commons.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * <h3>系统设置</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/27  下午 8:43  周三
 * @Description: hello world
 */
@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "starshine.system")
public class SystemSettingProperties {

    /**用户缓存时间,是指用户缓存在redis时间,单位为分钟*/
    private int userCacheTime = 30;

    /**首页缓存有效时间*/
    private int homePageCacheTime = 30;

}
