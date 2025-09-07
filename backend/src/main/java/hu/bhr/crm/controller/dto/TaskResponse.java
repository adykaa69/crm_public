package hu.bhr.crm.controller.dto;

import hu.bhr.crm.model.TaskStatus;

import java.time.ZonedDateTime;
import java.util.UUID;

public record TaskResponse(
        UUID id,
        UUID customerId,
        String title,
        String description,
        ZonedDateTime reminder,
        ZonedDateTime dueDate,
        TaskStatus status,
        ZonedDateTime createdAt,
        ZonedDateTime completedAt,
        ZonedDateTime updatedAt
) {}
