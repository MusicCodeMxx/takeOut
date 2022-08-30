package top.starshine.commons.entity.order;

import lombok.Getter;
import lombok.Setter;
import top.starshine.commons.entity.coupon.CouponVo;
import top.starshine.commons.entity.shoppingcar.ShoppingCartVo;
import top.starshine.commons.entity.user.UserAddressBookVo;

import java.util.List;

/**
 * <h3>加载下单汇聚信息视图对象</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/31  下午 11:37  周日
 * @Description: hello world
 */
@Setter
@Getter
public class BuildBillDataVo implements java.io.Serializable{

    /**购物车信息*/
    private List<ShoppingCartVo> shoppingCartVos;

    /**地址簿信息*/
    private UserAddressBookVo userAddressBookVo;

    /**优惠券列表*/
    private List<CouponVo> couponVos;

}
