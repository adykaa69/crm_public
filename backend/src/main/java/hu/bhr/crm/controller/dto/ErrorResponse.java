package hu.bhr.crm.controller.dto;

public record ErrorResponse(
        String errorCode,
        int statusCode,
        long timestamp
) {}