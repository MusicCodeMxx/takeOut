package top.starshine.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.starshine.commons.aspect.ApiRestController;
import top.starshine.commons.converter.UserConverter;
import top.starshine.commons.entity.user.UserVo;
import top.starshine.service.UserService;

/**
 * <h3>用户管理</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/25  下午 3:44  周一
 * @Description: hello world
 */
@RequiredArgsConstructor
@ApiRestController("/user/")
public class UserController {

    private final UserService userService;
    private final UserConverter userConverter;

    // 通过主键查找某个用户
    @GetMapping("find/by/id/{id}")
    public UserVo findById(@PathVariable("id")Long id){
        return userConverter.userToUserVo(userService.getById(id));
    }

    // 通过存入手机号查找用户
    @GetMapping("find/by/phoneNumber/{id}")
    public UserVo findByPhoneNumber(@PathVariable("id")Long id){
        return null;
    }

}
