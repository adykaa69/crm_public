package hu.bhr.crm.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import hu.bhr.crm.Constants;
import hu.bhr.crm.HttpRequestFactory;
import hu.bhr.crm.step_definition.dto.CustomerDetailsRequest;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CustomerDetailsApiClient {

    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final ObjectMapper objectMapper =
            new ObjectMapper().registerModule(new JavaTimeModule());

    private static final String CUSTOMER_DETAILS_PATH = "/api/v1/customers";
    private static final String CUSTOMER_DETAILS_BY_CUSTOMER_ID_PATH = CUSTOMER_DETAILS_PATH + "/%s/details";
    private static final String CUSTOMER_DETAILS_BY_ID_PATH = CUSTOMER_DETAILS_PATH + "/details/%s";

    public HttpResponse<String> createCustomerDetails(String customerId, CustomerDetailsRequest request) throws Exception {
        String body = objectMapper.writeValueAsString(request);
        HttpRequest httpRequest = HttpRequestFactory.createPost(
                Constants.SERVICE_URL + String.format(CUSTOMER_DETAILS_BY_CUSTOMER_ID_PATH, customerId), body);
        return httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> getCustomerDetailsById(String id) throws Exception {
        HttpRequest httpRequest = HttpRequestFactory.createGet(
                Constants.SERVICE_URL + String.format(CUSTOMER_DETAILS_BY_ID_PATH, id));
        return httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> getAllCustomerDetailsByCustomerId(String customerId) throws Exception {
        HttpRequest httpRequest = HttpRequestFactory.createGet(
                Constants.SERVICE_URL + String.format(CUSTOMER_DETAILS_BY_CUSTOMER_ID_PATH, customerId));
        return httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> deleteCustomerDetails(String id) throws Exception {
        HttpRequest httpRequest = HttpRequestFactory.createDelete(
                Constants.SERVICE_URL + String.format(CUSTOMER_DETAILS_BY_ID_PATH, id));
        return httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> updateCustomerDetails(String id, CustomerDetailsRequest request) throws Exception {
        String body = objectMapper.writeValueAsString(request);
        HttpRequest httpRequest = HttpRequestFactory.createPut(
                Constants.SERVICE_URL + String.format(CUSTOMER_DETAILS_BY_ID_PATH, id), body);
        return httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }
}
