package hu.bhr.crm.step_definition.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;

public record TaskResponse(
        @JsonProperty("id") String id,
        @JsonProperty("customerId") String customerId,
        @JsonProperty("title") String title,
        @JsonProperty("description") String description,
        @JsonProperty("reminder") ZonedDateTime reminder,
        @JsonProperty("dueDate") ZonedDateTime dueDate,
        @JsonProperty("status") String status,
        @JsonProperty("createdAt") ZonedDateTime createdAt,
        @JsonProperty("completedAt") ZonedDateTime completedAt,
        @JsonProperty("updatedAt") ZonedDateTime updatedAt
) {}
