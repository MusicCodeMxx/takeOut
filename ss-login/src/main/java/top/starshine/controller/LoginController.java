package top.starshine.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import top.starshine.commons.aspect.AesDecrypt;
import top.starshine.commons.aspect.ApiRestController;
import top.starshine.commons.aspect.PreventDuplicateSubmissions;
import top.starshine.commons.aspect.ReturnResultToAesEncrypt;
import top.starshine.commons.converter.UserConverter;
import top.starshine.commons.entity.login.LoginSubmitVc;
import top.starshine.commons.entity.user.User;
import top.starshine.commons.enums.LoginRecordTypeEnum;
import top.starshine.commons.exception.BadRequestException;
import top.starshine.commons.handle.ThreadLocalCache;
import top.starshine.commons.status.UserStatus;
import top.starshine.commons.util.PhoneNumberUtils;
import top.starshine.commons.util.TokenUtils;
import top.starshine.commons.util.UserAgentUtils;
import top.starshine.service.LoginRecordService;
import top.starshine.service.LoginService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <h3>登录管理接口</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/25  上午 12:00  周一
 * @Description: hello world
 */
@Slf4j
@RequiredArgsConstructor
@ApiRestController("/login/")
public class LoginController {

    private final LoginService loginService;
    private final UserConverter userConverter;
    private final LoginRecordService loginRecordService;

    // 发送验证码, 解密操作, +F/vO7Q43drz3cAKvcjhiw==
    @PreventDuplicateSubmissions
    @PostMapping("sendMsm")
    public void sendMsm(@AesDecrypt("phone") String phoneNumber){
        // 检查是否为空,检查时是手机号
        if (!PhoneNumberUtils.isPhoneNumber(phoneNumber)) throw new BadRequestException(UserStatus.PHONE_NUMBER_IS_EMPTY);
        loginService.sendMsm(phoneNumber); // 调用服务
    }

    // 提交登录
    @ReturnResultToAesEncrypt
    @PreventDuplicateSubmissions
    @PostMapping("submit")
    public String submit(@AesDecrypt("phoneNumber") @Validated LoginSubmitVc loginSubmitVc, HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
        User user = loginService.submit(loginSubmitVc.getPhoneNumber(), loginSubmitVc.getVerificationCode());
        loginSubmitVc = null;
        // 生成令牌,将令牌写入响应头
        TokenUtils.addHeaderToken(response, TokenUtils.create(user.getNickname(), user.getId().toString()));
        // TODO 由 httpCline 改 okhttp 出现 Bug, 查询 ip 归属有问题, 存入登录信息
        loginRecordService.saveRecord(UserAgentUtils.getUserAgent(request),
                        request.getRemoteAddr(), user.getId(), LoginRecordTypeEnum.VERIFICATION_CODE_LOGIN.name());
        return new ObjectMapper().writeValueAsString(userConverter.userToUserVo(user));
    }

    // 退出登录
    @DeleteMapping("logout")
    public void logout(HttpServletRequest request){
        User user = ThreadLocalCache.getNotNull();
        // 移除登录信息
        loginService.logout(user);
        // 存入登录信息
        loginRecordService.saveRecord(UserAgentUtils.getUserAgent(request),
                                    request.getRemoteAddr(),
                                    user.getId(),
                                    LoginRecordTypeEnum.LOGOUT.name());
    }

}
