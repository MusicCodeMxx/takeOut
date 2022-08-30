package top.starshine.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import top.starshine.commons.aspect.ApiRestController;
import top.starshine.commons.converter.OrderConverter;
import top.starshine.commons.entity.order.TradeRefundApplicationVc;
import top.starshine.service.TradeRefundService;

/**
 * <h3>交易退款接口</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/4  下午 7:54  周四
 * @Description: 创作不容易, 记得关注点赞打赏一键三连
 */
@RequiredArgsConstructor
@ApiRestController("/tradeRefund/")
public class TradeRefundController {

    private final OrderConverter orderConverter;
    private final TradeRefundService tradeRefundService;

    // TODO 暂时写这里, 后续admin 控制台才有 商家处理退款
    //@PostMapping("handle/return/apply")
    //public void handleReturnApply (@RequestBody @Validated OrderHandleReturnApplyVc orderHandleReturnApplyVc){
    //
    //}

    // 用户退货申请
    @PostMapping("apply")
    public void apply(@RequestBody @Validated TradeRefundApplicationVc tradeRefundApplicationVc){
        tradeRefundService.apply(orderConverter.tradeRefundApplicationVcToTradeRefund(tradeRefundApplicationVc));
    }

}
