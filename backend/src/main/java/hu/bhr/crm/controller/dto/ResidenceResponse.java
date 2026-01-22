package hu.bhr.crm.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.ZonedDateTime;
import java.util.UUID;

@Schema(description = "Residence details returned by the API")
public record ResidenceResponse(
        @Schema(description = "Unique identifier of the residence", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,

        @Schema(description = "Postal or Zip code", example = "1055")
        String zipCode,

        @Schema(description = "Primary street address", example = "1 Kossuth Lajos Square")
        String streetAddress,

        @Schema(description = "Secondary address line (e.g. apartment, suite)", example = "2nd floor, apt 4")
        String addressLine2,

        @Schema(description = "City name", example = "Budapest")
        String city,

        @Schema(description = "Country name", example = "Hungary")
        String country,

        @Schema(description = "Timestamp of creation", example = "2023-10-01T12:00:00Z")
        ZonedDateTime createdAt,

        @Schema(description = "Timestamp of most recent update", example = "2023-10-02T14:30:00Z")
        ZonedDateTime updatedAt
) {}
