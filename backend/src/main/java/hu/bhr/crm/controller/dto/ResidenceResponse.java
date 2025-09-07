package hu.bhr.crm.controller.dto;

import java.time.ZonedDateTime;
import java.util.UUID;

public record ResidenceResponse(
        UUID id,
        String zipCode,
        String streetAddress,
        String addressLine2,
        String city,
        String country,
        ZonedDateTime createdAt,
        ZonedDateTime updatedAt
) {}
