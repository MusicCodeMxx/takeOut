package top.starshine.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.starshine.commons.aspect.ApiRestController;
import top.starshine.service.PaymentService;

/**
 * <h3></h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/2  下午 9:08  周二
 * @Description: 创作不容易, 记得关注点赞打赏一键三连
 */
@RequiredArgsConstructor
@ApiRestController("/payment/")
public class PaymentController {

    private final PaymentService paymentService;

    // 拉起支付宝手机网页支付
    @GetMapping("alipay/mobile/{orderId}")
    public String payment(@PathVariable("orderId") Long orderId){
        if (null == orderId) throw new RuntimeException("订单号必须填参数");
        return paymentService.payment(orderId);
    }

}
