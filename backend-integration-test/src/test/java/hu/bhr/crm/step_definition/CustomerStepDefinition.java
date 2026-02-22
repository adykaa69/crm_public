package hu.bhr.crm.step_definition;

import com.fasterxml.jackson.core.type.TypeReference;
import hu.bhr.crm.api.ApiResponseParser;
import hu.bhr.crm.api.CustomerApiClient;
import hu.bhr.crm.assertions.CustomerAssertions;
import hu.bhr.crm.assertions.HttpAssertions;
import hu.bhr.crm.context.CustomerContext;
import hu.bhr.crm.context.GlobalTestContext;
import hu.bhr.crm.step_definition.dto.CustomerRequest;
import hu.bhr.crm.step_definition.dto.CustomerResponse;
import hu.bhr.crm.step_definition.dto.PlatformResponse;
import hu.bhr.crm.step_definition.dto.ResidenceRequest;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;

import java.net.http.HttpResponse;
import java.util.List;

@RequiredArgsConstructor
public class CustomerStepDefinition {

    private final CustomerContext customerContext;
    private final GlobalTestContext globalTestContext;
    private final CustomerApiClient apiClient = new CustomerApiClient();

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
        customerContext.setLastResponse(apiClient.getCustomerById(customerContext.getCreatedCustomerId()));
    }

    @When("all customers are retrieved")
    public void allCustomersAreRetrieved() throws Exception {
        customerContext.setLastResponse(apiClient.getAllCustomers());
    }

    @Then("the response should contain the customer's details")
    public void theResponseShouldContainTheCustomerDetails() throws Exception {
        PlatformResponse<CustomerResponse> resultResponse =
                ApiResponseParser.parseResponse(customerContext.getLastResponse(), new TypeReference<>() {});
        CustomerResponse expectedResponse = customerContext.getLastCustomerResponse();

        CustomerAssertions.assertCustomersEqual(expectedResponse, resultResponse.content());
    }

    @Then("the response should contain all customer's details")
    public void theResponseShouldContainAllCustomersDetails() throws Exception {
        PlatformResponse<List<CustomerResponse>> resultResponse =
                ApiResponseParser.parseResponse(customerContext.getLastResponse(), new TypeReference<>() {});
        List<String> expectedIds = globalTestContext.getCreatedCustomerIds();

        for (String id : expectedIds) {
            boolean found = resultResponse.content().stream()
                    .anyMatch(c -> c.id().equals(id));
            Assertions.assertThat(found)
                    .as("Customer with ID %s should be in the response", id)
                    .isTrue();
        }
    }

    @Given("the customer database is empty")
    public void theCustomerDatabaseIsEmpty() {
        Assertions.assertThat(globalTestContext.getCreatedCustomerIds()).isEmpty();
    }

    @When("the created customer is deleted")
    public void theCreatedCustomerIsDeleted() throws Exception {
        HttpResponse<String> response = apiClient.deleteCustomer(customerContext.getCreatedCustomerId());
        customerContext.setLastResponse(response);
        if (response.statusCode() == 204) {
            globalTestContext.getCreatedCustomerIds().remove(customerContext.getCreatedCustomerId());
        }
    }

    @And("the created customer should no longer exist in the database")
    public void theCreatedCustomerShouldNoLongerExistInTheDatabase() throws Exception {
        HttpResponse<String> response = apiClient.getCustomerById(customerContext.getCreatedCustomerId());
        HttpAssertions.assertStatusCode(response, 404);
    }

    @When("the created customer's details are updated")
    public void theCreatedCustomerDetailsAreUpdated() throws Exception {
        CustomerRequest updatedRequest = CustomerRequest.builder()
                .firstName("customer_updatedFirstName")
                .lastName("customer_updatedLastName")
                .nickname("customer_updatedNickname")
                .email("customer_updated@email.com")
                .phoneNumber("customer_updatedPhoneNumber")
                .relationship("customer_updatedRelationship")
                .residence(ResidenceRequest.builder()
                        .zipCode("residence_updatedZipCode")
                        .streetAddress("residence_UpdatedStreet")
                        .addressLine2("residence_UpdatedAddress")
                        .city("residence_UpdatedCity")
                        .country("residence_UpdatedCountry")
                        .build())
                .build();

        HttpResponse<String> response = apiClient.updateCustomer(customerContext.getCreatedCustomerId(), updatedRequest);
        customerContext.setLastResponse(response);

        if (customerContext.getLastResponse().statusCode() == 200) {
            PlatformResponse<CustomerResponse> resultResponse =
                    ApiResponseParser.parseResponse(customerContext.getLastResponse(), new TypeReference<>() {});
            customerContext.setLastCustomerResponse(resultResponse.content());
        }
    }

    @Then("the response should contain the updated customer's details")
    public void theResponseShouldContainTheUpdatedCustomerDetails() throws Exception {
        theResponseShouldContainTheCustomerDetails();
    }

    @When("the customer with ID {string} is requested")
    public void theCustomerWithNonExistentIdIsRequested(String id) throws Exception {
        customerContext.setLastResponse(apiClient.getCustomerById(id));
    }

    @When("a new customer is created with invalid email")
    public void aNewCustomerIsCreatedWithInvalidEmail() throws Exception {
        CustomerRequest customerRequest = CustomerRequest.builder()
                .firstName("customer_firstName")
                .lastName("customer_lastName")
                .nickname("customer_nickname")
                .email("invalid_email.com")
                .phoneNumber("customer_phoneNumber")
                .relationship("customer_relationship")
                .build();
        customerContext.setLastResponse(apiClient.createCustomer(customerRequest));
    }

    private CustomerRequest buildCustomerRequest(int customerNumber) {
        return CustomerRequest.builder()
                .firstName("customer_firstName_" + customerNumber)
                .lastName("customer_lastName_" + customerNumber)
                .nickname("customer_nickname_" + customerNumber)
                .phoneNumber("customer_phoneNumber_" + customerNumber)
                .relationship("customer_relationship_" + customerNumber)
                .email("customer_email_" + customerNumber + "@email.com")
                .residence(createResidenceRequest())
                .build();
    }

    private ResidenceRequest createResidenceRequest() {
        return ResidenceRequest.builder()
                .zipCode("residence_zipCode")
                .streetAddress("residence_street")
                .addressLine2("residence_address")
                .city("residence_city")
                .country("residence_country")
                .build();
    }

    private void createCustomer(int customerNumber) throws Exception {
        CustomerRequest request = buildCustomerRequest(customerNumber);
        HttpResponse<String> response = apiClient.createCustomer(request);
        customerContext.setLastResponse(response);

        if (response.statusCode() == 201) {
            PlatformResponse<CustomerResponse> parsed =
                    ApiResponseParser.parseResponse(response, new TypeReference<>() {});
            customerContext.setCreatedCustomerId(parsed.content().id());
            globalTestContext.addCreatedCustomerId(parsed.content().id());
            customerContext.setLastCustomerResponse(parsed.content());
        }
    }

    @After
    public void cleanUpAfterScenario() throws Exception {
        if (!globalTestContext.getCreatedCustomerIds().isEmpty()) {
            for (String id: globalTestContext.getCreatedCustomerIds()) {
                apiClient.deleteCustomer(id);
            }
            globalTestContext.clear();
        }
    }
}
