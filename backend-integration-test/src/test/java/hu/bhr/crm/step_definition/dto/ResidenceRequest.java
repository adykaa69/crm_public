package hu.bhr.crm.step_definition.dto;

import lombok.Builder;

@Builder
public record ResidenceRequest(
        String zipCode,
        String streetAddress,
        String addressLine2,
        String city,
        String country
) {}

