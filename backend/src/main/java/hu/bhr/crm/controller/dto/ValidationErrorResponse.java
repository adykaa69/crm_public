package hu.bhr.crm.controller.dto;

import java.util.List;

public record ValidationErrorResponse(
    List<ErrorResponse> errors
) {}
