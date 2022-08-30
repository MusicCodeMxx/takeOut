package top.starshine.commons.model.redis;

/**
 * <h3>缓存前缀</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/26  下午 8:33  周二
 * @Description: hello world
 */
public final class CachePrefix {

    public static final String LOGIN_VERIFICATION_CODE = "login:verification:code:";

    public static final String LOGIN_STATUS_USER_DETAIL = "login:status:user:detail:";

    public static final String LOGOUT_STATUS_USER_LIST = "logout:status:user:list:";

    public static final String USER_SHOPPING_CART = "user:shopping:cart:";

    public static final String HOME_PAGE_CACHE = "home:page:cache";

    public static final String RABBIT_MQ_ORDER_SAVE = "rabbit:mq:order:save:";

    private CachePrefix(){}
}
