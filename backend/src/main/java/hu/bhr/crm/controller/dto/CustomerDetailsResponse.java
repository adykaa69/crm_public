package hu.bhr.crm.controller.dto;

import java.time.ZonedDateTime;
import java.util.UUID;

public record CustomerDetailsResponse(
        UUID id,
        UUID customerId,
        String note,
        ZonedDateTime createdAt,
        ZonedDateTime updatedAt
) {}
