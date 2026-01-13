package hu.bhr.crm.model;

import lombok.Builder;
import lombok.With;

import java.time.Instant;
import java.util.UUID;

@Builder
public record Residence(
        @With UUID id,
        String zipCode,
        String streetAddress,
        String addressLine2,
        String city,
        String country,
        Instant createdAt,
        Instant updatedAt
) {}
