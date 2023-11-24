package github.wx.v4.cache;


import com.github.benmanes.caffeine.cache.Cache;
import github.wx.v4.config.DoubleCacheConfig;
import github.wx.v4.config.MessageConfig;
import github.wx.v4.msg.CacheMessage;
import github.wx.v4.msg.CacheMsgType;
import github.wx.v4.util.MessageSourceUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.data.redis.core.RedisTemplate;

import java.net.UnknownHostException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author wx
 * @description
 * @date 2023/11/15 15:42
 */
@Slf4j
public class DoubleCache extends AbstractValueAdaptingCache {
    private String cacheName;
    private RedisTemplate<Object, Object> redisTemplate;
    private Cache<Object, Object> caffeineCache;
    private DoubleCacheConfig doubleCacheConfig;

    protected DoubleCache(boolean allowNullValues) {
        super(allowNullValues);
    }

    public DoubleCache(String cacheName, RedisTemplate<Object, Object> redisTemplate,
                       Cache<Object, Object> caffeineCache,
                       DoubleCacheConfig doubleCacheConfig) {
        super(doubleCacheConfig.getAllowNull());
        this.cacheName = cacheName;
        this.redisTemplate = redisTemplate;
        this.caffeineCache = caffeineCache;
        this.doubleCacheConfig = doubleCacheConfig;
    }

    @Override
    protected Object lookup(Object key) {
        // 先从caffeine中查找
        Object object = caffeineCache.getIfPresent(key);
        if (Objects.nonNull(object)) {
            log.info("get data from caffeine");
            return object; //不用 fromStoreValue，否则返回的是null，会再查数据库
        }

        //再从redis中查找
        String redisKey = this.cacheName + ":" + key;
        object = redisTemplate.opsForValue().get(redisKey);
        if (Objects.nonNull(object)) {
            log.info("get data from redis");
            caffeineCache.put(key, object);
        }
        return object;
    }

    @Override
    public String getName() {
        return this.cacheName;
    }

    @Override
    public Object getNativeCache() {
        return this;
    }

    //使用注解时不走这个方法，实际走父类的get方法，父类 get 方法调用了 lookup 方法
    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        ReentrantLock lock = new ReentrantLock();
        try {
            lock.lock();
            Object obj = lookup(key);
            if (Objects.nonNull(obj)) {
                return (T) obj;
            }
            // 没有找到
            obj = valueLoader.call();
            //放入缓存
            put(key, obj);
            return (T) obj;
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            lock.unlock();
        }
        return null;
    }

    @Override
    public void put(Object key, Object value) {
        if (!isAllowNullValues() && Objects.nonNull(value)) {
            log.error("the value NULL will not be cached");
            return;
        }

        //使用 toStoreValue(value) 包装，解决caffeine不能存null的问题
        caffeineCache.put(key, toStoreValue(value));

        // null对象只存在caffeine中一份就够了，不用存redis了
        if (Objects.isNull(value))
            return;

        String redisKey = this.cacheName + ":" + key;

        // 从 doubleCacheConfig 对象中获取 redisExpire 属性值，
        // 并将其封装到一个 Optional<Long> 对象中。
        // 这样的设计可以有效地处理可能为 null 的情况，避免了空指针异常
        Optional<Long> expireOpt = Optional.ofNullable(doubleCacheConfig)
                .map(DoubleCacheConfig::getRedisExpire);
        if (expireOpt.isPresent()) {
            redisTemplate.opsForValue().set(redisKey, toStoreValue(value),
                    expireOpt.get(), TimeUnit.SECONDS);
        } else {
            redisTemplate.opsForValue().set(redisKey, toStoreValue(value));
        }

        //发送信息通知其他节点更新一级缓存
        //同样，空对象不会给其他节点发送信息

        try {
            CacheMessage cacheMessage =
                    new CacheMessage(this.cacheName, CacheMsgType.UPDATE,
                            key, value, MessageSourceUtil.getMsgSource());
            redisTemplate.convertAndSend(MessageConfig.TOPIC, cacheMessage);

        } catch (UnknownHostException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void evict(Object key) {
        redisTemplate.delete(this.cacheName +":"+ key);
        caffeineCache.invalidate(key);

        //发送信息通知其他节点删除一级缓存
        try {
            CacheMessage cacheMessage =
                    new CacheMessage(this.cacheName, CacheMsgType.DELETE,
                            key, null, MessageSourceUtil.getMsgSource());
            redisTemplate.convertAndSend(MessageConfig.TOPIC, cacheMessage);

        } catch (UnknownHostException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void clear() {
        // 如果是正式环境，避免使用 keys 命令
        Set<Object> keys = redisTemplate.keys(this.cacheName.concat(":*"));
        for (Object key : keys) {
            redisTemplate.delete(String.valueOf(key));
        }
        caffeineCache.invalidateAll();
    }

    // 更新一级缓存
    public void updateL1Cache(Object key, Object value) {
        caffeineCache.put(key, value);
    }

    // 删除一级缓存
    public void evictL1Cache(Object key) {
        caffeineCache.invalidate(key);
    }

}
