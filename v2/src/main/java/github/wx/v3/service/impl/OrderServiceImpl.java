package github.wx.v3.service.impl;

import github.wx.business.constant.CacheConstant;
import github.wx.business.entity.Order;
import github.wx.business.mapper.OrderMapper;
import github.wx.v3.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author wx
 * @description 基于 spring 提供的 CacheManager 注解和手动操作 Redis 实现两级缓存
 * @date 2023/11/13 17:11
 */

@Slf4j
@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderMapper orderMapper;
    private final RedisTemplate redisTemplate;

    /**
     * value 和 cacheNames 互为别名关系，表示当前方法的结果会被缓存在哪个 Cache 上，
     * 应用中通过 cacheName 来对 Cache 进行隔离，每个 cacheName 对应一个 Cache 实现。
     * value 和 cacheNames 可以是一个数组，绑定多个 Cache。
     */
    @Override
//    @Cacheable(value = "order",key = "#id")
    @Cacheable(cacheNames = "order", key = "#id")
    public Order getOrderById(Long id) {
        String key = CacheConstant.ORDER + id;
        // 查 Redis
        Object obj = redisTemplate.opsForValue().get(key);
        if (Objects.nonNull(obj)) {
            log.info("get data from redis");
            return (Order) obj;
        }
        // Redis没有则查询 DB
        log.info("get data from database");
        Order order = orderMapper.selectById(id);
        redisTemplate.opsForValue().set(key, order, 120, TimeUnit.SECONDS);
        return order;
    }

    @Override
    @CachePut(cacheNames = "order", key = "#order.id")
    public Order updateOrder(Order order) {
        log.info("update order data");
        String key = CacheConstant.ORDER + order.getId();
        orderMapper.updateById(order);
        //修改 Redis
        redisTemplate.opsForValue().set(key, order, 120, TimeUnit.SECONDS);
        //修改 本地缓存
        return order;
    }

    @Override
    @CacheEvict(cacheNames = "order", key = "#id")
    public void deleteOrder(Long id) {
        log.info("delete order");
        String key = CacheConstant.ORDER + id;
        orderMapper.deleteById(id);
        redisTemplate.delete(key);
    }
}
