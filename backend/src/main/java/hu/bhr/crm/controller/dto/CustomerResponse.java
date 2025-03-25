package hu.bhr.crm.controller.dto;

import java.sql.Timestamp;

public record CustomerResponse(
        String id,
        String firstName,
        String lastName,
        String nickname,
        String email,
        String phoneNumber,
        String relationship,
        Timestamp createdAt,
        Timestamp updatedAt
) {}