package hu.bhr.crm.step_definition.dto;

import java.time.ZonedDateTime;

public record TaskRequest (
    String customerId,
    String title,
    String description,
    ZonedDateTime reminder,
    ZonedDateTime dueDate,
    String status
) {}
