package top.starshine.commons;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import top.starshine.commons.aspect.ApiRestController;
import top.starshine.commons.aspect.DoWriteBody;
import top.starshine.commons.entity.user.User;
import top.starshine.commons.handle.ThreadLocalCache;

/**
 * <h3>项目初始化测试专用接口,生产环境请删除</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/24  下午 6:06  周日
 * @Description: hello world
 */
@ApiRestController("/hello/")
public class HelloController {

    @Value("${spring.application.name}")
    String applicationName;

    @GetMapping("say")
    public String say(){
        return applicationName + " Cloud Service, Hello world!";
    }

    @GetMapping("user")
    public String user(){
        return applicationName + "&"+ ((User)ThreadLocalCache.getNotNull()).getNickname() + " Hello world";
    }

    @DoWriteBody
    @GetMapping("body")
    public String body(){ return "test message write body"; }

}
