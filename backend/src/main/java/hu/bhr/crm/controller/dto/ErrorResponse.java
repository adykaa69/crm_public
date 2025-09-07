package hu.bhr.crm.controller.dto;

import java.time.ZonedDateTime;

public record ErrorResponse(
        String errorMessage,
        ZonedDateTime timestamp
) {}