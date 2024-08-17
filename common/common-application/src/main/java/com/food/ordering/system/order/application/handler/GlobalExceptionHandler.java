package com.food.ordering.system.order.application.handler;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import javax.xml.validation.ValidatorHandler;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value={Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO handleException(Exception exception){
        log.error(exception.getMessage(),exception);
        return ErrorDTO.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message("Unexpected error!")
                .build();
    }

    @ExceptionHandler
    public final ResponseEntity<ErrorDTO> handleMethodAArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request){
        String fieldErrors = ex.getFieldErrors().toString();// add list of errors converted to string
        int count = ex.getErrorCount();// return error count

        String error  = "Total Errors : "+ count + " ; "+ "First : "+ex.getFieldError().getDefaultMessage();
        ErrorDTO errorDTO = ErrorDTO.builder()
                .code(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(error)
                .build();
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);

    }
    @ExceptionHandler
    public final ResponseEntity<ErrorDTO> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request){
        String fieldError = ex.getConstraintViolations().stream().map(err->err.getMessage()).findFirst().get();// add list of errors converted to string
        int count = ex.getConstraintViolations().size();// return error count

        String error  = "Total Errors : "+ count + " ; "+ "First : "+fieldError;
        ErrorDTO errorDTO = ErrorDTO.builder()
                .code(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(error)
                .build();
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);

    }

    @ResponseBody
    @ExceptionHandler(value={ValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleException(ValidationException validationException){
        ErrorDTO errorDTO;

        if(validationException  instanceof ConstraintViolationException){
            String violations = extractViolationsFromException((ConstraintViolationException) validationException);
            log.error(violations,validationException);
            errorDTO = ErrorDTO.builder().code(HttpStatus.BAD_REQUEST.getReasonPhrase())
                    .message(violations)
                    .build();
        }else{
            String exceptionMessage = validationException.getMessage();
            log.error(exceptionMessage,validationException);
            errorDTO = ErrorDTO.builder()
                    .code(HttpStatus.BAD_REQUEST.getReasonPhrase())
                    .message(exceptionMessage)
                    .build();
        }
        return errorDTO;

    }

    private String extractViolationsFromException(ConstraintViolationException validationException) {
        return validationException.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("--"));
    }
}
