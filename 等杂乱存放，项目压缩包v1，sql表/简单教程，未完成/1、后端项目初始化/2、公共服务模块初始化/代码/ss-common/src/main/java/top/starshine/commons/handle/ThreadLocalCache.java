package top.starshine.commons.handle;

import top.starshine.commons.exception.InternalServerErrorException;
import top.starshine.commons.status.R;
import top.starshine.commons.status.UserStatus;

/**
 * <h3>本地线程缓存工具类</h3>
 * <ul>
 *     <li>本地线程缓存: ThreadLocal 底层类似 Map</li>
 *     <li>key 是弱引用, 容易被 GC 干掉</li>
 *     <li>value 非弱引用, 谨记使用完之后调用移除方法</li>
 * </ul>
 * @author: starshine
 * @version: 1.0
 * @since: 2022/6/22 下午 8:41  周三
 * @Description:
 */
public final class ThreadLocalCache {

    private static final ThreadLocal<Object> THREAD_LOCAL_CACHE = new ThreadLocal<Object>();

    private ThreadLocalCache(){};

    /**
     * 检查缓存中是否有用户数据
     * <li>返回真,数据存在</li>
     * <li>返回假,数据不存在</li>
     * @return {@link boolean} true 数据存在, false 数据不存在
     */
    public static boolean isNotNull(){ return THREAD_LOCAL_CACHE.get() != null; }

    /**
     * 存入本地线程缓存
     * @param value 存入对象
     */
    public static void put(Object value) {
        if (null != value) THREAD_LOCAL_CACHE.set(value);
    }

    /**
     * 获取本地线程缓存
     * @return 对象
     */
    public static Object getObject() {
        return THREAD_LOCAL_CACHE.get();
    }

    /**
     * <h3>获取本地线程缓存缓存对象</h3>
     * @param <T> 泛型
     * @return 缓存中存储的值
     */
    public static <T> T get() {
        return (T) THREAD_LOCAL_CACHE.get();
    }

    /**
     * 获取本地线程缓存
     * <li>获取缓存时,判断是否为空,如果为空,就抛出异常</li>
     * @return 对象
     */
    public static <T> T getNotNull() {
        return (T)getNotNull(UserStatus.GET_LOCAL_CACHE_IS_EMPTY);
    }

    /**
     * 获取本地线程缓存 - 抛出指定内容
     * <li>获取缓存时,判断是否为空,如果为空,就抛出异常</li>
     * @return 对象
     */
    public static Object getNotNull(String message) {
        Object cache = THREAD_LOCAL_CACHE.get();
        if (cache == null) throw new RuntimeException(message);
        return cache;
    }

    /**
     * 获取本地线程缓存 - 抛出指定内容
     * <li>获取缓存时,判断是否为空,如果为空,就抛出异常</li>
     * @return 对象
     */
    public static Object getNotNull(Integer code,String message) {
        Object cache = THREAD_LOCAL_CACHE.get();
        if (cache == null) throw new InternalServerErrorException(code,message);
        return cache;
    }

    /**
     * 获取本地线程缓存 - 传入枚举状态类
     * <li>获取缓存时,判断是否为空,如果为空,就抛出异常</li>
     * @return 对象
     */
    public static Object getNotNull(R r) {
        Object cache = THREAD_LOCAL_CACHE.get();
        if (cache == null){ throw new InternalServerErrorException(r); }
        return cache;
    }

    /** 移除本地线程缓存 */
    public static void remove() {
        THREAD_LOCAL_CACHE.remove();
    }

}
