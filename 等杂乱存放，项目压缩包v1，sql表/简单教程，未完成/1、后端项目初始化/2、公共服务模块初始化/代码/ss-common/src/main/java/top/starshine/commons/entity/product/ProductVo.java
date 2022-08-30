package top.starshine.commons.entity.product;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * <h3>产品视图对象</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/27  下午 10:21  周三
 * @Description: hello world
 */
@Setter
@Getter
public class ProductVo implements java.io.Serializable{

    /** 产品主键 */
    private String id;

    /** 绑定分类主键 */
    private String categoryId;

    /** 产品名称 */
    private String name;

    /** 描述 */
    private String description;

    /** 产品主图片 */
    private String imageDefUrl;

    /** 产品价格 */
    private Double price;

    /** 产品属性 */
    private List<AttributesVo> attributesVos;

    public void addAttributesVos(AttributesVo attributesVos) {
        if (null == attributesVos) return;
        if (null == this.attributesVos) this.attributesVos = new ArrayList<>();
        this.attributesVos.add(attributesVos);
    }
}