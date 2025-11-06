package hu.bhr.backend.assertions;

import hu.bhr.backend.step_definition.dto.CustomerResponse;
import org.assertj.core.api.Assertions;

public class CustomerAssertions {

    public static void assertCustomersEqual(CustomerResponse expectedResponse, CustomerResponse actualResponse) {
        Assertions.assertThat(actualResponse)
                .usingRecursiveAssertion()
                .ignoringFields("createdAt", "updatedAt")
                .isEqualTo(expectedResponse);
    }
}
