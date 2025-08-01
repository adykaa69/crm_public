package hu.bhr.crm.controller.dto;

import hu.bhr.crm.model.TaskStatus;
import hu.bhr.crm.validation.annotation.ValidEnum;
import jakarta.validation.constraints.NotBlank;

import java.sql.Timestamp;
import java.util.UUID;

public record TaskRequest(
    UUID customerId,
    @NotBlank(message = "Title is required")
    String title,
    String description,
    Timestamp reminder,
    Timestamp dueDate,
    @ValidEnum(
            message = "Invalid task status",
            enumClass = TaskStatus.class)
    String status
) {}
