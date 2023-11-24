package github.wx.v1.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import github.wx.business.constant.CacheConstant;
import github.wx.business.entity.Order;
import github.wx.business.mapper.OrderMapper;
import github.wx.v1.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author wx
 * @description  基于 caffeine 和 Redis 手动实现两级缓存
 * @date 2023/11/13 17:11
 */

@Slf4j
@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderMapper orderMapper;
    private final Cache cache;
    private final RedisTemplate redisTemplate;

    @Override
    public Order getOrderById(Long id) {
        String key = CacheConstant.ORDER + id;
        Order order = (Order) cache.get(key, k -> {
            //先查询 Redis
            Object obj = redisTemplate.opsForValue().get(k);
            if (Objects.nonNull(obj)) {
                log.info("get data from redis");
                return obj;
            }
            // Redis没有则查询 DB
            log.info("get data from database");
            Order order1 = orderMapper.selectById(id);
            redisTemplate.opsForValue().set(k, order1, 120, TimeUnit.SECONDS);
            return order1;
        });

        return order;
    }

    @Override
    public void updateOrder(Order order) {
        log.info("update order data");
        String key = CacheConstant.ORDER + order.getId();
        orderMapper.updateById(order);
        //修改 Redis
        redisTemplate.opsForValue().set(key, order, 120, TimeUnit.SECONDS);
        //修改 本地缓存
        cache.put(key, order);
    }

    @Override
    public void deleteOrder(Long id) {
        log.info("delete order");
        String key = CacheConstant.ORDER + id;
        orderMapper.deleteById(id);
        redisTemplate.delete(key);
        cache.invalidate(key);
    }
}
