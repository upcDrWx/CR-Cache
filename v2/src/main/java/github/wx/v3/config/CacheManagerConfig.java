package github.wx.v3.config;


import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author wx
 * @description  spring项目提供了 CacheManager 接口和一些注解，允许让我们通过注解的方式来操作缓存。
 * @date 2023/11/13 16:17
 */
@Configuration
public class CacheManagerConfig {
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .initialCapacity(128)//初始大小
                .maximumSize(1024)//最大数量
                .expireAfterWrite(60, TimeUnit.SECONDS));
        return cacheManager;
    }
}
