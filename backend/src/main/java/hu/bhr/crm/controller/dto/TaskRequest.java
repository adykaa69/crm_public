package hu.bhr.crm.controller.dto;

import hu.bhr.crm.model.TaskStatus;

import java.sql.Timestamp;
import java.util.UUID;

public record TaskRequest(
    UUID customerId,
    String title,
    String description,
    Timestamp reminder,
    Timestamp dueDate,
    TaskStatus status
) {}
