package hu.bhr.crm.assertions;

import hu.bhr.crm.step_definition.dto.CustomerResponse;
import org.assertj.core.api.Assertions;

public class CustomerAssertions {

    public static void assertCustomersEqual(CustomerResponse expectedResponse, CustomerResponse actualResponse) {
        Assertions.assertThat(actualResponse)
                .usingRecursiveComparison()
                .ignoringFields("createdAt", "updatedAt")
                .isEqualTo(expectedResponse);
    }
}
