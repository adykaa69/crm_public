package hu.bhr.crm.step_definition.dto;

import io.cucumber.core.internal.com.fasterxml.jackson.annotation.JsonProperty;

public record CustomerDetailsResponse(
        @JsonProperty("id") String id,
        @JsonProperty("customerId") String customerId,
        @JsonProperty("note") String note,
        @JsonProperty("createdAt") String createdAt,
        @JsonProperty("updatedAt") String updatedAt
) {}
