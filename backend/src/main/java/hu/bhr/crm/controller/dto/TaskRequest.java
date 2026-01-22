package hu.bhr.crm.controller.dto;

import hu.bhr.crm.model.TaskStatus;
import hu.bhr.crm.validation.annotation.NotPast;
import hu.bhr.crm.validation.annotation.ValidEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.time.ZonedDateTime;
import java.util.UUID;

@Builder
@Schema(description = "Payload for creating or updating a task")
public record TaskRequest(
    @Schema(description = "UUID of the associated customer (optional)", example = "a1b2c3d4-e5f6-7890-1234-567890abcdef")
    UUID customerId,

    @NotBlank(message = "Title is required")
    @Schema(
        description = "Title of the task",
        example = "Call customer",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    String title,

    @Schema(description = "Description of the task", example = "Going bouldering with the customer")
    String description,

    @NotPast(message = "Reminder date must be in the future")
    @Schema(description = "Date and time for the reminder notification", example = "2025-02-15T09:00:00Z")
    ZonedDateTime reminder,

    @NotPast(message = "Due date must be in the future")
    @Schema(description = "Due date for the task", example = "2025-02-20T17:00:00Z")
    ZonedDateTime dueDate,

    @ValidEnum(
        message = "Invalid task status",
        enumClass = TaskStatus.class)
    @Schema(
        description = "Current status of the task",
        example = "IN_PROGRESS",
        defaultValue = "OPEN",
        implementation = TaskStatus.class
    )
    String status
) {}
