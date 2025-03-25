package hu.bhr.crm.controller.dto;

public record PlatformResponse<T>(
    String status,
    String message,
    T data
) {}
