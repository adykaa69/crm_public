package hu.bhr.crm.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import hu.bhr.crm.mapper.EnumUtils;

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
        String normalized = EnumUtils.normalizeEnumName(status);
        return TaskStatus.valueOf(normalized);
    }
}