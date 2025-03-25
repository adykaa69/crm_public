package hu.bhr.backend.customer.dto;

import io.cucumber.core.internal.com.fasterxml.jackson.annotation.JsonProperty;

public record ErrorResponse(
        @JsonProperty("timestamp") String timestamp,
        @JsonProperty("status") String status,
        @JsonProperty("error") String error,
        @JsonProperty("path") String path
) {}
