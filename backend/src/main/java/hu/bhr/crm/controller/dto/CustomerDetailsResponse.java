package hu.bhr.crm.controller.dto;

import java.time.Instant;
import java.util.UUID;

public record CustomerDetailsResponse(
        UUID id,
        UUID customerId,
        String note,
        Instant createdAt,
        Instant updatedAt
) {}
