package hu.bhr.crm.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.ZonedDateTime;
import java.util.List;

@Schema(description = "Standard error response structure")
public record ErrorResponse(
        @Schema(description = "Status category", example = "error")
        String status,

        @Schema(description = "Short descriptive title of the error type")
        String title,

        @Schema(description = "List of specific error messages or validation failures")
        List<String> errorMessages,

        @Schema(description = "Timestamp when the error occurred", example = "2024-01-22T13:52:00Z")
        ZonedDateTime timestamp
) {}