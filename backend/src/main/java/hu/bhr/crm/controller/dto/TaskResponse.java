package hu.bhr.crm.controller.dto;

import hu.bhr.crm.model.TaskStatus;

import java.sql.Timestamp;
import java.util.UUID;

public record TaskResponse(
        UUID id,
        UUID customerId,
        String title,
        String description,
        Timestamp reminder,
        Timestamp dueDate,
        TaskStatus status,
        Timestamp createdAt,
        Timestamp completedAt,
        Timestamp updatedAt
) {}
