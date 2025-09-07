package hu.bhr.crm.exception.handler;

import hu.bhr.crm.controller.dto.ErrorResponse;
import hu.bhr.crm.controller.dto.PlatformResponse;
import hu.bhr.crm.controller.dto.ValidationErrorResponse;
import hu.bhr.crm.exception.CustomerDetailsNotFoundException;
import hu.bhr.crm.exception.CustomerNotFoundException;
import hu.bhr.crm.exception.InvalidEmailException;
import hu.bhr.crm.exception.InvalidStatusException;
import hu.bhr.crm.exception.MissingFieldException;
import hu.bhr.crm.exception.TaskNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZonedDateTime;
import java.util.List;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SpecificExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(SpecificExceptionHandler.class);

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CustomerNotFoundException.class)
    public PlatformResponse<ErrorResponse> handleCustomerNotFoundException(CustomerNotFoundException ex) {
        log.warn("Customer not found", ex);
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                ZonedDateTime.now()
        );

        return new PlatformResponse<>("error", "Error occurred during requesting customer", errorResponse);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CustomerDetailsNotFoundException.class)
    public PlatformResponse<ErrorResponse> handleCustomerDetailsNotFoundException(CustomerDetailsNotFoundException ex) {
        log.warn("Customer details not found", ex);
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                ZonedDateTime.now()
        );

        return new PlatformResponse<>("error", "Error occurred during customer details retrieval", errorResponse);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(TaskNotFoundException.class)
    public PlatformResponse<ErrorResponse> handleTaskNotFoundException(TaskNotFoundException ex) {
        log.warn("Task not found", ex);
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                ZonedDateTime.now()
        );

        return new PlatformResponse<>("error", "Error occurred during task retrieval", errorResponse);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidEmailException.class)
    public PlatformResponse<ErrorResponse> handleInvalidEmailException(InvalidEmailException ex) {
        log.warn("Invalid email exception", ex);
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                ZonedDateTime.now()
        );

        return new PlatformResponse<>("error", "Error occurred during customer registration", errorResponse);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingFieldException.class)
    public PlatformResponse<ErrorResponse> handleMissingFieldException(MissingFieldException ex) {
        log.warn("Missing field exception", ex);
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                ZonedDateTime.now()
        );

        return new PlatformResponse<>("error", "Error occurred during customer registration", errorResponse);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidStatusException.class)
    public PlatformResponse<ErrorResponse> handleInvalidStatusException(InvalidStatusException ex) {
        log.warn("Invalid status exception", ex);
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                ZonedDateTime.now()
        );

        return new PlatformResponse<>("error", "Error occurred during task processing", errorResponse);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(EmailSendingException.class)
    public void handleEmailSendingException(EmailSendingException ex) {
        log.error("Email sending exception", ex);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(EmailScheduleException.class)
    public void handleEmailScheduleException(EmailScheduleException ex) {
        log.error("Email scheduling exception", ex);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public PlatformResponse<ValidationErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.warn("Invalid request", ex);
        List<ErrorResponse> errors = ex.getBindingResult().getAllErrors().stream()
                .map(error -> new ErrorResponse(
                        error.getDefaultMessage(),
                        ZonedDateTime.now()
                ))
                .toList();

        return new PlatformResponse<>("error", "Validation error during request processing", new ValidationErrorResponse(errors));
    }
}
