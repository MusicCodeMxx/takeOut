package top.starshine.dubbo;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;
import top.starshine.commons.converter.UserConverter;
import top.starshine.commons.dubbo.UserDubboService;
import top.starshine.commons.entity.user.User;
import top.starshine.commons.entity.user.UserAddressBookVo;
import top.starshine.service.UserAddressBookService;
import top.starshine.service.UserService;

/**
 * <h3></h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/26  下午 4:31  周二
 * @Description: hello world
 */
@DubboService(
        // 集群容错模式
        cluster = "failback",
        // 服务降级
        mock = "return null",
        // 接口类型
        interfaceClass = UserDubboService.class,
        // 接口名称
        interfaceName = "top.top.starshine.dubbo.UserDubboService",
        // 接口版本
        version = "1.0.0"
)
@RequiredArgsConstructor
public class UserDubboServiceImpl implements UserDubboService {

    private final UserService userService;
    private final UserConverter userConverter;
    private final UserAddressBookService userAddressBookService;

    @Override
    public User findUserByPhoneNumber(String phoneNumber) {
        return new LambdaQueryChainWrapper<>(userService.getBaseMapper())
                .eq(User::getPhoneNumber,phoneNumber)
                .eq(User::getIsDelete,0)
                .eq(User::getStatus,0)
                .one();
    }

    @Override
    public User findUserById(String id) {
        return userService.getById(id);
    }

    @Override
    @Transactional
    public void saveUserDetail(User user) {
        userService.save(user);
    }

    @Override
    public UserAddressBookVo getDefaultUserAddress() {
        return userConverter.userAddressBookToUserAddressBookVo(userAddressBookService.getDefault());
    }

}
