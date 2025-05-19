package hu.bhr.backend.customer;

import hu.bhr.backend.HttpRequestFactory;
import hu.bhr.backend.customer.dto.CustomerRequest;
import hu.bhr.backend.customer.dto.CustomerResponse;
import hu.bhr.backend.customer.dto.ErrorResponse;
import hu.bhr.backend.customer.dto.PlatformResponse;
import io.cucumber.core.internal.com.fasterxml.jackson.core.type.TypeReference;
import io.cucumber.core.internal.com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static hu.bhr.backend.Constants.SERVICE_URL;

@Component
public class CustomerStepDefinition {

    private static final String CUSTOMER_PATH = "/api/v1/customers";
    private static final String CUSTOMER_BY_ID_PATH = CUSTOMER_PATH + "/%s";

    private CustomerResponse customerResponse;
    private HttpResponse<String> response;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private String createdCustomerId;
    private final List<String> createdCustomerIds = new ArrayList<>();

    @Given("a new customer is created")
    public void aNewCustomerIsCreated() throws Exception {
        createNewCustomer(1);
    }

    @Given("{int} new customers are created")
    public void newCustomersAreCreated(int numberOfCustomers) throws Exception {
        for (int i = 1; i < numberOfCustomers; i++) {
            createNewCustomer(i);
        }
    }

    public void createNewCustomer(int customerNumber) throws Exception {
        CustomerRequest customerRequest = new CustomerRequest(
                "customer_firstName" + customerNumber,
                "customer_lastName" + customerNumber,
                "customer_nickname" + customerNumber,
                "customer_" + customerNumber + "@email.com",
                "customer_phoneNumber" + customerNumber,
                "customer_relationship" + customerNumber
        );
        sendCustomerRequest(customerRequest);
    }

    public void sendCustomerRequest(CustomerRequest customerRequest) throws Exception {
        String requestBody = objectMapper.writeValueAsString(customerRequest);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(SERVICE_URL + CUSTOMER_PATH))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                .build();

        try (var client = HttpClient.newHttpClient()) {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }

        if (response.statusCode() == 201) {
            // Deserialize response and store created customer ID
            PlatformResponse<CustomerResponse> platformResponse =
                    objectMapper.readValue(response.body(), new TypeReference<PlatformResponse<CustomerResponse>>() {});

            customerResponse = platformResponse.data();
            createdCustomerId = platformResponse.data().id();
            createdCustomerIds.add(createdCustomerId);
        }
    }

    @When("the customer is retrieved by ID")
    public void theCustomerIsRetrievedByID() throws Exception {
        HttpRequest request = HttpRequestFactory.createGet(SERVICE_URL + String.format(CUSTOMER_BY_ID_PATH, createdCustomerId));
        try (var client = HttpClient.newHttpClient()) {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }
    }

    @When("all customers are retrieved")
    public void allCustomersAreRetrieved() throws Exception {
        HttpRequest request = HttpRequestFactory.createGet(SERVICE_URL + CUSTOMER_PATH);
        try (var client = HttpClient.newHttpClient()) {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }
    }

    @Then("the response should contain the customer's details")
    public void theResponseShouldContainTheCustomerDetails() throws Exception {
        PlatformResponse<CustomerResponse> platformResponse =
                objectMapper.readValue(response.body(), new TypeReference<PlatformResponse<CustomerResponse>>() {
                });
        CustomerResponse createdCustomer = platformResponse.data();

        Assert.assertEquals("ID should match", customerResponse.id(), createdCustomer.id());
        Assert.assertEquals("First name should match", customerResponse.firstName(), createdCustomer.firstName());
        Assert.assertEquals("Last name should match", customerResponse.lastName(), createdCustomer.lastName());
        Assert.assertEquals("Nickname should match", customerResponse.nickname(), createdCustomer.nickname());
        Assert.assertEquals("Email should match", customerResponse.email(), createdCustomer.email());
        Assert.assertEquals("Phone number should match", customerResponse.phoneNumber(), createdCustomer.phoneNumber());
        Assert.assertEquals("Relationship should match", customerResponse.relationship(), createdCustomer.relationship());
    }

    @Then("the response should contain all customer's details")
    public void theResponseShouldContainAllCustomersDetails() throws Exception {
        PlatformResponse<List<CustomerResponse>> platformResponse =
                objectMapper.readValue(response.body(), new TypeReference<PlatformResponse<List<CustomerResponse>>>() {});

        List<CustomerResponse> customersResponses = platformResponse.data();

        for (String id: createdCustomerIds) {
            boolean found = customersResponses.stream().anyMatch(c -> c.id().equals(id));
            Assert.assertTrue("Customer with ID " + id + " should be in the response", found);
        }
    }

    @And("the status code should be {int}")
    public void theStatusCodeShouldBe(int httpStatusCode) {
        Assert.assertEquals("The status code must be " + httpStatusCode, httpStatusCode, response.statusCode());
    }

    @Given("the customer database is empty")
    public void theCustomerDatabaseIsEmpty() throws Exception {
        allCustomersAreRetrieved();

        PlatformResponse<List<CustomerResponse>> platformResponse =
                objectMapper.readValue(response.body(), new TypeReference<PlatformResponse<List<CustomerResponse>>>() {});

        Assert.assertTrue("Retrieved customer list should be empty", platformResponse.data().isEmpty());
    }

    @When("the created customer is deleted")
    public void theCreatedCustomerIsDeleted() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(SERVICE_URL + CUSTOMER_PATH + "/" + createdCustomerId))
                .DELETE()
                .build();

        try (var client = HttpClient.newHttpClient()) {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }

        createdCustomerIds.remove(createdCustomerId);
    }

    @And("the created customer should no longer exist in the database")
    public void theCreatedCustomerShouldNoLongerExistInTheDatabase() throws Exception {
        HttpRequest request = HttpRequestFactory.createGet(SERVICE_URL + String.format(CUSTOMER_BY_ID_PATH, createdCustomerId));
        try (var client = HttpClient.newHttpClient()) {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }

        Assert.assertEquals("Customer should not exist after deletion", 404, response.statusCode());
    }


    @When("the created customer's details are updated")
    public void theCreatedCustomerDetailsAreUpdated() throws Exception {
        CustomerRequest updatedRequest = new CustomerRequest(
                "customer_updatedFirstName",
                "customer_updatedLastName",
                "customer_updatedNickname",
                "customer_updated@email.com",
                "customer_updatedPhoneNumber",
                "customer_updatedRelationship"
        );
        sendUpdatedRequest(updatedRequest);
    }

    public void sendUpdatedRequest(CustomerRequest updatedRequest) throws Exception {
        String requestBody = objectMapper.writeValueAsString(updatedRequest);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(SERVICE_URL + String.format(CUSTOMER_BY_ID_PATH, createdCustomerId)))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                .build();

        try (var client = HttpClient.newHttpClient()) {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }

        if (response.statusCode() == 200) {
            PlatformResponse<CustomerResponse> platformResponse =
                    objectMapper.readValue(response.body(), new TypeReference<PlatformResponse<CustomerResponse>>() {});

            customerResponse = platformResponse.data();
        }
    }

    @Then("the response should contain the updated customer's details")
    public void theResponseShouldContainTheUpdatedCustomerDetails() throws Exception {
        theResponseShouldContainTheCustomerDetails();
    }

    @When("the customer with ID {string} is requested")
    public void theCustomerWithNonExistentIdIsRequested(String ID) throws Exception {
        HttpRequest request = HttpRequestFactory.createGet(SERVICE_URL + String.format(CUSTOMER_BY_ID_PATH, ID));
        try (var client = HttpClient.newHttpClient()) {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }
    }

    @Then("the response should contain the error message: {string}")
    public void theResponseShouldContainTheErrorMessage(String expectedErrorMessage) throws Exception {
        PlatformResponse<ErrorResponse> platformResponse =
                objectMapper.readValue(response.body(), new TypeReference<PlatformResponse<ErrorResponse>>() {});

        ErrorResponse errorResponse = platformResponse.data();

        Assert.assertEquals(expectedErrorMessage, errorResponse.errorMessage());
    }

    @When("a new customer is created without relationship")
    public void aNewCustomerIsCreatedWithoutRelationship() throws Exception {
        CustomerRequest customerRequest = new CustomerRequest(
                "customer_firstName",
                "customer_lastName",
                "customer_nickname",
                "customer_@email.com",
                "customer_phoneNumber",
                null
        );
        sendCustomerRequest(customerRequest);
    }

    @When("a new customer is created without first name and nickname")
    public void aNewCustomerIsCreatedWithoutFirstNameAndNickname() throws Exception {
        CustomerRequest customerRequest = new CustomerRequest(
                "",
                "customer_lastName",
                null,
                "customer_@email.com",
                "customer_phoneNumber",
                "customer_relationship"
        );
        sendCustomerRequest(customerRequest);
    }

    @When("a new customer is created with invalid email")
    public void aNewCustomerIsCreatedWithInvalidEmail() throws Exception {
        CustomerRequest customerRequest = new CustomerRequest(
                "customer_firstName",
                "customer_lastName",
                "customer_nickname",
                "customer_email.com",
                "customer_phoneNumber",
                "customer_relationship"
        );
        sendCustomerRequest(customerRequest);
    }

    @When("the customer with ID {string} is requested to be deleted")
    public void theCustomerWithNonExistingIdIsRequestedToBeDeleted(String ID) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(SERVICE_URL + CUSTOMER_PATH + "/" + ID))
                .DELETE()
                .build();

        try (var client = HttpClient.newHttpClient()) {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }
    }

    @When("the created customer's details are updated without relationship")
    public void theCreatedCustomerDetailsAreUpdatedWithoutRelationship() throws Exception {
        CustomerRequest updatedRequest = new CustomerRequest(
                "customer_updatedFirstName",
                "customer_updatedLastName",
                "customer_updatedNickname",
                "customer_updated@email.com",
                "customer_updatedPhoneNumber",
                null
        );
        sendUpdatedRequest(updatedRequest);
    }

    @When("the created customer's details are updated without first name and nickname")
    public void theCreatedCustomerDetailsAreUpdatedWithoutFirstNameAndNickname() throws Exception {
        CustomerRequest updatedRequest = new CustomerRequest(
                " ",
                "customer_updatedLastName",
                "",
                "customer_updated@email.com",
                "customer_updatedPhoneNumber",
                "customer_updatedRelationship"
        );
        sendUpdatedRequest(updatedRequest);
    }

    @When("the created customer's details are updated with an invalid email")
    public void theCreatedCustomerDetailsAreUpdatedWithAnInvalidEmail() throws Exception {
        CustomerRequest updatedRequest = new CustomerRequest(
                "customer_updatedFirstName",
                "customer_updatedLastName",
                "customer_updatedNickname",
                "customer_updated@email",
                "customer_updatedPhoneNumber",
                "customer_updatedRelationship"
        );
        sendUpdatedRequest(updatedRequest);
    }

    @After
    public void cleanUpAfterScenario() throws Exception {
        if (!createdCustomerIds.isEmpty()) {
            for (String id: createdCustomerIds) {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI(SERVICE_URL+ CUSTOMER_PATH + "/" + id))
                        .DELETE()
                        .build();

                try (var client = HttpClient.newHttpClient()) {
                    response = client.send(request, HttpResponse.BodyHandlers.ofString());
                }
            }
        }
    }
}
