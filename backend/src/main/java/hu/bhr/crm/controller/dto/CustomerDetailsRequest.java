package hu.bhr.crm.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
@Schema(description = "Payload for adding or updating detailed notes for a customer")
public record CustomerDetailsRequest(
        @NotBlank(message = "Note is required")
        @Schema(
            description = "The content of the document or additional details",
            example = "Customer prefers wine over beer.",
            requiredMode = Schema.RequiredMode.REQUIRED
        )
        String note
) {}
