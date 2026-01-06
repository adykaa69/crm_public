package hu.bhr.crm.controller.dto;

public record PlatformResponse<T>(
    T content
) {}
