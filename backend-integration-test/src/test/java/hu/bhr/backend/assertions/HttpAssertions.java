package hu.bhr.backend.assertions;

import org.assertj.core.api.Assertions;

import java.net.http.HttpResponse;

public class HttpAssertions {

    public static void assertStatusCode(HttpResponse<?> response, int expectedStatus) {
        Assertions.assertThat(response.statusCode())
                .as("Expected HTTP status to be %s but was %s", expectedStatus, response.statusCode())
                .isEqualTo(expectedStatus);
    }
}
