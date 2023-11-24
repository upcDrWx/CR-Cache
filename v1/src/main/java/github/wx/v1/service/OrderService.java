package github.wx.v1.service;

import github.wx.business.entity.Order;

/**
 * @author wx
 * @description
 * @date 2023/11/13 16:41
 */
public interface OrderService {

    Order getOrderById(Long id);

    void updateOrder(Order order);

    void deleteOrder(Long id);

}
