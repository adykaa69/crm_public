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
                "customer_FirstName" + customerNumber,
                "customer_LastName" + customerNumber,
                "customer_Nickname" + customerNumber,
                "customer_" + customerNumber + "@email.com",
                "customer_PhoneNumber" + customerNumber,
                "customer_Relationship" + customerNumber
        );

        String requestBody = objectMapper.writeValueAsString(customerRequest);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new java.net.URI(SERVICE_URL + CUSTOMER_PATH))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                .build();

        try (var client = HttpClient.newHttpClient()) {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }

        // Check if the response contains an error (validation failure)
        if (response.statusCode() == 500) {
            PlatformResponse<ErrorResponse> errorResponse =
                    objectMapper.readValue(response.body(), new TypeReference<PlatformResponse<ErrorResponse>>() {});
            System.out.println("Validation Error: " + errorResponse.message());
            return; // Do not proceed further if the request was invalid
        }

        // Deserialize response and store created customer ID
        PlatformResponse<CustomerResponse> platformResponse =
                objectMapper.readValue(response.body(), new TypeReference<PlatformResponse<CustomerResponse>>() {});


        customerResponse = platformResponse.data();
        createdCustomerId = platformResponse.data().id();

        createdCustomerIds.add(createdCustomerId);
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
                .uri(new java.net.URI(SERVICE_URL + CUSTOMER_PATH + "/" + createdCustomerId))
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
                "updatedCustomerFirstName",
                "updatedCustomerLastName",
                "updatedCustomerNickname",
                "updatedCustomer@email.com",
                "updatedCustomerPhoneNumber",
                "updatedCustomerRelationship"
        );

        String requestBody = objectMapper.writeValueAsString(updatedRequest);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new java.net.URI(SERVICE_URL + String.format(CUSTOMER_BY_ID_PATH, createdCustomerId)))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                .build();

        try (var client = HttpClient.newHttpClient()) {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }

        PlatformResponse<CustomerResponse> platformResponse =
                objectMapper.readValue(response.body(), new TypeReference<PlatformResponse<CustomerResponse>>() {});

        customerResponse = platformResponse.data();
    }

    @Then("the response should contain the updated customer's details")
    public void theResponseShouldContainTheUpdatedCustomerDetails() throws Exception {
        theResponseShouldContainTheCustomerDetails();
    }

    @After
    public void cleanUpAfterScenario() throws Exception {
        if (!createdCustomerIds.isEmpty()) {
            for (String id: createdCustomerIds) {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(new java.net.URI(SERVICE_URL+ CUSTOMER_PATH + "/" + id))
                        .DELETE()
                        .build();

                try (var client = HttpClient.newHttpClient()) {
                    response = client.send(request, HttpResponse.BodyHandlers.ofString());
                }
            }
        }
    }
}
