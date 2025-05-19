package hu.bhr.backend.customer.dto;

import io.cucumber.core.internal.com.fasterxml.jackson.annotation.JsonProperty;

public record ErrorResponse(
        @JsonProperty("errorCode") String errorCode,
        @JsonProperty("errorMessage") String errorMessage,
        @JsonProperty("timestamp") String timestamp
) {}
