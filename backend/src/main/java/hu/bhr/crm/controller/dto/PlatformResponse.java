package hu.bhr.crm.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Standard wrapper for all successful API responses")
public record PlatformResponse<T>(
    @Schema(description = "The actual data payload of the response")
    T content
) {}
