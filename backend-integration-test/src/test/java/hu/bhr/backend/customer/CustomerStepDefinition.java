package hu.bhr.backend.customer;

import hu.bhr.backend.HttpRequestFactory;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static hu.bhr.backend.Constants.SERVICE_URL;

public class CustomerStepDefinition {

    private static final String CUSTOMER_PATH = "/customer";
    private static final String CUSTOMER_BODY = """
            {
                    "firstName": "%s",
                    "lastName": "%s",
                    "email": "%s",
                    "phoneNumber": "%s"
            }""";
    private String customer;
    HttpResponse<String> response;


    @Given("a customer")
    public void aCustomer() {
        customer = CUSTOMER_BODY.formatted("Béla", "Átlag", "bela.atla@gmail.com", "+36201234567");
    }

    @When("add to database")
    public void addToDatabase() throws Exception {
        HttpRequest request = HttpRequestFactory.createPost(SERVICE_URL + CUSTOMER_PATH, customer);
        try (var client = HttpClient.newHttpClient()) {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }
    }

    @Then("return {string} status code")
    public void returnStatusCode(String httpStatusCode) {
        Assert.assertEquals("The status code must be " + httpStatusCode, Integer.parseInt(httpStatusCode), response.statusCode());
    }
}
