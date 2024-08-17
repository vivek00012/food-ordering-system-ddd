package com.food.ordering.system.order.service.domain.ports.input.message.listener.customer;

import com.food.ordering.system.order.service.domain.dto.message.CustomerRequest;
import com.food.ordering.system.order.service.domain.dto.message.PaymentResponse;

public interface CustomerMessageListener {
    void persistCustomer(CustomerRequest customerRequest);

}
