package hu.bhr.crm.step_definition.dto;

import lombok.Builder;

@Builder
public record CustomerDetailsRequest(
        String note
) {}
