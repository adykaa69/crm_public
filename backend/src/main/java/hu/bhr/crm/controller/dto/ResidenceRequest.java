package hu.bhr.crm.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Address details for creating or updating a residence")
public record ResidenceRequest(
        @Schema(description = "Postal or Zip code", example = "1055")
        String zipCode,

        @Schema(description = "Primary street address", example = "1 Kossuth Lajos Square")
        String streetAddress,

        @Schema(description = "Secondary address line (e.g. apartment, suite)", example = "2nd floor, apt 4")
        String addressLine2,

        @Schema(description = "City name", example = "Budapest")
        String city,

        @Schema(description = "Country name", example = "Hungary")
        String country
) {}
