package hu.bhr.crm.controller.dto;

public record ResidenceRequest(
        String zipCode,
        String streetAddress,
        String addressLine2,
        String city,
        String country
) {}
