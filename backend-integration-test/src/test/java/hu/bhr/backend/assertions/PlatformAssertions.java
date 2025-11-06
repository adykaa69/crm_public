package hu.bhr.backend.assertions;

import hu.bhr.backend.step_definition.dto.PlatformResponse;
import org.assertj.core.api.Assertions;

import java.util.List;
import java.util.Map;

public class PlatformAssertions {

    public static void assertContainsErrorMessage(PlatformResponse<?> response, String expectedMessage) {
        boolean found = containsErrorMessage(response, expectedMessage);

        Assertions.assertThat(found)
                .as("Expected error message not found: %s", expectedMessage)
                .isTrue();
    }

    private static boolean containsErrorMessage(PlatformResponse<?> platformResponse, String expectedErrorMessage) {
        Object data = platformResponse.data();

        if (data instanceof Map<?, ?> mapData) {
            if (mapData.containsKey("errors") && mapData.get("errors") instanceof List<?> errorsList) {
                // ValidationErrorResponse
                return errorsList.stream()
                        .filter(e -> e instanceof Map<?, ?>)
                        .map(e -> (Map<?, ?>) e)
                        .anyMatch(e -> expectedErrorMessage.equals(e.get("errorMessage")));
            }

            if (mapData.containsKey("errorMessage")) {
                // ErrorResponse
                return expectedErrorMessage.equals(mapData.get("errorMessage"));
            }
        }

        return false;
    }
}
