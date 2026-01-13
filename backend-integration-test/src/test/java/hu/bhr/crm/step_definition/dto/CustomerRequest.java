package hu.bhr.crm.step_definition.dto;

import lombok.Builder;

@Builder
public record CustomerRequest(
        String firstName,
        String lastName,
        String nickname,
        String email,
        String phoneNumber,
        String relationship,
        ResidenceRequest residenceRequest
) {}
