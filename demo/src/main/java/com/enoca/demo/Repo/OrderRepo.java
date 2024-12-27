package com.enoca.demo.Repo;

import com.enoca.demo.Models.Customer;
import com.enoca.demo.Models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepo extends JpaRepository<Order, Integer> {
    Optional<Order> findByOrderCode(String orderCode);
    Iterable<Order> findAllByCustomer(Customer customer);
}
