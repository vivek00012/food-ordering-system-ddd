package com.food.ordering.system.customer.service.domain.ports.input.service;

import com.food.ordering.system.customer.service.domain.dto.create.CreateCustomerCommand;
import com.food.ordering.system.customer.service.domain.dto.create.CreateCustomerResponse;
import com.food.ordering.system.customer.service.domain.dto.create.DeleteCustomerResponse;
import jakarta.validation.Valid;

import java.util.UUID;

public interface CustomerServiceApplication {
    CreateCustomerResponse createCustomer(@Valid CreateCustomerCommand createCustomerCommand);
    DeleteCustomerResponse deleteCustomer(@Valid UUID customerId);

}
