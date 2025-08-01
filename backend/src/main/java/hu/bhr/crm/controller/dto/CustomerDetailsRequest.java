package hu.bhr.crm.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record CustomerDetailsRequest(
        @NotBlank(message = "Note is required")
        String note
) {}
