package com.enoca.demo.Controller;

import com.enoca.demo.Models.Cart;
import com.enoca.demo.Service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CartController {

    @Autowired
    private ICartService cartService;

    @GetMapping("/Cart/{customerId}")
    public Cart getCart(@PathVariable int customerId) {
        return cartService.getCartForCustomer(customerId);
    }

    @PostMapping("/addProduct")
    public ResponseEntity<Cart> addProductToCart(@RequestParam int customerId,
                                                 @RequestParam int productId,
                                                 @RequestParam int quantity) {
        try {
            Cart cart = cartService.addProductToCart(customerId, productId, quantity);
            return new ResponseEntity<>(cart, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping("/removeProduct")
    public Cart removeProductFromCart(@RequestParam int customerId,
                                      @RequestParam int productId,
                                      @RequestParam int quantity) {

        return cartService.removeProductFromCart(customerId, productId, quantity);
    }

    @PutMapping("/updateCart")
    public ResponseEntity<Cart> updateCart(
            @RequestParam int cartId,
            @RequestParam int productId,
            @RequestParam int quantity) {
        Cart updatedCart = cartService.updateCart(cartId, productId, quantity);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/emptyCard/{cartId}")
    public ResponseEntity<Void> emptyCart(@PathVariable int cartId) {
        cartService.emptyCart(cartId);
        return ResponseEntity.noContent().build();
    }

}
