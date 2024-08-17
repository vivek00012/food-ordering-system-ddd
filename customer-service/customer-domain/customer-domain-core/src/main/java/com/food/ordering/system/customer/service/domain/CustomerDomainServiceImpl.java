package com.food.ordering.system.customer.service.domain;

import com.food.ordering.system.customer.service.domain.event.CustomerCreatedEvent;
import com.food.ordering.system.customer.service.domain.entity.Customer;
import com.food.ordering.system.customer.service.domain.exception.CustomerDomainException;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static com.food.ordering.system.domain.DomainConstants.UTC;

@Slf4j
public class CustomerDomainServiceImpl implements CustomerDomainService{

    @Override
    public CustomerCreatedEvent validateAndCreateCustomer(Customer customer, boolean usernameExists) {
        validateCustomer(customer,usernameExists);
        customer.initializeCustomer();
        log.info("Customer with id : {} is initialised",customer.getId());
        return new CustomerCreatedEvent(customer, ZonedDateTime.now(ZoneId.of(UTC)));
    }

    private void validateCustomer(Customer customer, boolean usernameExists) {
        if(usernameExists){
            log.error("Customer with same username {} exists, try different username!",customer.getUsername());
            throw new CustomerDomainException("Customer with same username "+customer.getUsername()+"  exists, try different username!");
        }
    }
}
