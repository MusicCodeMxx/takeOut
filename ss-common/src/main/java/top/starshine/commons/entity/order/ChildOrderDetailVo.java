package top.starshine.commons.entity.order;

import lombok.Getter;
import lombok.Setter;

/**
 * <h3>子订单视图对象</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/4  上午 1:58  周四
 * @Description: 创作不容易, 记得关注点赞打赏一键三连
 */
@Setter
@Getter
public class ChildOrderDetailVo implements java.io.Serializable{

    /** 产品名 */
    private String name;

    /** 产品属性 > 口味 */
    private String value;

    /** 产品主图片 */
    private String imageDefUrl;

    /** 产品价格 */
    private Double price;

    /** 产品购买数量 */
    private Integer amount;

}
