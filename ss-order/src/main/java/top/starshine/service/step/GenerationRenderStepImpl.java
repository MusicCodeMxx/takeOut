package top.starshine.service.step;

import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import top.starshine.commons.entity.order.ChildOrderDetail;
import top.starshine.commons.entity.order.TradeOrderDto;
import top.starshine.commons.exception.InternalServerErrorException;
import top.starshine.commons.model.renderstep.RenderStep;
import top.starshine.commons.model.renderstep.RenderStepEnums;
import top.starshine.commons.status.ShoppingCartStatus;
import top.starshine.commons.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <h3>生成订单渲染步骤</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/1  下午 5:11  周一
 * @Description: 创作不容易, 记得关注点赞打赏一键三连
 */
@Slf4j
@Service
public class GenerationRenderStepImpl implements RenderStep {

    private static final RenderStepEnums GENERATION = RenderStepEnums.GENERATION;
    private static final String PAY_BODY_REMARK_PREFIX = "订单备注:";


    /**
     * 检查是否相同渲染枚举
     * @return {@link RenderStepEnums} 渲染枚举
     */
    @Override
    public boolean step(RenderStepEnums rs) {
        return GENERATION.equals(rs);
    }

    @Override
    public void render(TradeOrderDto tod) {
        log.info("生成订单信息");

        Long userId = tod.getUser().getId();// 用户主键
        String outTradeNo = generateOutTradeNo();// 订单号
        Long orderDetailsId = new DefaultIdentifierGenerator().nextId(new Object());// 订单主键
        if (CollectionUtils.isEmpty(tod.getChildOrderDetails())) throw new InternalServerErrorException(ShoppingCartStatus.IS_EMPTY);

        // 初始子订单
        for (ChildOrderDetail cod : tod.getChildOrderDetails()) {
            cod.setId(new DefaultIdentifierGenerator().nextId(orderDetailsId));
            cod.setOutTradeNo(outTradeNo)
                .setOrderId(orderDetailsId)
                .setIsDelete(0);

        }

        // 填充订单标题
        ChildOrderDetail childOrderDetail = tod.getChildOrderDetails().get(0);
        String subject = "点餐:" + childOrderDetail.getName() + "(" + childOrderDetail.getValue() + ")";
        if (tod.getChildOrderDetails().size() > 1) subject += "，等多个...";

        // 前缀 + 用户备注
        String remark = tod.getSubmitOrderVC().getRemark();
        remark = StringUtils.hasText(remark) ? remark : "无备注";


        // 初始化主订单
        tod.getOrderDetail()
                .setId(orderDetailsId)
                .setUpdateById(userId)
                .setCreateById(userId);
        tod.getOrderDetail()
                .setSubject(subject)
                .setBody(PAY_BODY_REMARK_PREFIX + remark)
                .setOutTradeNo(outTradeNo)
                .setStatus(1)
                .setUpdateById(userId)
                .setCreateById(userId)
                .setIsDelete(0);
    }

    /**
     * 生成订单号
     * @return 返回订单
     */
    private String generateOutTradeNo(){
        return  new SimpleDateFormat("yyyyMMdd").format(new Date(System.currentTimeMillis())) + new DefaultIdentifierGenerator().nextId(new Object());
    }


    public static void main(String[] args) {
    }
}
