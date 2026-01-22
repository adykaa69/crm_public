package hu.bhr.crm.controller.dto;

import hu.bhr.crm.model.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.ZonedDateTime;
import java.util.UUID;

@Schema(description = "Task details returned by the API")
public record TaskResponse(
        @Schema(description = "Unique identifier of the task", example = "b2c3d4e5-f6a7-8901-2345-678901abcdef")
        UUID id,

        @Schema(description = "UUID of the related customer", example = "a1b2c3d4-e5f6-7890-1234-567890abcdef")
        UUID customerId,

        @Schema(description = "Title of the task", example = "Call customer")
        String title,

        @Schema(description = "Description of the task", example = "Going bouldering with the customer")
        String description,

        @Schema(description = "Date and time for the reminder notification", example = "2025-02-15T09:00:00Z")
        ZonedDateTime reminder,

        @Schema(description = "Due date for the task", example = "2025-02-20T17:00:00Z")
        ZonedDateTime dueDate,

        @Schema(
            description = "Current status of the task",
            example = "IN_PROGRESS",
            implementation = TaskStatus.class
        )
        TaskStatus status,

        @Schema(description = "Timestamp of creation", example = "2023-10-01T12:00:00Z")
        ZonedDateTime createdAt,

        @Schema(
            description = "Timestamp of completion, Set automatically when status becomes 'COMPLETED'",
            example = "2023-10-05T15:45:00Z")
        ZonedDateTime completedAt,

        @Schema(description = "Timestamp of most recent update", example = "2023-10-02T14:30:00Z")
        ZonedDateTime updatedAt
) {}
