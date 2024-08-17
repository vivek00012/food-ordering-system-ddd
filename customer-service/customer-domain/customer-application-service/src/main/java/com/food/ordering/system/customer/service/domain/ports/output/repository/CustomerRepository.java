package com.food.ordering.system.customer.service.domain.ports.output.repository;

import com.food.ordering.system.customer.service.domain.entity.Customer;
import com.food.ordering.system.domain.valueobject.CustomerId;

import java.util.Optional;

public interface CustomerRepository {
    Customer saveCustomer(Customer customer);
    void deleteCustomer(Customer customerId);
    Optional<Customer> findCustomerInformation(CustomerId customerid);
    Optional<Customer> findCustomerByUsername(String username);
}
