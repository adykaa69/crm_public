package hu.bhr.crm.step_definition.dto;

import lombok.Builder;

import java.time.ZonedDateTime;

@Builder
public record TaskRequest (
    String customerId,
    String title,
    String description,
    ZonedDateTime reminder,
    ZonedDateTime dueDate,
    String status
) {}
