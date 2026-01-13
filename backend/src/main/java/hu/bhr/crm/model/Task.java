package hu.bhr.crm.model;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record Task(
        UUID id,
        UUID customerId,
        String title,
        String description,
        Instant reminder,
        Instant dueDate,
        TaskStatus status,
        Instant createdAt,
        Instant completedAt,
        Instant updatedAt
) {}

