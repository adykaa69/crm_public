package hu.bhr.crm.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.ZonedDateTime;
import java.util.UUID;

@Schema(description = "Customer details returned by the API")
public record CustomerResponse(
        @Schema(description = "Unique identifier of the customer", example = "a1b2c3d4-e5f6-7890-1234-567890abcdef")
        UUID id,

        @Schema(description = "First name", example = "John")
        String firstName,

        @Schema(description = "Last name", example = "Doe")
        String lastName,

        @Schema(description = "Nickname", example = "Johnny")
        String nickname,

        @Schema(description = "Email address", example = "john.doe@example.com")
        String email,

        @Schema(description = "Phone number", example = "+36301234567")
        String phoneNumber,

        @Schema(description = "Type or nature of the relationship", example = "cousin")
        String relationship,

        @Schema(description = "Residence address details")
        ResidenceResponse residence,

        @Schema(description = "Timestamp of creation", example = "2023-10-01T12:00:00Z")
        ZonedDateTime createdAt,

        @Schema(description = "Timestamp of most recent update", example = "2023-10-02T14:30:00Z")
        ZonedDateTime updatedAt
) {}