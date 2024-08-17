package com.food.ordering.system.customer.service.domain;

import com.food.ordering.system.domain.valueobject.CustomerId;
import com.food.ordering.system.customer.service.domain.dto.create.CreateCustomerCommand;
import com.food.ordering.system.customer.service.domain.dto.create.CreateCustomerResponse;
import com.food.ordering.system.customer.service.domain.dto.create.DeleteCustomerResponse;
import com.food.ordering.system.customer.service.domain.ports.input.service.CustomerServiceApplication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Service
@Slf4j
@Validated
public class CustomerApplicationServiceImpl implements CustomerServiceApplication {

    private final CustomerCreateCommandHandler  customerCreateCommandHandler;

    public CustomerApplicationServiceImpl(CustomerCreateCommandHandler customerCreateCommandHandler) {
        this.customerCreateCommandHandler = customerCreateCommandHandler;
    }

    @Override
    public CreateCustomerResponse createCustomer(CreateCustomerCommand createCustomerCommand) {
        return customerCreateCommandHandler.createCustomer(createCustomerCommand);
    }

    @Override
    public DeleteCustomerResponse deleteCustomer(UUID customerId) {
        return customerCreateCommandHandler.deleteCustomer(new CustomerId(customerId));
    }
}
