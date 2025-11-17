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
import org.assertj.core.api.Assertions;

import java.net.http.HttpResponse;
import java.util.List;

public class CustomerStepDefinition {

    private final CustomerContext customerContext;
    private final GlobalTestContext globalTestContext;
    private final CustomerApiClient apiClient = new CustomerApiClient();

    public CustomerStepDefinition(CustomerContext context,
                                  GlobalTestContext globalTestContext) {
        this.customerContext = context;
        this.globalTestContext = globalTestContext;
    }

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

        CustomerAssertions.assertCustomersEqual(expectedResponse, resultResponse.data());
    }

    @Then("the response should contain all customer's details")
    public void theResponseShouldContainAllCustomersDetails() throws Exception {
        PlatformResponse<List<CustomerResponse>> resultResponse =
                ApiResponseParser.parseResponse(customerContext.getLastResponse(), new TypeReference<>() {});
        List<String> expectedIds = globalTestContext.getCreatedCustomerIds();

        for (String id : expectedIds) {
            boolean found = resultResponse.data().stream()
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
        globalTestContext.getCreatedCustomerIds().remove(customerContext.getCreatedCustomerId());
    }

    @And("the created customer should no longer exist in the database")
    public void theCreatedCustomerShouldNoLongerExistInTheDatabase() throws Exception {
        HttpResponse<String> response = apiClient.getCustomerById(customerContext.getCreatedCustomerId());
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

        HttpResponse<String> response = apiClient.updateCustomer(customerContext.getCreatedCustomerId(), updatedRequest);
        customerContext.setLastResponse(response);

        if (customerContext.getLastResponse().statusCode() == 200) {
            PlatformResponse<CustomerResponse> resultResponse =
                    ApiResponseParser.parseResponse(customerContext.getLastResponse(), new TypeReference<>() {});
            customerContext.setLastCustomerResponse(resultResponse.data());
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
        CustomerRequest customerRequest = new CustomerRequest(
                "customer_firstName",
                "customer_lastName",
                "customer_nickname",
                "invalid_email.com",
                "customer_phoneNumber",
                "customer_relationship",
                null
        );
        customerContext.setLastResponse(apiClient.createCustomer(customerRequest));
    }

    private CustomerRequest buildCustomerRequest(int customerNumber) {
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
        customerContext.setLastResponse(response);

        if (response.statusCode() == 201) {
            PlatformResponse<CustomerResponse> parsed =
                    ApiResponseParser.parseResponse(response, new TypeReference<>() {});
            customerContext.setCreatedCustomerId(parsed.data().id());
            globalTestContext.addCreatedCustomerId(parsed.data().id());
            customerContext.setLastCustomerResponse(parsed.data());
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
