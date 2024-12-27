package com.enoca.demo.Service;

import com.enoca.demo.Models.Product;
import com.enoca.demo.Repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class ProductService implements IProductService{

    @Autowired
    private ProductRepo productRepo;

    @Override
    public List<Product> listAll() {
        return productRepo.findAll();
    }

    @Override
    public Product delete(@PathVariable int id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product does not exist with id: " + id));
        productRepo.delete(product);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return product;
    }

    @Override
    public Product insert(@RequestBody Product product) {
        productRepo.save(product);
        return product;
    }

    @Override
    public Product update(int id, @RequestBody Product updatedProduct) {
        Product existingProduct = productRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product does not exist with id: " + id));

        existingProduct.setName(updatedProduct.getName());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setUpdatedAt(LocalDateTime.now());

        return productRepo.save(existingProduct);
    }

    public Product getProduct(int productId) {
        return productRepo.findById(productId).orElseThrow(() -> new RuntimeException("Ürün bulunamadı"));
    }

    public void updateStock(int productId, int quantity) {
        Product product = getProduct(productId);
        if (product.getStock() < quantity) {
            throw new RuntimeException("Insufficient stock for product: " + product.getName());
        }
        product.setStock(product.getStock() - quantity);
        productRepo.save(product);
    }

}
