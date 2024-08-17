package com.food.ordering.system.customer.service.domain;

import com.food.ordering.system.customer.service.domain.event.CustomerCreatedEvent;
import com.food.ordering.system.domain.valueobject.CustomerId;
import com.food.ordering.system.customer.service.domain.dto.create.CreateCustomerCommand;
import com.food.ordering.system.customer.service.domain.dto.create.CreateCustomerResponse;
import com.food.ordering.system.customer.service.domain.dto.create.DeleteCustomerResponse;
import lombok.extern.slf4j.Slf4j;
import com.food.ordering.system.customer.service.domain.mapper.CustomerDataMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.food.ordering.system.customer.service.domain.ports.output.message.publisher.CustomerMessageRequestPublisher;

@Component
@Slf4j
public class CustomerCreateCommandHandler {

    private final CustomerDomainHelper customerDomainHelper;
    private final CustomerDataMapper customerDataMapper;
    private final CustomerMessageRequestPublisher customerMessageRequestPublisher;

    public CustomerCreateCommandHandler(CustomerDomainHelper customerDomainHelper, CustomerDataMapper customerDataMapper, CustomerMessageRequestPublisher customerMessageRequestPublisher) {
        this.customerDomainHelper = customerDomainHelper;
        this.customerDataMapper = customerDataMapper;
        this.customerMessageRequestPublisher = customerMessageRequestPublisher;
    }

    @Transactional
    public CreateCustomerResponse createCustomer(CreateCustomerCommand createCustomer){
        CustomerCreatedEvent customerCreatedEvent = this.customerDomainHelper.persistCustomer(createCustomer);
        customerMessageRequestPublisher.publish(customerCreatedEvent);
        return customerDataMapper.customerToCreateCustomerResponse(customerCreatedEvent.getCustomer());
    }

    @Transactional
    public DeleteCustomerResponse deleteCustomer(CustomerId customerId){
        DeleteCustomerResponse deleteCustomerResponse = this.customerDomainHelper.persistDelete(customerId);
        return deleteCustomerResponse;
    }
}
