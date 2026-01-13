package hu.bhr.crm.assertions;

import hu.bhr.crm.step_definition.dto.ErrorResponse;
import lombok.experimental.UtilityClass;
import org.assertj.core.api.Assertions;

@UtilityClass
public class PlatformAssertions {

    public static void assertContainsErrorMessage(ErrorResponse errorResponse, String expectedMessage) {
        Assertions.assertThat(errorResponse.errorMessages())
                .as("Expected error message not found: %s", expectedMessage)
                .anyMatch(msg -> msg.contains(expectedMessage));
    }
}
