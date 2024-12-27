package com.enoca.demo.Models;

import jakarta.persistence.*;

@Entity
@Table(name = "product", schema = "dbo")
public class Product extends BaseEntity {
    private String name;
    private int price;
    private int stock;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

}
