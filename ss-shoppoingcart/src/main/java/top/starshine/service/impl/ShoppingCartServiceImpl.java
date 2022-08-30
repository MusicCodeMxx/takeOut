package top.starshine.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import top.starshine.commons.converter.ShoppingCartConverter;
import top.starshine.commons.dubbo.ProductDubboService;
import top.starshine.commons.entity.product.Product;
import top.starshine.commons.entity.shoppingcar.ShoppingCartBo;
import top.starshine.commons.entity.shoppingcar.ShoppingCartVo;
import top.starshine.commons.entity.user.User;
import top.starshine.commons.exception.BadRequestException;
import top.starshine.commons.exception.InternalServerErrorException;
import top.starshine.commons.handle.ThreadLocalCache;
import top.starshine.commons.model.redis.CachePrefix;
import top.starshine.commons.status.ShoppingCartStatus;
import top.starshine.commons.util.CollectionUtils;
import top.starshine.service.ShoppingCartService;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * <h3>购物车实现类</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/28  下午 3:40  周四
 * @Description: hello world
 */
@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final RedisTemplate redisTemplate;
    private final ThreadPoolExecutor threadPoolExecutor;
    private final ShoppingCartConverter shoppingCartConverter;

    @DubboReference(cluster = "failback", interfaceClass = ProductDubboService.class,
            interfaceName = "top.top.starshine.dubbo.ProductDubboService", version = "1.0.0")
    private ProductDubboService productDubboService;

    @Override
    public List<ShoppingCartVo> list() {
        String cacheKey = CachePrefix.USER_SHOPPING_CART + ((User) ThreadLocalCache.getNotNull()).getId();
        HashOperations<String,String,Object> hashOperations = redisTemplate.opsForHash();

        try {
            // 获取用户购物车的 map key
            CompletableFuture<Set<String>> cacheKeys = CompletableFuture.supplyAsync(() -> {
                Set<String> keys = hashOperations.keys(cacheKey);
                if (CollectionUtils.isEmpty(keys)) throw new BadRequestException(ShoppingCartStatus.IS_EMPTY);
                    return keys;
            }, threadPoolExecutor);

            // 使用父线程, 远程调用产品服务, 批量查询产品信息
            CompletableFuture<List<Product>> productsFuture = cacheKeys.thenApply( keys -> productDubboService.findByIds(keys));

            // 使用新的线程, 通过获取的主键去找出值, 批量查询
            CompletableFuture<List<Object>> objectsFuture = cacheKeys.thenApplyAsync( keys -> hashOperations.multiGet(cacheKey, keys),threadPoolExecutor);

            // 使用线程是, 最后完成的线程
            return productsFuture.thenCombine(objectsFuture, (products, cartData) -> {
                // 检查是否都获取到数据
                if (CollectionUtils.isEmpty(products) || CollectionUtils.isEmpty(cartData))
                    throw new InternalServerErrorException(ShoppingCartStatus.PRODUCT_IS_INVALID);

                // 针对和集合进行合并操作
                List<ShoppingCartBo> shoppingCartBos = new ArrayList<>();
                for (Object item : cartData) {
                    if (item instanceof ShoppingCartBo) shoppingCartBos.add((ShoppingCartBo) item);
                    if (item instanceof List) shoppingCartBos.addAll((Collection<? extends ShoppingCartBo>) item);
                }
                cartData = null;

                return shoppingCartBos.stream().filter(Objects::nonNull).map(bo -> {
                    // 转视图对象
                    ShoppingCartVo vo = shoppingCartConverter.shoppingCartBoToShoppingCartVo(bo);
                    // 找出对应的产品信息进行组装
                    for (Product product : products) {
                        if (product.getId().equals(bo.getId())){
                            shoppingCartConverter.copyProductToShoppingCartVo(product,vo);
                            return vo;
                        }
                    }
                    return vo;
                }).sorted(Comparator.comparing(ShoppingCartVo::getId).reversed()).collect(Collectors.toList());
            }).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    // id : 1397850851245600769 value : 不要蒜,不辣
    @Override
    public int add(Long id, String value) {
        Long userId =  ((User)ThreadLocalCache.getNotNull()).getId();
        String cacheKey = CachePrefix.USER_SHOPPING_CART;
        HashOperations<String,String,Object> hashCache = redisTemplate.opsForHash();
        // 存储到 redis 散列集合, 用户为缓存主键, 商品为集合主键, 属性为值
        Object cacheObject = hashCache.get(CachePrefix.USER_SHOPPING_CART + userId, String.valueOf(id));
        // 缓存中无该产品
        if( null == cacheObject){
            hashCache.put(cacheKey + userId, String.valueOf(id), new ShoppingCartBo(id, value, 1));
            return 1;
        }
        // 对象状态处理
        if (cacheObject instanceof ShoppingCartBo) return objectAdd(hashCache, userId, cacheKey, id, value, (ShoppingCartBo) cacheObject);
        // 数组集合状态处理
        return listAdd(hashCache, userId, cacheKey, id, value, (List<ShoppingCartBo>) cacheObject);
    }

    @Async
    @Override
    public void clear(User user) {
        if (null == user) return;
        redisTemplate.delete(CachePrefix.USER_SHOPPING_CART + user.getId());
    }

    @Override
    public int subtract(Long id, String value) {
        Long userId =  ((User)ThreadLocalCache.getNotNull()).getId();
        String cacheKey = CachePrefix.USER_SHOPPING_CART;
        HashOperations<String,String,Object> hashCache = redisTemplate.opsForHash();
        // 存储到 redis 散列集合, 用户为缓存主键, 商品为集合主键, 属性为值
        Object cacheObject = hashCache.get(CachePrefix.USER_SHOPPING_CART + userId, String.valueOf(id));
        // 缓存中无该产品
        if( null == cacheObject) return 0;
        // 对象状态处理
        if (cacheObject instanceof ShoppingCartBo) return objectSubtract(hashCache, userId, cacheKey, id, value, (ShoppingCartBo) cacheObject);
        // 数组集合状态处理
        return listSubtract(hashCache, userId, cacheKey, id, value, (List<ShoppingCartBo>) cacheObject);
    }

    /**
     * 针对对象版本的减少或删除记录
     * @param hashCache redis对象
     * @param userId 用户主键
     * @param cacheKey 缓存主键
     * @param id 产品主键
     * @param value 产品属性值
     * @param cache 缓存集合对象
     * @return 当前的产品数量
     */
    private int listSubtract(HashOperations<String,String,Object> hashCache, Long userId,  String cacheKey, Long id, String value, List<ShoppingCartBo> cache) {
        int sameCount = 0;
        ShoppingCartBo one = null;
        for (ShoppingCartBo sc : cache) {
            if (eq(value, sc.getValue())) {
                if (sameCount == 0) one = sc;// 引用第一个查找到的对象
                sameCount++;
            }
        }
        // 没有找到相同
        if (sameCount == 0) return 0;
        // 找到一个相同的
        if (sameCount == 1) {
            // 检查操作边界
            if (isConformAmount(one.getAmount())){
                // 如果集合中只有一条记录, 直接删除
                if (cache.size() == 1) hashCache.delete(cacheKey + userId, String.valueOf(id));
                // 如果集合中只有一条记录, 取出第一个存入缓存
                if (cache.size() == 2) hashCache.put(cacheKey + userId, String.valueOf(id), cache.get(0));
                // 如果集合中记录大于 2 的
                if (cache.size() > 2) {
                    cache.remove(one);// 删除该对象
                    hashCache.put(cacheKey + userId, String.valueOf(id), cache);
                }
                return 0;
            }
            // 减少数量操作, 如果这里你看不懂, 你基础请重温
            one.setAmount(one.getAmount() - 1);
            hashCache.put(cacheKey + userId, String.valueOf(id), cache);
            return one.getAmount();
        }
        // 相同属性的产品有多个要合并处理
        if (sameCount > 1) {
            // 分类不相同记录, 相同属性值的累计数量
            int amount = 0;
            List<ShoppingCartBo> temp =  new ArrayList<>(cache.size());
            for (ShoppingCartBo sc : cache) {
                if (eq(value, sc.getValue())) {
                    amount += sc.getAmount();
                }else{
                    temp.add(sc);
                }
            }
            // 若合并之后不相同属性值记录为零, 转为对象
            if (CollectionUtils.isEmpty(temp)){
                // 检查操作边界
                if (isConformAmount(amount)){
                    hashCache.delete(cacheKey + userId,String.valueOf(id));
                    return 0;
                }
                one.setAmount(amount - 1);
                hashCache.put(cacheKey + userId, String.valueOf(id), one);
                return one.getAmount();
            }
            cache.clear();// 清除原来的数组的数据
            // 检查操作边界
            if (!isConformAmount(amount)){
                one.setAmount(amount - 1);
                cache.add(one);// 将对象存入数组
            }
            cache.addAll(temp);// 将不相同的属性值数组存入
            temp = null;
        }
        hashCache.put(cacheKey + userId, String.valueOf(id), cache);
        return one.getAmount();

    }

    /**
     * 针对对象版本的减少或删除数量记录
     * @param hashCache redis对象
     * @param userId 用户主键
     * @param cacheKey 缓存主键
     * @param id 产品主键
     * @param value 产品属性值
     * @param cache 缓存对象
     * @return 当前的产品数量
     */
    private int objectSubtract(HashOperations<String, String, Object> hashCache, Long userId, String cacheKey, Long id, String value, ShoppingCartBo cache) {
        // 如果存入的属性是 null 和 缓存中的属性也是 null 或者 属性都是相同
        if (eq(value, cache.getValue())) {
            if (isConformAmount(cache.getAmount())) {
                hashCache.delete(cacheKey + userId, String.valueOf(id));
                return 0;
            }
            cache.setAmount(cache.getAmount() - 1);
            hashCache.put(cacheKey + userId, String.valueOf(id), cache);
            return cache.getAmount();
        }
        return 0;
    }

    /**
     * 差是否符合减少产品数量的边界
     * @param amount 当前数量
     * @return true 不符合操作, false 符合操作
     */
    private boolean isConformAmount(Integer amount){
        return null == amount || 1 >= amount;
    }

    /**
     * 针对对象版本的添加或增加数量记录
     * @param hashCache redis对象
     * @param userId 用户主键
     * @param cacheKey 缓存主键
     * @param id 产品主键
     * @param value 产品属性值
     * @param cache 缓存集合对象
     * @return 当前的产品数量
     */
    private int listAdd(HashOperations<String,String,Object> hashCache, Long userId,  String cacheKey, Long id, String value, List<ShoppingCartBo> cache) {
        // 计数：有多少个相同的属性值的产品
        int sameCount = 0;
        ShoppingCartBo one = null;
        for (ShoppingCartBo sc : cache) {
            if (eq(value, sc.getValue())) {
                // 只有在第一个才能修改, 后面的不能修改
                if (sameCount == 0) {
                    one = sc;
                    sc.setAmount(sc.getAmount() + 1);
                }
                sameCount++;
            }
        }
        // 没有找到修改的
        if (sameCount == 0){
            one = new ShoppingCartBo(id, value, 1);
            cache.add(one);
        }
        // 相同属性的产品有多个要合并处理
        if (sameCount > 1) {
            // 分类不相同记录, 相同属性值的累计数量
            int amount = 1;
            List<ShoppingCartBo> temp =  new ArrayList<>(cache.size());
            for (ShoppingCartBo sc : cache) {
                if (eq(value, sc.getValue())) {
                    amount += sc.getAmount();
                }else{
                    temp.add(sc);
                }
            }
            one.setAmount(amount);
            // 若合并之后不相同属性值记录为零, 转为对象
            if (CollectionUtils.isEmpty(temp)){
                hashCache.put(cacheKey + userId, String.valueOf(id), one);
                return one.getAmount();
            }
            cache.clear();// 清除原来的数组集合
            cache.addAll(temp);// 将不相同的属性值存入
            cache.add(one);// 将合并对象存入
            temp = null;
        }
        hashCache.put(cacheKey + userId, String.valueOf(id), cache);
        return one.getAmount();
    }

    /**
     * 针对对象版本的增加或添加数量记录
     * @param hashCache redis对象
     * @param userId 用户主键
     * @param cacheKey 缓存主键
     * @param id 产品主键
     * @param value 产品属性值
     * @param cache 缓存对象
     * @return 当前的产品数量
     */
    private int objectAdd(HashOperations<String, String, Object> hashCache, Long userId, String cacheKey, Long id, String value, ShoppingCartBo cache) {
        // 如果存入的属性是 null 和 缓存中的属性也是 null 或者 属性都是相同
        if (eq(value, cache.getValue())) {
            cache.setAmount(cache.getAmount() + 1);
            hashCache.put(cacheKey + userId, String.valueOf(id), cache);
            return cache.getAmount();
        }
        // 如果不相同就创建对象, 并且合并成数组集合
        hashCache.put(cacheKey + userId, String.valueOf(id), Arrays.asList(new ShoppingCartBo(id, value, 1), cache));
        return 1;
    }

    /**
     * 检查 vOne 和 vTwo 是否相等
     * @param vOne 被比较值
     * @param vTwo 比较值
     * @return true 相等, false不相等
     */
    private boolean eq(String vOne,String vTwo){
        return (null != vOne && vOne.equals(vTwo)) || (null == vOne && null == vTwo);
    }

}
