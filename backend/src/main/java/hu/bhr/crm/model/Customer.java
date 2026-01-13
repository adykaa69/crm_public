package hu.bhr.crm.model;

import lombok.Builder;
import lombok.With;

import java.time.Instant;
import java.util.UUID;

@Builder(toBuilder = true)
public record Customer(
        UUID id,
        String firstName,
        String lastName,
        String nickname,
        String email,
        String phoneNumber,
        String relationship,
        @With Residence residence,
        Instant createdAt,
        Instant updatedAt
) {}