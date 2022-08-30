package top.starshine.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.starshine.commons.entity.product.Attributes;
import top.starshine.commons.entity.product.AttributesDto;
import top.starshine.commons.util.CollectionUtils;
import top.starshine.mapper.AttributesMapper;
import top.starshine.service.AttributesService;

import java.util.List;
import java.util.Set;

/**
 * <h3>产品属性</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/28  下午 1:03  周四
 * @Description: hello world
 */
@Service
public class AttributesServiceImpl extends ServiceImpl<AttributesMapper, Attributes> implements AttributesService {

    @Override
    public List<AttributesDto> findProductPropertiesByIds(Set<Long> productIds) {
        if (CollectionUtils.isEmpty(productIds)) return null;
        QueryWrapper<Attributes> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("t1.id",productIds);
        queryWrapper.eq("t1.status",1);
        queryWrapper.eq("t3.is_delete",0);
        return getBaseMapper().findByProductIds(queryWrapper);
    }

}
