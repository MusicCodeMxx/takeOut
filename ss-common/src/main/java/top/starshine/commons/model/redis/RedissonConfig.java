package top.starshine.commons.model.redis;

import lombok.RequiredArgsConstructor;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <h3> redis 分布式锁</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/30  下午 2:48  周六
 * @Description: hello world
 */
@Configuration
@RequiredArgsConstructor
public class RedissonConfig {

    private final RedisProperties redisProperties;

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient () {
        // 创建配置
        Config config = new Config();
        // 必须添加连接协议
        SingleServerConfig serverConfig = config.useSingleServer();
        // redis 服务器地址和端口
        serverConfig.setAddress("redis://"+redisProperties.getHost()+":"+redisProperties.getPort());
        // redis 数据库
        serverConfig.setDatabase(redisProperties.getDatabase());
        // redis 链接密码
        serverConfig.setPassword(redisProperties.getPassword());
        // 创建实例
        return Redisson.create(config);
    }

}
