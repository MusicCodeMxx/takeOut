package top.starshine.service.impl;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import top.starshine.commons.dubbo.UserDubboService;
import top.starshine.commons.entity.user.User;
import top.starshine.commons.model.token.LoadUserDetailService;

/**
 * <h3></h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/25  上午 12:06  周一
 * @Description: hello world
 */
@Service
public class LoadUserDetailServiceImpl implements LoadUserDetailService {

    @DubboReference(cluster = "failback", interfaceClass = UserDubboService.class, version = "1.0.0")
    private UserDubboService userDubboService;

    @Override
    public User getUserById(String id) {
        return userDubboService.findUserById(id);
    }


}
