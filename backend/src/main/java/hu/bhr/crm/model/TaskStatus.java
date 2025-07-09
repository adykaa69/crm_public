package hu.bhr.crm.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import hu.bhr.crm.validation.EnumValidation;

public enum TaskStatus {
    OPEN,
    IN_PROGRESS,
    ON_HOLD,
    BLOCKED,
    COMPLETED,
    CANCELLED,
    ARCHIVED;

    @JsonCreator
    public static TaskStatus fromString(String status) {
        String normalized = status.trim().toUpperCase().replace(" ", "_");
        EnumValidation.validateEnum(TaskStatus.class, normalized);
        return TaskStatus.valueOf(normalized);
    }
}