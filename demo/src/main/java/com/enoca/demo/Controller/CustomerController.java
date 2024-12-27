package com.enoca.demo.Controller;

import com.enoca.demo.Models.Customer;
import com.enoca.demo.Service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {
    @Autowired
    private ICustomerService customerService;

    @PostMapping("/CreateCustomer")
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        Customer savedCustomer = customerService.insert(customer);
        return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
    }
}
