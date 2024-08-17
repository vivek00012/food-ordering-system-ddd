package com.food.ordering.system.customer.service.dataaccess.mapper;

import com.food.ordering.system.customer.service.dataaccess.entity.CustomerEntity;
import com.food.ordering.system.customer.service.domain.entity.Customer;
import com.food.ordering.system.domain.valueobject.CustomerId;
import org.springframework.stereotype.Component;

@Component
public class CustomerDataAccessMapper {

    public Customer customerEntityToCustomer(CustomerEntity customerEntity) {
        return Customer.newBuilder()
                .id(new CustomerId(customerEntity.getId()))
                .firstName(customerEntity.getFirstName())
                .lastName(customerEntity.getLastName())
                .username(customerEntity.getUsername())
                .build();
    }

    public CustomerEntity customerToCustomerEntity(Customer customer) {
        return CustomerEntity.builder()
                .id(customer.getId().getValue())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .username(customer.getUsername())
                .build();
    }
}
