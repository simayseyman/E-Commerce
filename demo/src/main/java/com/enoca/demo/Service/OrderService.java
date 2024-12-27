package com.enoca.demo.Service;

import com.enoca.demo.Models.*;
import com.enoca.demo.Repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class OrderService implements IOrderService{

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private OrderDetailRepo orderDetailRepo;


    @Override
    public Order placeOrder(int customerId) {

        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Müşteri bulunamadı"));


        Cart cart = cartRepo.findByCustomerId(customerId)
                .orElseThrow(() -> new RuntimeException("Sepet bulunamadı"));


        if (cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Sepet boş, sipariş verilemez.");
        }

        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderDetails(new ArrayList<>());
        order.generateOrderCode();
        order.calculateTotalPrice();
        orderRepo.save(order);

        for (CartItem cartItem : cart.getCartItems()) {
            Product product = cartItem.getProduct();
            int quantity = cartItem.getQuantity();

            if (product.getStock() < quantity) {
                throw new RuntimeException("Yeterli stok bulunamadı: " + product.getName());
            }

            product.setStock(product.getStock() - quantity);
            productRepo.save(product);

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setProduct(product);
            orderDetail.setPriceAtOrderTime(product.getPrice());
            orderDetail.setQuantity(quantity);
            orderDetail.setOrder(order);
            order.getOrderDetails().add(orderDetail);

            orderDetailRepo.save(orderDetail);

            order.calculateTotalPrice();
            orderRepo.save(order);
            cartRepo.delete(cart);

        }
        return order;
    }

    @Override
    public Order getOrderByCode(String orderCode) {
        return orderRepo.findByOrderCode(orderCode)
                .orElseThrow(() -> new RuntimeException("Sipariş bulunamadı"));
    }

    @Override
    public Iterable<Order> getAllOrdersForCustomer(int customerId) {
        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Müşteri bulunamadı"));
        return orderRepo.findAllByCustomer(customer);
    }
}
