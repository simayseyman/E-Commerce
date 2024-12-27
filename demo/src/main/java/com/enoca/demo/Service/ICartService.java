package com.enoca.demo.Service;

import com.enoca.demo.Models.Cart;

public interface ICartService {
    Cart getCartForCustomer(int customerId);
    Cart addProductToCart(int customerId, int productId, int quantity);
    Cart removeProductFromCart(int customerId, int productId, int quantity);
    Cart updateCart(int cartId, int productId, int quantity);
    Cart emptyCart(int cartId);
}
