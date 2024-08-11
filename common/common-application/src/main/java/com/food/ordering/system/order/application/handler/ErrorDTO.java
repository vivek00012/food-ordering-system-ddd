package com.food.ordering.system.order.application.handler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ErrorDTO {
    private final String code;
    private final String message;
}
