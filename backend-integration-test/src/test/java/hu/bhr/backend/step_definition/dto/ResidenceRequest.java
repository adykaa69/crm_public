package hu.bhr.backend.step_definition.dto;

public record ResidenceRequest(
        String zipCode,
        String streetAddress,
        String addressLine2,
        String city,
        String country
) {}

