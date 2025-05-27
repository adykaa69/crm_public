package hu.bhr.crm.controller.dto;

import java.sql.Timestamp;
import java.util.UUID;

public record CustomerResponse(
        UUID id,
        String firstName,
        String lastName,
        String nickname,
        String email,
        String phoneNumber,
        String relationship,
        ResidenceResponse residence,
        Timestamp createdAt,
        Timestamp updatedAt
) {}