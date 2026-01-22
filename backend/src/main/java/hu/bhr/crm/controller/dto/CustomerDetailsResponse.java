package hu.bhr.crm.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.ZonedDateTime;
import java.util.UUID;

@Schema(description = "Customer document related to a customer")
public record CustomerDetailsResponse(
        @Schema(description = "Unique identifier of the document", example = "c3d4e5f6-7890-1234-5678-90abcdef1234")
        UUID id,

        @Schema(description = "UUID of the customer this document belongs to", example = "a1b2c3d4-e5f6-7890-1234-567890abcdef")
        UUID customerId,

        @Schema(description = "The content of the document or additional details", example = "Customer prefers wine over beer.")
        String note,

        @Schema(description = "Timestamp of creation", example = "2023-10-01T12:00:00Z")
        ZonedDateTime createdAt,

        @Schema(description = "Timestamp of most recent update", example = "2023-10-02T14:30:00Z")
        ZonedDateTime updatedAt
) {}
