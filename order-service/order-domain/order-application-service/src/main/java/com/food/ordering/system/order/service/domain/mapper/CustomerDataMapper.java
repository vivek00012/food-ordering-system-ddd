package com.food.ordering.system.order.service.domain.mapper;

import com.food.ordering.system.domain.valueobject.CustomerId;
import com.food.ordering.system.order.service.domain.dto.message.CustomerRequest;
import com.food.ordering.system.order.service.domain.entity.Customer;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CustomerDataMapper {
    public Customer customerRequestToCustomer(CustomerRequest customerRequest){
        return Customer.builder()
                .customerId(new CustomerId(UUID.fromString(customerRequest.getCustomerId())))
                .username(customerRequest.getUsername())
                .firstName(customerRequest.getFirstName())
                .lastName(customerRequest.getLastName())
                .build();
    }
}
