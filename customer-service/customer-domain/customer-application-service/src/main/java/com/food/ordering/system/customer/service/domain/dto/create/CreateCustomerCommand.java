package com.food.ordering.system.customer.service.domain.dto.create;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class CreateCustomerCommand {
    @NotNull
    @NotEmpty(message = "User name cannot be empty!")
    private final String username;

    @NotNull
    @NotEmpty(message = "First name cannot be empty!")
    private final String firstName;

    @NotNull
    @NotEmpty(message = "Last name cannot be empty!")
    private final String lastName;

}
