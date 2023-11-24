package github.wx.v3.controller;

import github.wx.business.entity.Order;
import github.wx.v3.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author wx
 * @description
 * @date 2023/11/13 20:24
 */
@RestController
@RequestMapping("order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("get/{id}")
    public Order get(@PathVariable("id") Long id){
        return orderService.getOrderById(id);
    }

    @PostMapping(value = "update")
    public void updateOrder(@RequestBody Order order){
        orderService.updateOrder(order);
    }

    @DeleteMapping("del")
    public void del(@RequestParam("id") Long id){
        orderService.deleteOrder(id);
    }
}
