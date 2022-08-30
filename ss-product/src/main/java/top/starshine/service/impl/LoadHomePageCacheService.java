package top.starshine.service.impl;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import top.starshine.commons.converter.ProductConverter;
import top.starshine.commons.entity.product.*;
import top.starshine.commons.model.redis.CachePrefix;
import top.starshine.commons.properties.SystemSettingProperties;
import top.starshine.commons.util.CollectionUtils;
import top.starshine.service.AttributesService;
import top.starshine.service.CategoryService;
import top.starshine.service.ProductService;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.*;

/**
 * <h3>加载首页的请求合并统一处理</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/30  下午 2:09  周六
 * @Description: hello world
 */
@Service
@RequiredArgsConstructor
public class LoadHomePageCacheService {

    private final RedisTemplate redisTemplate;
    private final RedissonClient redissonClient;
    private final ProductService productService;
    private final CategoryService categoryService;
    private final ProductConverter productConverter;
    private final AttributesService attributesService;
    private final ThreadPoolExecutor threadPoolExecutor;
    private final SystemSettingProperties systemSettingProperties;

    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
    private LinkedBlockingQueue<CompletableFuture<HomePageCacheVo>> queue = new LinkedBlockingQueue<>(200);

    private static final String LOAD_HOME_PAGE_CACHE_LOCK_KEY = "lock:load:home:page:cache";
    private static boolean isNewCache = false;

    // 后构造
    @PostConstruct
    public void init(){
        // 定期执行任务, 固定 6 毫秒执行一次
        scheduledExecutorService.scheduleAtFixedRate(()->{
            int size = queue.size();// 获取队列数量
            if (size == 0) return;// 若获取零, 表示这段时间没有请求, 直接返回
            // 获取缓存
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            HomePageCacheVo cache = (HomePageCacheVo) redisTemplate.opsForValue().get(CachePrefix.HOME_PAGE_CACHE);
            // 检查缓存是否存在
            if (null == cache) {
                // 获取锁
                RLock lock = redissonClient.getLock(LOAD_HOME_PAGE_CACHE_LOCK_KEY);
                try{
                    lock.lock();// 加锁
                    cache = getHomePageCacheVo();// 查询数据库
                    // 存入缓存, 30 分钟有效
                    if (null != cache) operations.set(CachePrefix.HOME_PAGE_CACHE, cache, systemSettingProperties.getHomePageCacheTime(), TimeUnit.MINUTES);
                    isNewCache = true;// 关闭检查
                }finally {
                    lock.unlock();// 解锁 将设解锁代码没有运行，reidsson会不会出现死锁
                }
            }
            // 将队列的请求进行消费
            for (int i = 0; i < size; i++) {
                Objects.requireNonNull(queue.poll()).complete(cache);
            }
            // 异步执行维持缓存有效时间
            leaseRenewalCache();
        }, 0, 6, TimeUnit.MILLISECONDS);
        // initialDelay 初始化延时, period 两次开始执行最小间隔时间, unit：计时单位
    }

    /**
     * 检查是否续期缓存
     */
    private void leaseRenewalCache(){
        try {
            // 如果是从数据库读缓存了, 就不要再检查了
            if (isNewCache){
                isNewCache = false;
                return;
            }
            // 开异步去检查
            CompletableFuture.runAsync(()->{
                // 获取缓存有效时间
                Long time = redisTemplate.getExpire(CachePrefix.HOME_PAGE_CACHE, TimeUnit.SECONDS);
                // 续期条件 6 ~ 60 秒
                if ( null == time || time == -2L || 6L > time || time > 60L) return;
                // 续期操作
                redisTemplate.expire(CachePrefix.HOME_PAGE_CACHE, systemSettingProperties.getHomePageCacheTime(), TimeUnit.MINUTES);
                time = null;
            },threadPoolExecutor);
        } catch (Exception ignored) {}
    }

    /**
     * 加载
     * @return 首页的分类和产品数据
     */
    public HomePageCacheVo load() {
        CompletableFuture<HomePageCacheVo> future = null;
        try {
            future = new CompletableFuture<>();// 创建异步信息
            queue.add(future);//将对象传入队列
            return future.get();// 获取值
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        } finally {
            future = null;
        }
    }

    /**
     * 获取分类和产品数据
     * @return 首页数据
     */
    private HomePageCacheVo getHomePageCacheVo(){
        HomePageCacheVo homePageCacheVo = new HomePageCacheVo();
        // 查询分类
        List<Category> categories = categoryService.queryList();
        if (CollectionUtils.isEmpty(categories)) return null;
        homePageCacheVo.setCategoryVos(productConverter.categorysToCategoryVos(categories));
         try {
             // 获取第一个分类, 调用产品服务查询属于该分离的产品列表
             List<Product> products = productService.listByCategoryId(categories.get(0).getId());
             categories = null;
             // 查询产品属性
             List<ProductVo> productVos = productConverter.productsToProductVos(products);
             Set<Long> productIds = new HashSet<>(products.size());
             // 获取产品主键
             for (Product product : products) {
                 productIds.add(product.getId());
             }
             products = null;
             // 查询产品的属性
             List<AttributesDto> attributesList = attributesService.findProductPropertiesByIds(productIds);
             productIds = null;
             if (CollectionUtils.isNotEmpty(attributesList)) {
                 // 组合归队
                 for (ProductVo productVo : productVos) {
                     for (AttributesDto attributesDto : attributesList) {
                         if (attributesDto.getProductId().equals(Long.valueOf(productVo.getId()))) {
                             // 转换
                             productVo.addAttributesVos(productConverter.attributesDtoToAttributesVo(attributesDto));
                         }
                     }
                 }
             }
             homePageCacheVo.setProductVos(productVos);
         }catch (Exception e){
             e.printStackTrace();
         }
         return homePageCacheVo;
    }

}
