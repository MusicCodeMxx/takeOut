package top.starshine.commons.model.mybatisplus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import top.starshine.commons.entity.user.User;
import top.starshine.commons.handle.ThreadLocalCache;

import java.util.Date;

/**
 * mybatis plus 提供的字段自动填充功能
 *
 * @author: starshine
 * @version: 1.0
 * @since: 2022/3/10  下午 3:06  周四
 * @Description:
 */
@Component
public class MybatisPlusDataAutoFillHandler implements MetaObjectHandler  {

    /**
     * 插入填充
     *
     * @param metaObject 元对象
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        Date date = new Date();
        // 起始版本 3.3.0(推荐使用)
        this.strictInsertFill(metaObject, "createTime", Date.class, date);
        this.strictInsertFill(metaObject, "updateTime", Date.class, date);
        // 默认删除为未删除,0未删除,1已删除
        this.strictInsertFill(metaObject, "isDelete", Integer.class, 0);
        date = null;
        Long id  = this.getUserId();
        this.strictInsertFill(metaObject, "updateById", Long.class, id);
        this.strictInsertFill(metaObject, "createById", Long.class, id);
    }

    /**
     * 更新填充
     *
     * @param metaObject 元对象
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        // 起始版本 3.3.0(推荐使用)
        this.strictUpdateFill(metaObject, "updateTime", Date.class, new Date());
        this.strictInsertFill(metaObject, "updateById", Long.class, this.getUserId());
    }

    private Long getUserId(){
        User user = ThreadLocalCache.get();
        return ( null == user )? -1L : user.getId() ;
    }
}
