package hu.bhr.crm.assertions;

import hu.bhr.crm.step_definition.dto.PlatformResponse;
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
            if (mapData.containsKey("messages") && mapData.get("messages") instanceof List<?> messages) {
                return messages.stream()
                        .filter(m -> m instanceof String)
                        .anyMatch(m -> m.equals(expectedErrorMessage));
            }
        }

        return false;
    }
}
