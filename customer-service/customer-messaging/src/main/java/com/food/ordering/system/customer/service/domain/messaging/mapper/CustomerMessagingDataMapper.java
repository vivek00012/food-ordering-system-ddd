package com.food.ordering.system.customer.service.domain.messaging.mapper;

import com.food.ordering.system.customer.service.domain.event.CustomerCreatedEvent;
import com.food.ordering.system.kafka.order.avro.model.*;
import org.springframework.stereotype.Component;

@Component
public class CustomerMessagingDataMapper {

    public CustomerAvroModel customerCreatedEventToCustomerAvroModel(CustomerCreatedEvent customerCreatedEvent) {
        return CustomerAvroModel.newBuilder()
                .setId(customerCreatedEvent.getCustomer().getId().getValue().toString())
                .setFirstName(customerCreatedEvent.getCustomer().getFirstName())
                .setLastName(customerCreatedEvent.getCustomer().getLastName())
                .setUsername(customerCreatedEvent.getCustomer().getUsername())
                .build();
    }

}
