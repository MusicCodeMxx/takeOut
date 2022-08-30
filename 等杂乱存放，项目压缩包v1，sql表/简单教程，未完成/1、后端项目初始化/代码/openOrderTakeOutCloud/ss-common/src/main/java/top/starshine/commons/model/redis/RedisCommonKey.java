package top.starshine.commons.model.redis;

/**
 * <h3> redis 缓存主键公共类 </h3>
 * @author: starshine
 * @version: 1.0
 * @since: 2022/7/6  上午 12:17  周三
 * @Description:
 */
public final class RedisCommonKey {

    /** 购物车缓存前缀 */
    public static final String SHOPPING_CART_CACHE_KEY_PREFIX = "user:shopping:cart:";

    /** 保存订单信息 */
    public static final String SAVE_TRADE_DETAILS_CACHE_PREFIX = "order:save:on:";

    /**登录黑名单主键前缀*/
    public static final String LOGIN_BLACK_LIST = "login:black:list:";

    /** 用户信息缓存前缀 */
    public static final String LOGIN_USER_DETAILS = "login:user:details:";

    /** 用缓存Redis有效期(单位分钟) */
    public static final long CACHE_MINUTES_30 = 30;

    private RedisCommonKey(){};

}
