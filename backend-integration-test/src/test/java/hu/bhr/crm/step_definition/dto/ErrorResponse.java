package hu.bhr.crm.step_definition.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;
import java.util.List;

public record ErrorResponse(
    @JsonProperty("status") String status,
    @JsonProperty("title") String title,
    @JsonProperty("errorMessages") List<String> errorMessages,
    @JsonProperty("timestamp") ZonedDateTime timestamp
) {}
