package hu.bhr.crm.controller.dto;

import java.time.LocalDateTime;

public record ErrorResponse(
        String errorMessage,
        LocalDateTime timestamp
) {}