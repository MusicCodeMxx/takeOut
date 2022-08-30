package top.starshine.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import top.starshine.commons.entity.product.Attributes;
import top.starshine.commons.entity.product.AttributesDto;

import java.util.List;

/**
 * <h3>产品属性</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/28  下午 1:02  周四
 * @Description: hello world
 */
public interface AttributesMapper extends BaseMapper<Attributes> {

    /**
     * 批量查询产品的属性信息
     * <p>关联表查询,各个表简称如下</p>
     * <li>product as t1</li>
     * <li>product_attributes as t2</li>
     * <li>attributes as t3</li>
     * @param queryWrapper 拼接对象
     * @return 返回产品属性列表
     */
    List<AttributesDto> findByProductIds(@Param(Constants.WRAPPER) QueryWrapper<Attributes> queryWrapper);

}
