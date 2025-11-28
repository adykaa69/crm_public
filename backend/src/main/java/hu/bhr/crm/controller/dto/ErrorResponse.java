package hu.bhr.crm.controller.dto;

import java.time.ZonedDateTime;
import java.util.List;

public record ErrorResponse(
        List<String> messages,
        ZonedDateTime timestamp
) {}