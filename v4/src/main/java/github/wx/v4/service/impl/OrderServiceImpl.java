package github.wx.v4.service.impl;

import github.wx.business.entity.Order;
import github.wx.business.mapper.OrderMapper;
import github.wx.v4.cache.DoubleCacheManager;
import github.wx.v4.service.OrderService;
import github.wx.v4.util.SpringContextUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

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
    @Cacheable(value = "order",key = "#id")
    public Order getOrderById(Long id) {
        log.info("get data from database");
        return orderMapper.selectById(id);
    }

    @Override
    @CachePut(cacheNames = "order",key = "#order.id")
    public Order updateOrder(Order order) {
        log.info("update order data");
        orderMapper.updateById(order);
        return order;
    }

    @Override
    @CacheEvict(cacheNames = "order",key = "#id")
    public void deleteOrder(Long id) {
        log.info("delete order");
        orderMapper.deleteById(id);
    }

    /**
     * 非注解方式使用缓存，实际调用 cache 的 get 方法
     */
    public Order getOrderByIdNonAnnotation(Long id) {
        DoubleCacheManager cacheManager = SpringContextUtil.getBean(DoubleCacheManager.class);
        Cache cache = cacheManager.getCache("order");
        Order order = cache.get(id, () -> {
            log.info("get data from database");
            return orderMapper.selectById(id);
        });
        return order;
    }
}
