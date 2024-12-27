package com.enoca.demo.Service;

import com.enoca.demo.Models.Cart;
import com.enoca.demo.Models.Customer;
import com.enoca.demo.Repo.CartRepo;
import com.enoca.demo.Repo.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService implements ICustomerService {

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private CartRepo cartRepo;

    @Override
    public Customer insert(Customer customer) {
        customerRepo.save(customer);

        Cart cart = new Cart();
        cart.setCustomer(customer);
        cart.setTotalPrice(0.0);
        cartRepo.save(cart);
        customer.setCart(cart);
        customerRepo.save(customer);

        return customer;
    }

}
