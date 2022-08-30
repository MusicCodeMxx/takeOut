package top.starshine.commons.util;

import org.springframework.lang.Nullable;

import java.util.Collection;
import java.util.Map;

/**
 * 集合工具类
 * @author: starshine
 * @version: 1.0
 * @since: 2022/7/22  下午 5:30  周五
 * @Description:
 */
public final class CollectionUtils {

    private CollectionUtils(){}

    /**
     * 检查集合为空
     * @param collection 集合
     * @return 布尔
     */
    public static boolean isEmpty(@Nullable Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 检查集合不为空
     * @param collection 集合
     * @return 布尔
     */
    public static boolean isNotEmpty(@Nullable Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * 检查集合不为空
     * @param collection 集合
     * @return 布尔
     */
    public static boolean isEmpty(@Nullable Map<?, ?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 检查集合不为空
     * @param collection 集合
     * @return 布尔
     */
    public static boolean isNotEmpty(@Nullable Map<?, ?> collection) {
        return !isEmpty(collection);
    }

}
