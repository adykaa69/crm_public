package hu.bhr.crm.exception.handler;

import hu.bhr.crm.controller.dto.ErrorResponse;
import hu.bhr.crm.controller.dto.PlatformResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class FallbackExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(FallbackExceptionHandler.class);

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public PlatformResponse<ErrorResponse> handleGeneralException(Exception ex) {
        log.error("Unexpected error occurred", ex);
        ErrorResponse errorResponse = ErrorResponseUtils.toErrorResponse(ex);

        return new PlatformResponse<>("error", "Unexpected error during request processing", errorResponse);
    }
}
