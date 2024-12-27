package com.enoca.demo.Controller;

import com.enoca.demo.Models.Product;
import com.enoca.demo.Service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {
    @Autowired
    private IProductService productService;

    @GetMapping(path = "/Products")
    public List<Product> Products(){
        return productService.listAll();
    }

    @DeleteMapping(path = "/DeleteProduct/{id}")
    public Product deleteProduct(@PathVariable(name = "id") int id){
        return productService.delete(id);
    }

    @PostMapping(path = "/CreateProduct")
    public Product createProduct(@RequestBody Product product){
        return productService.insert(product);
    }

    @PutMapping(path = "/UpdateProduct/{id}")
    public Product updateProduct(@PathVariable(name = "id")int id, @RequestBody Product product){
        return productService.update(id, product);
    }



}
