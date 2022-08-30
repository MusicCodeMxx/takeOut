package top.starshine.commons.entity.order;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import top.starshine.commons.entity.coupon.Coupon;
import top.starshine.commons.entity.shoppingcar.ShoppingCartVo;
import top.starshine.commons.entity.user.User;

import java.util.ArrayList;
import java.util.List;

/**
 * <h3>订单交易数据对象</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/1  下午 5:16  周一
 * @Description: 创作不容易, 记得关注点赞打赏一键三连
 */
@Setter
@Getter
@Accessors(chain = true)
public class TradeOrderDto implements java.io.Serializable{

    /**前端提交的信息*/
    private SubmitOrderVC submitOrderVC;

    /**子订单集合*/
    private List<ChildOrderDetail> childOrderDetails;

    /**订单信息*/
    private OrderDetail orderDetail;

    /**当前登录用户信息*/
    private User user;

    /**用户持有的优惠券列表*/
    private List<Coupon> coupons;

    /**要核销的优惠券*/
    private Coupon coupon;

    public TradeOrderDto() {}

    public TradeOrderDto(SubmitOrderVC submitOrderVC) {
        this.submitOrderVC = submitOrderVC;
    }

    /**初始化*/
    public void init(){
        if (null == submitOrderVC) return;
        // 创建订单对象, 地址复制
        this.orderDetail = new OrderDetail().setStatus(1)
                                            .setCouponPrice(0.00D)
                                            .setRemark(submitOrderVC.getRemark())
                                            .setSex(submitOrderVC.getSex())
                                            .setPhoneNumber(submitOrderVC.getPhoneNumber())
                                            .setAddressBookId(submitOrderVC.getAddressBookId())
                                            .setConsigneeName(submitOrderVC.getConsigneeName())
                                            .setDetail(submitOrderVC.getDetail())
                                            .setLabel(submitOrderVC.getLabel())
                                            .setUserId(user.getId());
    }

    /**
     * 添加产品数据
     * @param shoppingCartVos 购物车对象列表
     */
    public void addChildOrderDetails(List<ShoppingCartVo> shoppingCartVos){
        List<ChildOrderDetail> temp = childOrderDetails;
        if (null==temp) temp = new ArrayList<>();
        for (ShoppingCartVo scv : shoppingCartVos) {
            temp.add(new ChildOrderDetail()
                    .setName(scv.getName())
                    .setPrice(scv.getPrice())
                    .setAmount(scv.getAmount())
                    .setImageDefUrl(scv.getImageDefUrl())
                    .setProductId(Long.valueOf(scv.getId()))
                    .setValue(scv.getValue())
                    .setCategoryId(Long.valueOf(scv.getCategoryId())));
        }
        childOrderDetails = temp;
        shoppingCartVos = null;
    }
}
