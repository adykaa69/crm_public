package hu.bhr.crm.controller.dto;

import hu.bhr.crm.model.TaskStatus;
import hu.bhr.crm.validation.annotation.NotPast;
import hu.bhr.crm.validation.annotation.ValidEnum;
import jakarta.validation.constraints.NotBlank;

import java.time.ZonedDateTime;
import java.util.UUID;

public record TaskRequest(
    UUID customerId,
    @NotBlank(message = "Title is required")
    String title,
    String description,
    @NotPast(message = "Reminder date must not be in the past")
    ZonedDateTime reminder,
    @NotPast(message = "Due date must not be in the past")
    ZonedDateTime dueDate,
    @ValidEnum(
            message = "Invalid task status",
            enumClass = TaskStatus.class)
    String status
) {}
