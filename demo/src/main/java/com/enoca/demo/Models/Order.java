package com.enoca.demo.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "[order]", schema = "dbo")
public class Order extends BaseEntity{

    private int totalPrice;

    private String orderCode;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonBackReference("customer-orders")
    private Customer customer;

    @OneToMany(mappedBy = "order")
    @JsonBackReference
    private List<OrderDetail> orderDetails;

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public void generateOrderCode() {
        this.orderCode = UUID.randomUUID().toString();
    }

    public void calculateTotalPrice(){
        this.totalPrice = 0;
        for (OrderDetail detail : orderDetails){
            totalPrice += detail.getPriceAtOrderTime() * detail.getQuantity();
        }
    }
}
