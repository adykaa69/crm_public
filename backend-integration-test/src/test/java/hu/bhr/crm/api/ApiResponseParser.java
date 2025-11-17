package hu.bhr.crm.api;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import hu.bhr.crm.step_definition.dto.PlatformResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.http.HttpResponse;

public class ApiResponseParser {

    private static final ObjectMapper objectMapper =
            new ObjectMapper().registerModule(new JavaTimeModule());

    private ApiResponseParser() {}

    public static <T> PlatformResponse<T> parseResponse(HttpResponse<String> response, TypeReference<PlatformResponse<T>> type)
            throws Exception {
        return objectMapper.readValue(response.body(), type);
    }
}
