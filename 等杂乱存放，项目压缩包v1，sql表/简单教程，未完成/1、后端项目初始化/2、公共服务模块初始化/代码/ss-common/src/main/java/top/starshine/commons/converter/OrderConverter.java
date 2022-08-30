package top.starshine.commons.converter;

import org.mapstruct.Mapper;
import top.starshine.commons.entity.order.*;

/**
 * <h3>订单服务 Bean 转换器</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/1  下午 3:04  周一
 * @Description: 创作不容易, 记得关注点赞打赏一键三连
 */
@Mapper(componentModel = "spring")
public interface OrderConverter {

    OrderDetailVo orderDetailToOrderDetailVo(OrderDetail source);

    ChildOrderDetailVo childOrderDetailToChildOrderDetailVo(ChildOrderDetail source);

    RefundRecord tradeRefundApplicationVcToTradeRefund(TradeRefundApplicationVc source);

}
