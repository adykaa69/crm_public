package hu.bhr.backend.step_definition.dto;

import com.fasterxml.jackson.annotation.JsonProperty;


import java.time.ZonedDateTime;

public record ResidenceResponse(
        @JsonProperty("id") String id,
        @JsonProperty("zipCode") String zipCode,
        @JsonProperty("streetAddress") String streetAddress,
        @JsonProperty("addressLine2") String addressLine2,
        @JsonProperty("city") String city,
        @JsonProperty("country") String country,
        @JsonProperty("createdAt") ZonedDateTime createdAt,
        @JsonProperty("updatedAt") ZonedDateTime updatedAt
) {}
