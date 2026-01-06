package hu.bhr.crm.controller.dto;

import java.time.ZonedDateTime;
import java.util.List;

public record ErrorResponse(
        String status,
        String title,
        List<String> errorMessages,
        ZonedDateTime timestamp
) {}