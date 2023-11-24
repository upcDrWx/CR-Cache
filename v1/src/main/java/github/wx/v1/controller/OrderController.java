package github.wx.v1.controller;

import github.wx.business.entity.Order;
import github.wx.v1.service.OrderService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
