package hu.bhr.crm.exception;

import hu.bhr.crm.controller.dto.ErrorResponse;
import hu.bhr.crm.controller.dto.PlatformResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CustomerNotFoundException.class)
    public PlatformResponse<ErrorResponse> handleCustomerNotFoundException(CustomerNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ErrorCode.CUSTOMER_NOT_FOUND.getCode(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return new PlatformResponse<>("error", "Error occurred during requesting customer", errorResponse);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidEmailException.class)
    public PlatformResponse<ErrorResponse> handleInvalidEmailException(InvalidEmailException ex) {
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
        ErrorResponse errorResponse = new ErrorResponse(
                ErrorCode.MISSING_FIELD.getCode(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return new PlatformResponse<>("error", "Error occurred during customer registration", errorResponse);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public PlatformResponse<ErrorResponse> handleGeneralException(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse(
                ErrorCode.INTERNAL_SERVER_ERROR.getCode(),
                "An unexpected error occurred.",
                LocalDateTime.now()
        );

        return new PlatformResponse<>("error", "Unexpected error during request processing", errorResponse);
    }

}
