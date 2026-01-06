package hu.bhr.crm.step_definition.dto;

import com.fasterxml.jackson.annotation.JsonProperty;


public record PlatformResponse<T>(
    @JsonProperty("content") T content
) {}
