package hu.bhr.backend.step_definition;

import com.fasterxml.jackson.core.type.TypeReference;
import hu.bhr.backend.api.CustomerApiClient;
import hu.bhr.backend.assertions.CustomerAssertions;
import hu.bhr.backend.assertions.HttpAssertions;
import hu.bhr.backend.assertions.PlatformAssertions;
import hu.bhr.backend.context.CustomerContext;
import hu.bhr.backend.step_definition.dto.CustomerRequest;
import hu.bhr.backend.step_definition.dto.CustomerResponse;
import hu.bhr.backend.step_definition.dto.PlatformResponse;
import hu.bhr.backend.step_definition.dto.ResidenceRequest;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.assertj.core.api.Assertions;
import org.springframework.stereotype.Component;

import java.net.http.HttpResponse;
import java.util.List;

@Component
public class CustomerStepDefinition {

    private final CustomerApiClient apiClient = new CustomerApiClient();
    private final CustomerContext context = new CustomerContext();

    @Given("a new customer is created")
    public void aNewCustomerIsCreated() throws Exception {
        createCustomer(1);
    }

    @Given("{int} new customers are created")
    public void newCustomersAreCreated(int numberOfCustomers) throws Exception {
        for (int i = 1; i <= numberOfCustomers; i++) {
            createCustomer(i);
        }
    }

    @When("the customer is retrieved by ID")
    public void theCustomerIsRetrievedByID() throws Exception {
        context.setLastResponse(apiClient.getCustomerById(context.getCreatedCustomerId()));
    }

    @When("all customers are retrieved")
    public void allCustomersAreRetrieved() throws Exception {
        context.setLastResponse(apiClient.getAllCustomers());
    }

    @Then("the response should contain the customer's details")
    public void theResponseShouldContainTheCustomerDetails() throws Exception {
        PlatformResponse<CustomerResponse> resultResponse =
                apiClient.parseResponse(context.getLastResponse(), new TypeReference<>() {});
        CustomerResponse expectedResponse = context.getLastCustomerResponse();

        CustomerAssertions.assertCustomersEqual(expectedResponse, resultResponse.data());
    }

    @Then("the response should contain all customer's details")
    public void theResponseShouldContainAllCustomersDetails() throws Exception {
        PlatformResponse<List<CustomerResponse>> resultResponse =
                apiClient.parseResponse(context.getLastResponse(), new TypeReference<>() {});
        List<String> expectedIds = context.getCreatedCustomerIds();

        for (String id : expectedIds) {
            boolean found = resultResponse.data().stream()
                    .anyMatch(c -> c.id().equals(id));
            Assertions.assertThat(found)
                    .as("Customer with ID %s should be in the response", id)
                    .isTrue();
        }
    }

    @And("the status code should be {int}")
    public void theStatusCodeShouldBe(int expectedStatus) {
        HttpAssertions.assertStatusCode(context.getLastResponse(), expectedStatus);
    }

    @Given("the customer database is empty")
    public void theCustomerDatabaseIsEmpty() throws Exception {
        Assertions.assertThat(context.getCreatedCustomerIds()).isEmpty();
    }

    @When("the created customer is deleted")
    public void theCreatedCustomerIsDeleted() throws Exception {
        HttpResponse<String> response = apiClient.deleteCustomer(context.getCreatedCustomerId());
        context.setLastResponse(response);
        context.getCreatedCustomerIds().remove(context.getCreatedCustomerId());
    }

    @And("the created customer should no longer exist in the database")
    public void theCreatedCustomerShouldNoLongerExistInTheDatabase() throws Exception {
        HttpResponse<String> response = apiClient.getCustomerById(context.getCreatedCustomerId());
        HttpAssertions.assertStatusCode(response, 404);
    }

    @When("the created customer's details are updated")
    public void theCreatedCustomerDetailsAreUpdated() throws Exception {
        CustomerRequest updatedRequest = new CustomerRequest(
                "customer_updatedFirstName",
                "customer_updatedLastName",
                "customer_updatedNickname",
                "customer_updated@email.com",
                "customer_updatedPhoneNumber",
                "customer_updatedRelationship",
                new ResidenceRequest(
                        "residence_updatedZipCode",
                        "residence_UpdatedStreet",
                        "residence_UpdatedAddress",
                        "residence_UpdatedCity",
                        "residence_UpdatedCountry"
                )
        );

        HttpResponse<String> response = apiClient.updateCustomer(context.getCreatedCustomerId(), updatedRequest);
        context.setLastResponse(response);

        if (context.getLastResponse().statusCode() == 200) {
            PlatformResponse<CustomerResponse> resultResponse =
                    apiClient.parseResponse(context.getLastResponse(), new TypeReference<>() {});
            context.setLastCustomerResponse(resultResponse.data());
        }
    }

    @Then("the response should contain the updated customer's details")
    public void theResponseShouldContainTheUpdatedCustomerDetails() throws Exception {
        theResponseShouldContainTheCustomerDetails();
    }

    @When("the customer with ID {string} is requested")
    public void theCustomerWithNonExistentIdIsRequested(String id) throws Exception {
        context.setLastResponse(apiClient.getCustomerById(id));
    }

    @Then("the response should contain the error message: {string}")
    public void theResponseShouldContainTheErrorMessage(String expectedErrorMessage) throws Exception {
        PlatformResponse<?> resultResponse =
                apiClient.parseResponse(context.getLastResponse(), new TypeReference<>() {});

        PlatformAssertions.assertContainsErrorMessage(resultResponse, expectedErrorMessage);
    }

    @When("a new customer is created with invalid email")
    public void aNewCustomerIsCreatedWithInvalidEmail() throws Exception {
        CustomerRequest customerRequest = new CustomerRequest(
                "customer_firstName",
                "customer_lastName",
                "customer_nickname",
                "invalid_email.com",
                "customer_phoneNumber",
                "customer_relationship",
                null
        );
        context.setLastResponse(apiClient.createCustomer(customerRequest));
    }

    private CustomerRequest buildCustomerRequest(int customerNumber) throws Exception {
        return new CustomerRequest(
                "customer_firstName_" + customerNumber,
                "customer_lastName_" + customerNumber,
                "customer_nickname_" + customerNumber,
                "customer_" + customerNumber + "_@email.com",
                "customer_phoneNumber_" + customerNumber,
                "customer_relationship_" + customerNumber,
                createResidenceRequest()
        );
    }

    private ResidenceRequest createResidenceRequest() {
        return new ResidenceRequest(
                "residence_zipCode",
                "residence_street",
                "residence_address",
                "residence_city",
                "residence_country"
        );
    }

    private void createCustomer(int customerNumber) throws Exception {
        CustomerRequest request = buildCustomerRequest(customerNumber);
        HttpResponse<String> response = apiClient.createCustomer(request);
        context.setLastResponse(response);

        if (response.statusCode() == 201) {
            PlatformResponse<CustomerResponse> parsed =
                    apiClient.parseResponse(response, new TypeReference<>() {});
            context.setCreatedCustomerId(parsed.data().id());
            context.addCreatedCustomerId(parsed.data().id());
            context.setLastCustomerResponse(parsed.data());
        }
    }

    @After
    public void cleanUpAfterScenario() throws Exception {
        if (!context.getCreatedCustomerIds().isEmpty()) {
            for (String id: context.getCreatedCustomerIds()) {
                apiClient.deleteCustomer(id);
            }
            context.clear();
        }
    }
}
