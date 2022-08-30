package top.starshine.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.starshine.commons.entity.user.User;
import top.starshine.commons.model.token.LoadUserDetailService;
import top.starshine.mapper.UserMapper;
import top.starshine.service.UserService;

/**
 * <h3>用户接口</h3>
 * @author: starshine
 * @version: 1.0
 * @since: 2022/6/22  下午 11:16  周三
 * @Description:
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService, LoadUserDetailService {

    @Override
    public User getUserById(String id) {
        return getBaseMapper().selectById(id);
    }

}
