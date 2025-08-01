package hu.bhr.crm.controller.dto;

import hu.bhr.crm.validation.annotation.AtLeastOneFieldRequired;
import hu.bhr.crm.validation.annotation.ValidEmail;
import jakarta.validation.constraints.NotBlank;

@AtLeastOneFieldRequired(
        message = "At least one of First Name or Nickname is required",
        fields = {"firstName", "nickname"})
public record CustomerRequest(
        String firstName,
        String lastName,
        String nickname,
        @ValidEmail
        String email,
        String phoneNumber,
        @NotBlank(message = "Relationship is required")
        String relationship,
        ResidenceRequest residence
) {}