package hu.bhr.crm.exception.handler;

import hu.bhr.crm.controller.dto.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.ZonedDateTime;
import java.util.List;

public class ErrorResponseUtils  {

    public static ErrorResponse toErrorResponse(Exception exception) {
        if (exception instanceof MethodArgumentNotValidException validationException) {
            List<String> messages = validationException
                    .getBindingResult()
                    .getAllErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .toList();

            return new ErrorResponse(messages, ZonedDateTime.now());
        }

        return new ErrorResponse(
                List.of(exception.getMessage()),
                ZonedDateTime.now()
        );
    }
}
