package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.dto.message.CustomerRequest;
import com.food.ordering.system.order.service.domain.entity.Customer;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.food.ordering.system.order.service.domain.mapper.CustomerDataMapper;
import com.food.ordering.system.order.service.domain.ports.output.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class CustomerRequestHelper {
    private final OrderDomainService orderDomainService;
    private final CustomerRepository customerRepository;
    private final CustomerDataMapper customerDataMapper;

    public CustomerRequestHelper(OrderDomainService orderDomainService, CustomerRepository customerRepository, CustomerDataMapper customerDataMapper) {
        this.orderDomainService = orderDomainService;
        this.customerRepository = customerRepository;
        this.customerDataMapper = customerDataMapper;
    }

    @Transactional
    public void persistCustomer(CustomerRequest customerRequest){
        Customer customer = saveCustomer(customerRequest);
        log.info("Customer created with id {} in order table", customerRequest.getCustomerId());
    }

    private Customer saveCustomer(CustomerRequest customerRequest){
        Customer savedCustomer = customerRepository.writeCustomer(customerDataMapper.customerRequestToCustomer(customerRequest));
        if(savedCustomer==null){
            log.error("Customer with customer id {} cannot be saved",customerRequest.getCustomerId());
            throw new OrderDomainException("Customer could not be written");
        }
        return savedCustomer;
    }
}
