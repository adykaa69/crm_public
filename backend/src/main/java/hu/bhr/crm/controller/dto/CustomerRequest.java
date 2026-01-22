package hu.bhr.crm.controller.dto;

import hu.bhr.crm.validation.annotation.AtLeastOneFieldRequired;
import hu.bhr.crm.validation.annotation.ValidEmail;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@AtLeastOneFieldRequired(
        message = "At least one of First Name or Nickname is required",
        fields = {"firstName", "nickname"})
@Schema(description = "Payload for registering or updating a customer")
@Builder
public record CustomerRequest(
        @Schema(description = "First name (required if nickname is missing)", example = "John")
        String firstName,

        @Schema(description = "Last name", example = "Doe")
        String lastName,

        @Schema(description = "Nickname (required if first name is missing)", example = "Johnny")
        String nickname,

        @ValidEmail
        @Schema(description = "Valid email address", example = "john.doe@example.com")
        String email,

        @Schema(description = "Phone number in international format", example = "+36301234567")
        String phoneNumber,

        @NotBlank(message = "Relationship is required")
        @Schema(
            description = "Type or nature of the relationship",
            example = "cousin",
            requiredMode = Schema.RequiredMode.REQUIRED
        )
        String relationship,

        @Schema(description = "Residence address details")
        ResidenceRequest residence
) {}