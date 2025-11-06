package hu.bhr.backend.step_definition.dto;

public record CustomerRequest(
        String firstName,
        String lastName,
        String nickname,
        String email,
        String phoneNumber,
        String relationship,
        ResidenceRequest residenceRequest
) {}
