package com.food.ordering.system.customer.service.application.exception.handler;

import com.food.ordering.system.customer.service.domain.exception.CustomerNotFoundException;
import com.food.ordering.system.customer.service.domain.exception.CustomerDomainException;
import com.food.ordering.system.order.application.handler.ErrorDTO;
import com.food.ordering.system.order.application.handler.GlobalExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class CustomerGlobalException extends GlobalExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CustomerDomainException.class)
    public ErrorDTO handleException(CustomerDomainException orderDomainException){
        log.error(orderDomainException.getMessage(),orderDomainException);
        return ErrorDTO.builder().code(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(orderDomainException.getMessage())
                .build();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CustomerNotFoundException.class)
    public ErrorDTO handleException(CustomerNotFoundException orderNotFoundException){
        log.error(orderNotFoundException.getMessage(),orderNotFoundException);
        return ErrorDTO.builder().code(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(orderNotFoundException.getMessage())
                .build();
    }


}
