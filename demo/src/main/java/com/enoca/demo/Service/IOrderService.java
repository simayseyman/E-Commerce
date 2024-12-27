package com.enoca.demo.Service;

import com.enoca.demo.Models.Order;

public interface IOrderService {
    Order placeOrder(int customerId);
    Order getOrderByCode(String orderCode);
    Iterable<Order> getAllOrdersForCustomer(int customerId);
}
