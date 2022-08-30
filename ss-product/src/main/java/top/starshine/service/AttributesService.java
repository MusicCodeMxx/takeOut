package top.starshine.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.starshine.commons.entity.product.Attributes;
import top.starshine.commons.entity.product.AttributesDto;

import java.util.List;
import java.util.Set;

/**
 * <h3>产品属性</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/28  下午 1:02  周四
 * @Description: hello world
 */
public interface AttributesService extends IService<Attributes> {

    /**
     * 查询多个产品主键的产品属性
     * @param productIds 多个产品的主键
     * @return 产品属性集合
     */
    List<AttributesDto> findProductPropertiesByIds(Set<Long> productIds);

}
