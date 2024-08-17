package com.food.ordering.system.customer.service.domain;

import com.food.ordering.system.customer.service.domain.entity.Customer;
import com.food.ordering.system.customer.service.domain.event.CustomerCreatedEvent;
import com.food.ordering.system.customer.service.domain.exception.CustomerDomainException;
import com.food.ordering.system.customer.service.domain.exception.CustomerNotFoundException;
import com.food.ordering.system.domain.valueobject.CustomerId;
import com.food.ordering.system.customer.service.domain.dto.create.CreateCustomerCommand;
import com.food.ordering.system.customer.service.domain.dto.create.DeleteCustomerResponse;
import lombok.extern.slf4j.Slf4j;
import com.food.ordering.system.customer.service.domain.mapper.CustomerDataMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.food.ordering.system.customer.service.domain.ports.output.repository.CustomerRepository;

import java.util.Optional;

@Component
@Slf4j
public class CustomerDomainHelper {
    private final CustomerDomainService customerDomainService;
    private final CustomerRepository customerRepository;
    private final CustomerDataMapper customerDataMapper;


    public CustomerDomainHelper(CustomerDomainService customerDomainService, CustomerRepository customerRepository, CustomerDataMapper customerDataMapper) {
        this.customerDomainService = customerDomainService;
        this.customerRepository = customerRepository;
        this.customerDataMapper = customerDataMapper;
    }

    @Transactional
    public CustomerCreatedEvent persistCustomer(CreateCustomerCommand createCustomerCommand){
        Customer customer = customerDataMapper.createCustomerCommandToCustomer(createCustomerCommand);
        Boolean usernameExists = this.findCustomerByUsername(customer);
        CustomerCreatedEvent customerCreatedEvent = this.customerDomainService.validateAndCreateCustomer(customer,usernameExists);

        saveCustomer(customer);

        log.info("CustomerCreatedEvent: Customer is created with id: {}",customerCreatedEvent.getCustomer().getId().getValue());
        return  customerCreatedEvent;
    }

    @Transactional
    public DeleteCustomerResponse persistDelete(CustomerId customerId){
        Optional<Customer> customer = customerRepository.findCustomerInformation(customerId);
        if(customer.isEmpty()){
            log.error("CustomerDeletedEvent: Customer not found for customer id {}",customerId.getValue());
            throw new CustomerNotFoundException("Customer not found for customer id: " +customerId.getValue());
        }
        customerRepository.deleteCustomer(customer.get());
        return customerDataMapper.customerToDeleteCustomerResponse(customer.get());

    }

    private void checkCustomer(CustomerId customerid){
        Optional<Customer> customerData = customerRepository.findCustomerInformation(customerid);
        if(customerData.isEmpty()){
            log.error("Customer not found for {}",customerid.getValue());
            throw new CustomerNotFoundException("Customer not found for customerId" + customerid.getValue());
        }
    }

    private void deleteCustomer(Customer customer){
        this.checkCustomer(customer.getId());
        this.customerRepository.deleteCustomer(customer);
    }

    private boolean findCustomerByUsername(Customer customer){
        Optional<Customer> customerData = this.customerRepository.findCustomerByUsername(customer.getUsername());
        return customerData.isPresent();
    }

    private Customer saveCustomer(Customer customer) {
        Customer savedCustomer = this.customerRepository.saveCustomer(customer);
        if(savedCustomer==null){
            log.error("Could not save customer with id : {}",savedCustomer.getId());
            throw  new CustomerDomainException("Could not save customer with id "+ customer.getId());
        }

        return savedCustomer;
    }
}
