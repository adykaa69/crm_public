package hu.bhr.crm.assertions;

import hu.bhr.crm.step_definition.dto.ErrorResponse;
import org.assertj.core.api.Assertions;

public class PlatformAssertions {

    public static void assertContainsErrorMessage(ErrorResponse errorResponse, String expectedMessage) {
        Assertions.assertThat(errorResponse.errorMessages())
                .as("Expected error message not found: %s", expectedMessage)
                .contains(expectedMessage);
    }
}
