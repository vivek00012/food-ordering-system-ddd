package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.dto.message.CustomerRequest;
import com.food.ordering.system.order.service.domain.ports.input.message.listener.customer.CustomerMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Validated
@Service
public class CustomerMessageListenerImpl implements CustomerMessageListener {

    private final CustomerRequestHelper customerRequestHelper;

    public CustomerMessageListenerImpl(CustomerRequestHelper customerRequestHelper) {
        this.customerRequestHelper = customerRequestHelper;
    }

    public void persistCustomer(CustomerRequest customerRequest){
        this.customerRequestHelper.persistCustomer(customerRequest);
    }
}
