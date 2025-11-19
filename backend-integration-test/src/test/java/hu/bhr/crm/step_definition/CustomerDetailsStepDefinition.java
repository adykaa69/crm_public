package hu.bhr.crm.step_definition;

import com.fasterxml.jackson.core.type.TypeReference;
import hu.bhr.crm.api.ApiResponseParser;
import hu.bhr.crm.api.CustomerApiClient;
import hu.bhr.crm.api.CustomerDetailsApiClient;
import hu.bhr.crm.assertions.HttpAssertions;
import hu.bhr.crm.context.CustomerDetailsContext;
import hu.bhr.crm.context.GlobalTestContext;
import hu.bhr.crm.step_definition.dto.CustomerDetailsRequest;
import hu.bhr.crm.step_definition.dto.CustomerDetailsResponse;
import hu.bhr.crm.step_definition.dto.CustomerRequest;
import hu.bhr.crm.step_definition.dto.CustomerResponse;
import hu.bhr.crm.step_definition.dto.PlatformResponse;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.assertj.core.api.Assertions;

import java.net.http.HttpResponse;
import java.util.List;

public class CustomerDetailsStepDefinition {

    private final CustomerDetailsContext customerDetailsContext;
    private final GlobalTestContext globalTestContext;
    private final CustomerDetailsApiClient customerDetailsApiClient = new CustomerDetailsApiClient();
    private final CustomerApiClient customerApiClient = new CustomerApiClient();

    public CustomerDetailsStepDefinition(CustomerDetailsContext customerDetailsContext,
                                         GlobalTestContext globalTestContext) {
        this.customerDetailsContext = customerDetailsContext;
        this.globalTestContext = globalTestContext;
    }

    @Given("a new customer document is created")
    public void aNewCustomerDocumentIsCreated() throws Exception {
        createCustomerDocument(1);
    }

    @Given("{int} new customer documents are created")
    public void newCustomerDocumentsAreCreated(int numberOfDocuments) throws Exception {
        for (int i = 1; i <= numberOfDocuments; i++) {
            createCustomerDocument(i);
        }
    }

    @When("the customer document is retrieved by ID")
    public void theCustomerDocumentIsRetrievedByID() throws Exception {
        customerDetailsContext.setLastResponse(
                customerDetailsApiClient.getCustomerDetailsById(
                        customerDetailsContext.getCreatedCustomerDetailsId()
                )
        );
    }

    @When("all customer documents are retrieved by customer ID")
    public void allCustomerDocumentsAreRetrievedByCustomerID() throws Exception {
        customerDetailsContext.setLastResponse(
                customerDetailsApiClient.getAllCustomerDetailsByCustomerId(
                        customerDetailsContext.getCustomerId()
                )
        );
    }

    @Then("the response should contain the customer document's details")
    public void theResponseShouldContainTheCustomerDocumentsDetails() throws Exception {
        PlatformResponse<CustomerDetailsResponse> resultResponse =
                ApiResponseParser.parseResponse(customerDetailsContext.getLastResponse(), new TypeReference<>() {});
        CustomerDetailsResponse expectedResponse = customerDetailsContext.getLastCustomerDetailsResponse();

        Assertions.assertThat(resultResponse.data())
                .usingRecursiveComparison()
                .ignoringFields("createdAt", "updatedAt")
                .isEqualTo(expectedResponse);
    }

    @Then("the response should contain all customer documents' details")
    public void theResponseShouldContainAllCustomerDocumentsDetails() throws Exception {
        PlatformResponse<List<CustomerDetailsResponse>> resultResponse =
                ApiResponseParser.parseResponse(customerDetailsContext.getLastResponse(), new TypeReference<>() {});
        List<String> expectedIds = customerDetailsContext.getCreatedCustomerDetailsIds();

        for (String id : expectedIds) {
            boolean found = resultResponse.data().stream()
                    .anyMatch(cd -> cd.id().equals(id));
            Assertions.assertThat(found)
                    .as("Customer document with ID %s should be in the response", id)
                    .isTrue();
        }
    }

    @Given("the customer document database is empty")
    public void theCustomerDocumentDatabaseIsEmpty() {
        Assertions.assertThat(customerDetailsContext.getCreatedCustomerDetailsIds()).isEmpty();
    }

    @When("the created customer document is deleted")
    public void theCreatedCustomerDocumentIsDeleted() throws Exception {
        HttpResponse<String> response = customerDetailsApiClient.deleteCustomerDetails(
                customerDetailsContext.getCreatedCustomerDetailsId()
        );
        customerDetailsContext.setLastResponse(response);
        customerDetailsContext.getCreatedCustomerDetailsIds()
                .remove(customerDetailsContext.getCreatedCustomerDetailsId());
    }

    @And("the created customer document should no longer exist in the database")
    public void theCreatedCustomerDocumentShouldNoLongerExistInTheDatabase() throws Exception {
        HttpResponse<String> response = customerDetailsApiClient.getCustomerDetailsById(
                customerDetailsContext.getCreatedCustomerDetailsId()
        );
        HttpAssertions.assertStatusCode(response, 404);
    }

    @When("the created customer document's details are updated")
    public void theCreatedCustomerDocumentSDetailsAreUpdated() throws Exception {
        CustomerDetailsRequest updatedRequest = new CustomerDetailsRequest(
                "note_updated"
        );
        HttpResponse<String> response = customerDetailsApiClient.updateCustomerDetails(
                customerDetailsContext.getCreatedCustomerDetailsId(),
                updatedRequest
        );
        customerDetailsContext.setLastResponse(response);

        if (response.statusCode() == 200) {
            PlatformResponse<CustomerDetailsResponse> resultResponse =
                    ApiResponseParser.parseResponse(response, new TypeReference<>() {});
            customerDetailsContext.setLastCustomerDetailsResponse(resultResponse.data());
        }
    }

    @Then("the response should contain the updated customer document's details")
    public void theResponseShouldContainTheUpdatedCustomerDocumentSDetails() throws Exception {
        theResponseShouldContainTheCustomerDocumentsDetails();
    }

    @When("the customer document with ID {string} is requested")
    public void theCustomerDocumentWithNonExistentIdIsRequested(String documentId) throws Exception {
        customerDetailsContext.setLastResponse(
                customerDetailsApiClient.getCustomerDetailsById(documentId)
        );
    }

    @When("a new customer document is created without a note")
    public void aNewCustomerDocumentIsCreatedWithoutANote() throws Exception {
        ensureRelatedCustomerExists();

        CustomerDetailsRequest request = new CustomerDetailsRequest(
                ""
        );
        customerDetailsContext.setLastResponse(customerDetailsApiClient
                .createCustomerDetails(customerDetailsContext.getCustomerId(), request));
    }

    private void createCustomerDocument(int documentNumber) throws Exception {
        ensureRelatedCustomerExists();

        CustomerDetailsRequest request = buildCustomerDetailsRequest(documentNumber);
        HttpResponse<String> response = customerDetailsApiClient.createCustomerDetails(
                customerDetailsContext.getCustomerId(),
                request
        );
        customerDetailsContext.setLastResponse(response);

        if (response.statusCode() == 201) {
            PlatformResponse<CustomerDetailsResponse> parsed =
                    ApiResponseParser.parseResponse(response, new TypeReference<>() {});
            String customerDetailsId = parsed.data().id();

            customerDetailsContext.setCreatedCustomerDetailsId(customerDetailsId);
            customerDetailsContext.addCreatedCustomerDetailsId(customerDetailsId);
            customerDetailsContext.setLastCustomerDetailsResponse(parsed.data());
        }
    }

    private CustomerDetailsRequest buildCustomerDetailsRequest(int documentNumber) {
        return new CustomerDetailsRequest(
                "note_" + documentNumber
        );
    }

    private void ensureRelatedCustomerExists() throws Exception {
        if (customerDetailsContext.getCustomerId() == null) {
            CustomerRequest request = new CustomerRequest(
                    "firstName",
                    "lastName",
                    "nickname",
                    "email@example.com",
                    "phoneNumber",
                    "relationship",
                    null
            );
            HttpResponse<String> response = customerApiClient.createCustomer(request);
            PlatformResponse<CustomerResponse> parsed =
                    ApiResponseParser.parseResponse(response, new TypeReference<>() {});
            String customerId = parsed.data().id();

            customerDetailsContext.setCustomerId(customerId);
            globalTestContext.addCreatedCustomerId(customerId);
        }
    }

    @After
    public void cleanUpAfterScenario() throws Exception {
        if (!customerDetailsContext.getCreatedCustomerDetailsIds().isEmpty()) {
            for (String id : customerDetailsContext.getCreatedCustomerDetailsIds()) {
                customerDetailsApiClient.deleteCustomerDetails(id);
            }
            globalTestContext.clear();
        }
    }
}
