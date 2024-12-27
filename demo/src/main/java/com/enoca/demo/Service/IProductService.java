package com.enoca.demo.Service;

import com.enoca.demo.Models.Product;

import java.util.List;

public interface IProductService {

    List<Product> listAll();
    Product delete(int id);
    Product insert(Product product);
    Product update(int id, Product product);
}
