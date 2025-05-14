package hu.bhr.crm.controller.dto;

import java.time.LocalDateTime;

public record ErrorResponse(
        String errorCode,
        String errorMessage,
        LocalDateTime timestamp
) {}