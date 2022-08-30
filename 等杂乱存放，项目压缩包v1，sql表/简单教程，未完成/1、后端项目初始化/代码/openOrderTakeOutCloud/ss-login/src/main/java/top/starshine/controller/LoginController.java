package top.starshine.controller;

import org.springframework.web.bind.annotation.PostMapping;
import top.starshine.commons.aspect.ApiRestController;

/**
 * <h3>登录接口</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/25  上午 12:00  周一
 * @Description: hello world
 */
@ApiRestController("/login/")
public class LoginController {

    @PostMapping("sendMsm")
    public String sendMsm(){
        return "666666";
    }

}
