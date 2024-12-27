package com.enoca.demo.Repo;

import com.enoca.demo.Models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepo extends JpaRepository<Product, Integer> {
    Optional<Product> findById(int id);
}
