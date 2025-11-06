package hu.bhr.backend.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import hu.bhr.backend.Constants;
import hu.bhr.backend.HttpRequestFactory;
import hu.bhr.backend.step_definition.dto.CustomerRequest;
import hu.bhr.backend.step_definition.dto.PlatformResponse;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CustomerApiClient {

    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final ObjectMapper objectMapper =
            new ObjectMapper().registerModule(new JavaTimeModule());

    private static final String CUSTOMER_PATH = "/api/v1/customers";
    private static final String CUSTOMER_BY_ID_PATH = CUSTOMER_PATH + "/%s";

    public HttpResponse<String> createCustomer(CustomerRequest request) throws Exception {
        String body = objectMapper.writeValueAsString(request);
        HttpRequest httpRequest = HttpRequestFactory.createPost(Constants.SERVICE_URL + CUSTOMER_PATH, body);
        return httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> getCustomerById(String id) throws Exception {
        HttpRequest httpRequest = HttpRequestFactory.createGet(
                Constants.SERVICE_URL + String.format(CUSTOMER_BY_ID_PATH, id));
        return httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> getAllCustomers() throws Exception {
        HttpRequest httpRequest = HttpRequestFactory.createGet(Constants.SERVICE_URL + CUSTOMER_PATH);
        return httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> deleteCustomer(String id) throws Exception {
        HttpRequest httpRequest = HttpRequestFactory.createDelete(
                Constants.SERVICE_URL + String.format(CUSTOMER_BY_ID_PATH, id));
        return httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> updateCustomer(String id, CustomerRequest request) throws Exception {
        String body = objectMapper.writeValueAsString(request);
        HttpRequest httpRequest = HttpRequestFactory.createPut(
                Constants.SERVICE_URL + String.format(CUSTOMER_BY_ID_PATH, id), body);
        return httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }

    public <T> PlatformResponse<T> parseResponse(HttpResponse<String> response, TypeReference<PlatformResponse<T>> type)
            throws Exception {
        return objectMapper.readValue(response.body(), type);
    }
}
