package com.food.ordering.system.order.service.dataaccess.customer.mapper;

import com.food.ordering.system.domain.valueobject.CustomerId;
import com.food.ordering.system.order.service.dataaccess.customer.entity.CustomerEntity;
import com.food.ordering.system.order.service.domain.entity.Customer;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CustomerDataAccessMapper {

    public Customer customerEntityToCustomer(CustomerEntity customerEntity){
        return Customer.builder()
                .customerId(new CustomerId(customerEntity.getId()))
                .firstName(customerEntity.getFirstName())
                .lastName(customerEntity.getLastName())
                .username(customerEntity.getUsername())
                .username(customerEntity.getUsername())
                .build();

    }

    public CustomerEntity customerToCustomerEntity(Customer customer){
        return CustomerEntity.builder()
                .id(UUID.fromString(customer.getId().getValue().toString()))
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .username(customer.getUsername())
                .build();
    }
}
