package hu.bhr.crm.exception.handler;

import hu.bhr.crm.controller.dto.ErrorResponse;
import hu.bhr.crm.exception.CustomerDetailsNotFoundException;
import hu.bhr.crm.exception.CustomerNotFoundException;
import hu.bhr.crm.exception.EmailScheduleException;
import hu.bhr.crm.exception.EmailSendingException;
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

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SpecificExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(SpecificExceptionHandler.class);

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CustomerNotFoundException.class)
    public ErrorResponse handleCustomerNotFoundException(CustomerNotFoundException ex) {
        log.warn("Customer not found", ex);
        return ErrorResponseUtils.toErrorResponse("Error occurred during requesting customer", ex);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CustomerDetailsNotFoundException.class)
    public ErrorResponse handleCustomerDetailsNotFoundException(CustomerDetailsNotFoundException ex) {
        log.warn("Customer details not found", ex);
        return ErrorResponseUtils.toErrorResponse("Error occurred during customer details retrieval", ex);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(TaskNotFoundException.class)
    public ErrorResponse handleTaskNotFoundException(TaskNotFoundException ex) {
        log.warn("Task not found", ex);
        return ErrorResponseUtils.toErrorResponse("Error occurred during task retrieval", ex);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidEmailException.class)
    public ErrorResponse handleInvalidEmailException(InvalidEmailException ex) {
        log.warn("Invalid email exception", ex);
        return ErrorResponseUtils.toErrorResponse("Error occurred during customer registration", ex);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingFieldException.class)
    public ErrorResponse handleMissingFieldException(MissingFieldException ex) {
        log.warn("Missing field exception", ex);
        return ErrorResponseUtils.toErrorResponse("Error occurred during customer registration", ex);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidStatusException.class)
    public ErrorResponse handleInvalidStatusException(InvalidStatusException ex) {
        log.warn("Invalid status exception", ex);
        return ErrorResponseUtils.toErrorResponse("Error occurred during task processing", ex);
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
    public ErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.warn("Invalid request", ex);
        return ErrorResponseUtils.toErrorResponse("Validation error during request processing", ex);
    }
}
