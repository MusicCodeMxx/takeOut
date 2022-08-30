package top.starshine.dubbo;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Value;

/**
 * <h3></h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/25  下午 4:54  周一
 * @Description: hello world
 */
@DubboService(
        // 集群容错模式
        cluster = "failback",
        // 服务降级
        mock = "return null",
        // 接口类型
        interfaceClass = HelloDubboService.class,
        // 接口名称
        interfaceName = "top.top.starshine.dubbo.HelloDubboService",
        // 接口版本
        version = "1.0.0"
)
public class HelloDubooServiceImpl implements HelloDubboService {

    @Value("${spring.application.name}")
    private String applicationName;

    @Override
    public String say() {
        return applicationName + " Hello World";
    }

}
