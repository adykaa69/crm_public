package hu.bhr.backend.step_definition.dto;

import com.fasterxml.jackson.annotation.JsonProperty;


public record PlatformResponse<T>(
    @JsonProperty("status") String status,
    @JsonProperty("message") String message,
    @JsonProperty("data") T data
) {}
