package github.wx.v3.service.impl;

import github.wx.business.constant.CacheConstant;
import github.wx.business.entity.Order;
import github.wx.business.mapper.OrderMapper;
import github.wx.v3.annotation.CacheType;
import github.wx.v3.annotation.DoubleCache;
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
 * @description  使用自定义注解的方式实现了两级缓存通过一个注解管理
 * @date 2023/11/13 17:11
 */

@Slf4j
@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderMapper orderMapper;


    @Override
    @DoubleCache(cacheName = "order", key = "#id", type = CacheType.FULL)
    public Order getOrderById(Long id) {
        return orderMapper.selectById(id);
    }

    @Override
    @DoubleCache(cacheName = "order", key = "#order.id", type = CacheType.PUT)
    public Order updateOrder(Order order) {
        log.info("update order data");
        orderMapper.updateById(order);
        return order;
    }

    @Override
    @DoubleCache(cacheName = "order", key = "#id", type = CacheType.DELETE)
    public void deleteOrder(Long id) {
        log.info("delete order");
        orderMapper.deleteById(id);
    }
}
