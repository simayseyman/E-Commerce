package com.enoca.demo.Service;

import com.enoca.demo.Models.Cart;
import com.enoca.demo.Models.CartItem;
import com.enoca.demo.Models.Customer;
import com.enoca.demo.Models.Product;
import com.enoca.demo.Repo.CartItemRepository;
import com.enoca.demo.Repo.CartRepo;
import com.enoca.demo.Repo.CustomerRepo;
import com.enoca.demo.Repo.ProductRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService implements ICartService {
    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private CartItemRepository cartItemRepo;

    @Autowired
    private ProductService productService;

    @Override
    public Cart getCartForCustomer(int customerId) {
        Optional<Customer> customer = customerRepo.findById(customerId);
        if (customer.isEmpty()) {
            throw new IllegalStateException("Müşteri bulunamadı.");
        }

        Cart cart = customer.get().getCart();
        if (cart == null) {
            throw new IllegalStateException("Müşterinin sepeti bulunamadı.");
        }
        return cart;
    }

    @Transactional
    @Override
    public Cart addProductToCart(int customerId, int productId, int quantity) {
        Cart cart = cartRepo.findByCustomerId(customerId).orElseThrow(() -> new RuntimeException("Sepet bulunamadı"));

        productService.updateStock(productId, quantity);

        Product product = productRepo.findById(productId).orElseThrow(() -> new RuntimeException("Ürün bulunamadı"));

        CartItem cartItemToAdd = null;
        for (CartItem cartItem : cart.getCartItems()) {
            if (cartItem.getProduct() != null && cartItem.getProduct().getId() == productId) {
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
                cartItemToAdd = cartItem;
                break;
            }
        }

        if (cartItemToAdd == null) {
            CartItem newCartItem = new CartItem();
            newCartItem.setProduct(product);
            newCartItem.setQuantity(quantity);
            newCartItem.setCart(cart);
            cart.getCartItems().add(newCartItem);
        }

        updateCartTotal(cart);

        cartRepo.save(cart);

        return cart;

    }

    @Override
    public Cart removeProductFromCart(int customerId, int productId, int quantity) {

        Cart cart = cartRepo.findByCustomerId(customerId).orElseThrow(() -> new RuntimeException("Sepet bulunamadı"));

        Product product = productRepo.findById(productId).orElseThrow(() -> new RuntimeException("Ürün bulunamadı"));

        CartItem cartItemToRemove = null;
        for (CartItem cartItem : cart.getCartItems()) {
            if (cartItem.getProduct() != null && cartItem.getProduct().getId() == productId) {
                cartItemToRemove = cartItem;
                break;
            }
        }

        if (cartItemToRemove == null) {
            throw new RuntimeException("Ürün sepette bulunamadı");
        }

        if (cartItemToRemove.getQuantity() <= quantity) {
            cart.getCartItems().remove(cartItemToRemove);
        } else {
            cartItemToRemove.setQuantity(cartItemToRemove.getQuantity() - quantity);
        }

        if (cart.getCartItems().isEmpty()) {
            cartRepo.delete(cart);
            cartRepo.flush();
            return null;
        }

        updateCartTotal(cart);

        cartRepo.save(cart);

        return cart;
    }

    @Override
    public Cart updateCart(int cartId, int productId, int quantity) {
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found with id: " + cartId));

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        CartItem cartItem = cartItemRepo.findByCartAndProduct(cart, product)
                .orElseGet(() -> {
                    CartItem newItem = new CartItem();
                    newItem.setCart(cart);
                    newItem.setProduct(product);
                    return newItem;
                });

        cartItem.setQuantity(quantity);

        updateCartTotal(cart);

        cartItemRepo.save(cartItem);
        return cartRepo.findById(cartId)
                .orElseThrow(()-> new RuntimeException("Card not found after update!"));
    }

    @Override
    public Cart emptyCart(int cartId) {
        Optional<Cart> optionalCart = cartRepo.findById(cartId);
        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();
            cart.getCartItems().clear();
            updateCartTotal(cart);
            return cartRepo.save(cart);
        } else {
            throw new EntityNotFoundException("Sepet bulunamadı.");
        }

    }

    private void updateCartTotal(Cart cart) {
        double total = 0;
        for (CartItem item : cart.getCartItems()) {
            total += item.getProduct().getPrice() * item.getQuantity();
        }
        cart.setTotalPrice(total);
    }

}

