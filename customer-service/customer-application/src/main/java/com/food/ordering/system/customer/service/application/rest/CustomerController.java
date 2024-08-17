package com.food.ordering.system.customer.service.application.rest;

import com.food.ordering.system.customer.service.domain.dto.create.CreateCustomerCommand;
import com.food.ordering.system.customer.service.domain.dto.create.CreateCustomerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.food.ordering.system.customer.service.domain.ports.input.service.CustomerServiceApplication;

@Slf4j
@RestController
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerServiceApplication customerServiceApplication;

    public CustomerController(CustomerServiceApplication customerServiceApplication) {
        this.customerServiceApplication = customerServiceApplication;
    }


    @PostMapping(consumes = "application/json")
    public ResponseEntity<CreateCustomerResponse> createOrder(@RequestBody CreateCustomerCommand createCustomerCommand){
        log.info("Creating customer {}",createCustomerCommand.getFirstName() + " "+ createCustomerCommand.getLastName());

        CreateCustomerResponse createCustomerResponse = customerServiceApplication.createCustomer(createCustomerCommand);

        log.info("CustomerController: Customer created with customer id: {}",createCustomerResponse.getCustomerId());
        return ResponseEntity.ok(createCustomerResponse);
    }
}
