package hu.bhr.crm.model;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record CustomerDetails(
        UUID id,
        UUID customerId,
        String note,
        Instant createdAt,
        Instant updatedAt
) {}