package top.starshine.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.starshine.commons.aspect.ApiRestController;
import top.starshine.commons.aspect.DoWriteBody;
import top.starshine.commons.entity.paymenty.AlipayNotifyVc;
import top.starshine.config.AlipayTemplate;
import top.starshine.service.PaymentService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * <h3>支付宝回调接口</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/3  下午 6:19  周三
 * @Description: 创作不容易, 记得关注点赞打赏一键三连
 */
@Slf4j
@RequiredArgsConstructor
@ApiRestController("/aliPay/notify/")
public class AlipayNotifyController {

    private final AlipayTemplate alipayTemplate;
    private final PaymentService paymentService;

    // 支付宝支付成功之后回调
    @GetMapping("callback")
    public void callbackNotify(AlipayNotifyVc alipayNotifyVc,
                               @RequestParam Map<String, String> paramsMap,
                               HttpServletResponse response) throws IOException {
        // 验签检查, 调用服务层处理业务
        if (alipayTemplate.rsaCheckV1(paramsMap))
            if (paymentService.callbackNotify(alipayNotifyVc)) {
                // 重定向到支付成功页面
                response.sendRedirect("http://10.10.10.201:8080/#/order");
                return;
            }
        paramsMap = null;
        // 重定向到失败页面
        response.sendRedirect("http://10.10.10.201:8080/#/order");
    }

    // 支付宝支付异步通知 支付宝每个订单异步回调通知的频率是 15s 4m,10m,10m,1h,2h,6h,15h
    // 文档地址： https://opendocs.alipay.com/open/270/105902
    @DoWriteBody
    @PostMapping("async")
    public String asyncNotify(AlipayNotifyVc alipayNotifyVc, @RequestParam Map<String, String> paramsMap) {
        // 验签检查, 调用服务层处理业务
        if (alipayTemplate.rsaCheckV1(paramsMap))
            if (paymentService.asyncNotify(alipayNotifyVc))
                return "success";

        paramsMap = null;
        // 订单识别回复失败
        return "failure";
    }

}
