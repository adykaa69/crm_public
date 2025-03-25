package hu.bhr.backend.customer.dto;

import io.cucumber.core.internal.com.fasterxml.jackson.annotation.JsonProperty;

public record CustomerResponse(
    @JsonProperty("id") String id,
    @JsonProperty("firstName") String firstName,
    @JsonProperty("lastName") String lastName,
    @JsonProperty("nickname") String nickname,
    @JsonProperty("email") String email,
    @JsonProperty("phoneNumber") String phoneNumber,
    @JsonProperty("relationship") String relationship,
    @JsonProperty("createdAt") String createdAt,
    @JsonProperty("updatedAt") String updatedAt
) {}
