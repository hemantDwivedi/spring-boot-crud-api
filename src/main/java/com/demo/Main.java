package com.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SpringBootApplication
@RestController
@RequestMapping("api/v1/customers")
public class Main {
    public Main(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    private final CustomerRepository customerRepository;

    @GetMapping
    public List<Customer> getCustomers(){
        return customerRepository.findAll();
    }

    record NewCustomerRequest(
            String name,
            String email,
            Integer age
    ){}


    @PostMapping
    public void addCustomer(@RequestBody NewCustomerRequest newCustomerRequest){
        Customer customer = new Customer();
        customer.setName(newCustomerRequest.name);
        customer.setEmail(newCustomerRequest.email);
        customer.setAge(newCustomerRequest.age);

        customerRepository.save(customer);
    }

    @PutMapping("{customerId}")
    public Customer updateCustomer(@PathVariable("customerId") Integer Id,
                               @RequestBody NewCustomerRequest newCustomerRequest){
        return customerRepository.findById(Id)
                .map(customer -> {
                    customer.setAge(newCustomerRequest.age);
                    return customerRepository.save(customer);
                })
                .orElseGet( () -> {
                    Customer customer = new Customer();
                    customer.setName(newCustomerRequest.name);
                    customer.setEmail(newCustomerRequest.email);
                    customer.setAge(newCustomerRequest.age);

                    return customerRepository.save(customer);
                });
    }

    @DeleteMapping("{customerId}")
    public void deleteCustomer(@PathVariable("customerId") Integer Id){
        customerRepository.deleteById(Id);
    }
}