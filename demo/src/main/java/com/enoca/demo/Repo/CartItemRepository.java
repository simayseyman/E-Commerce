package com.enoca.demo.Repo;

import com.enoca.demo.Models.Cart;
import com.enoca.demo.Models.CartItem;
import com.enoca.demo.Models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
}
