package com.food.ordering.system.customer.service.domain.entity;

import com.food.ordering.system.domain.entity.AggregateRoot;
import com.food.ordering.system.domain.valueobject.CustomerId;

import java.util.UUID;

public class Customer extends AggregateRoot<CustomerId> {
    private final String username;

    private final String firstName;

    private final String lastName;


    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void initializeCustomer(){
        setId(new CustomerId(UUID.randomUUID()));
    }


    public static Builder newBuilder() {
        return new Builder();
    }

    private Customer(Builder builder) {
        setId(builder.customerId);
        username = builder.username;
        firstName = builder.firstName;
        lastName = builder.lastName;
    }


    public static final class Builder {
        private CustomerId customerId;
        private String username;
        private String firstName;
        private String lastName;

        private Builder() {
        }

        public Builder id(CustomerId customerId) {
            customerId = customerId;
            return this;
        }

        public Builder username(String val) {
            username = val;
            return this;
        }

        public Builder firstName(String val) {
            firstName = val;
            return this;
        }

        public Builder lastName(String val) {
            lastName = val;
            return this;
        }

        public Customer build() {
            return new Customer(this);
        }
    }
}
