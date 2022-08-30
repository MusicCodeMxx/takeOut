package top.starshine.commons.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * <h3>AES 对称加密属性类</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/27  下午 8:47  周三
 * @Description: hello world
 */
@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "starshine.aes")
public class AesProperties {

    /**后端输出给客户端密钥*/
    private String clientOut = "2Vqz1yy35s5Uo776";

    /**前端客户端传入密钥*/
    private String clientInput = "7g4MLt0V7TY939x4";

    /**服务于服务之间调用加密用户信息的密钥*/
    private String serviceUserDetail = "h776GYWBXe687706";

}
