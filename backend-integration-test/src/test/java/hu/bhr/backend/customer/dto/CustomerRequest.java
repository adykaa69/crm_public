package hu.bhr.backend.customer.dto;

public record CustomerRequest(
        String firstName,
        String lastName,
        String nickname,
        String email,
        String phoneNumber,
        String relationship
) {}
