package hu.bhr.crm.exception.handler;

import hu.bhr.crm.controller.dto.ErrorResponse;
import hu.bhr.crm.controller.dto.PlatformResponse;
import hu.bhr.crm.exception.*;
import hu.bhr.crm.exception.code.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SpecificExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(SpecificExceptionHandler.class);

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CustomerNotFoundException.class)
    public PlatformResponse<ErrorResponse> handleCustomerNotFoundException(CustomerNotFoundException ex) {
        log.warn("Customer not found", ex);
        ErrorResponse errorResponse = new ErrorResponse(
                ErrorCode.CUSTOMER_NOT_FOUND.getCode(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return new PlatformResponse<>("error", "Error occurred during requesting customer", errorResponse);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CustomerDetailsNotFoundException.class)
    public PlatformResponse<ErrorResponse> handleCustomerDetailsNotFoundException(CustomerDetailsNotFoundException ex) {
        log.warn("Customer details not found", ex);
        ErrorResponse errorResponse = new ErrorResponse(
                ErrorCode.CUSTOMER_DETAILS_NOT_FOUND.getCode(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return new PlatformResponse<>("error", "Error occurred during customer details retrieval", errorResponse);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(TaskNotFoundException.class)
    public PlatformResponse<ErrorResponse> handleTaskNotFoundException(TaskNotFoundException ex) {
        log.warn("Task not found", ex);
        ErrorResponse errorResponse = new ErrorResponse(
                ErrorCode.TASK_NOT_FOUND.getCode(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return new PlatformResponse<>("error", "Error occurred during task retrieval", errorResponse);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidEmailException.class)
    public PlatformResponse<ErrorResponse> handleInvalidEmailException(InvalidEmailException ex) {
        log.warn("Invalid email exception", ex);
        ErrorResponse errorResponse = new ErrorResponse(
                ErrorCode.EMAIL_INVALID.getCode(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return new PlatformResponse<>("error", "Error occurred during customer registration", errorResponse);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingFieldException.class)
    public PlatformResponse<ErrorResponse> handleMissingFieldException(MissingFieldException ex) {
        log.warn("Missing field exception", ex);
        ErrorResponse errorResponse = new ErrorResponse(
                ErrorCode.MISSING_FIELD.getCode(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return new PlatformResponse<>("error", "Error occurred during customer registration", errorResponse);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidStatusException.class)
    public PlatformResponse<ErrorResponse> handleInvalidStatusException(InvalidStatusException ex) {
        log.warn("Invalid status exception", ex);
        ErrorResponse errorResponse = new ErrorResponse(
                ErrorCode.INVALID_STATUS.getCode(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return new PlatformResponse<>("error", "Error occurred during task processing", errorResponse);
    }
}
