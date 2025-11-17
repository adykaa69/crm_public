package hu.bhr.crm.step_definition;

import com.fasterxml.jackson.core.type.TypeReference;
import hu.bhr.crm.api.ApiResponseParser;
import hu.bhr.crm.assertions.HttpAssertions;
import hu.bhr.crm.assertions.PlatformAssertions;
import hu.bhr.crm.context.GlobalTestContext;
import hu.bhr.crm.step_definition.dto.PlatformResponse;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;

public class CommonStepDefinition {

    private final GlobalTestContext context;

    public CommonStepDefinition(GlobalTestContext context) {
        this.context = context;
    }

    @And("the status code should be {int}")
    public void theStatusCodeShouldBe(int expectedStatus) {
        HttpAssertions.assertStatusCode(context.getLastResponse(), expectedStatus);
    }

    @Then("the response should contain the error message: {string}")
    public void theResponseShouldContainTheErrorMessage(String expectedErrorMessage) throws Exception {
        PlatformResponse<?> resultResponse =
                ApiResponseParser.parseResponse(context.getLastResponse(), new TypeReference<>() {});

        PlatformAssertions.assertContainsErrorMessage(resultResponse, expectedErrorMessage);
    }
}
