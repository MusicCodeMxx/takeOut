package top.starshine.controller;


import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.starshine.dubbo.HelloDubboService;

/**
 * <h3></h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/25  下午 5:05  周一
 * @Description: hello world
 */
@RestController
public class HelloController {

    @DubboReference(cluster = "failback",
                    interfaceClass = HelloDubboService.class,
                    interfaceName = "top.top.starshine.dubbo.HelloDubboService",
                    version = "1.0.0")
    private HelloDubboService helloDubboService;

    @GetMapping("/say")
    public String say(){
        return helloDubboService.say();
    }

}
