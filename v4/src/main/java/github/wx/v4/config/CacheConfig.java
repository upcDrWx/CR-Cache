package github.wx.v4.config;

import github.wx.v4.cache.DoubleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author wx
 * @description  cacheManager 配置类
 * @date 2023/11/15 15:45
 */
@Configuration
public class CacheConfig {

    @Bean
    public DoubleCacheManager cacheManager(RedisTemplate<Object,Object> redisTemplate,
                                           DoubleCacheConfig doubleCacheConfig){
        return new DoubleCacheManager(redisTemplate,doubleCacheConfig);
    }

}
