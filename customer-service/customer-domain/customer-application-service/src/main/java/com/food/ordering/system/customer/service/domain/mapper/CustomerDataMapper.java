package com.food.ordering.system.customer.service.domain.mapper;

import com.food.ordering.system.customer.service.domain.entity.Customer;
import com.food.ordering.system.customer.service.domain.dto.create.CreateCustomerCommand;
import com.food.ordering.system.customer.service.domain.dto.create.CreateCustomerResponse;
import com.food.ordering.system.customer.service.domain.dto.create.DeleteCustomerResponse;
import org.springframework.stereotype.Component;

@Component
public class CustomerDataMapper {

    public Customer createCustomerCommandToCustomer(CreateCustomerCommand createCustomerCommand){
        return Customer.newBuilder()
                .firstName(createCustomerCommand.getFirstName())
                .lastName(createCustomerCommand.getLastName())
                .username(createCustomerCommand.getUsername())
                .build();
    }

    public CreateCustomerResponse customerToCreateCustomerResponse(Customer customer){
        return CreateCustomerResponse.builder()
                .customerId(customer.getId().getValue())
                .message("Customer created successfully!")
                .build();
    }

    public DeleteCustomerResponse customerToDeleteCustomerResponse(Customer customer){
        return DeleteCustomerResponse.builder()
                .customerId(customer.getId().getValue())
                .message("Customer deleted successfully!")
                .build();
    }
}
