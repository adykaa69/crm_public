package hu.bhr.backend.context;

import hu.bhr.backend.step_definition.dto.CustomerResponse;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class CustomerContext {

    private String createdCustomerId;
    private final List<String> createdCustomerIds = new ArrayList<>();
    private HttpResponse<String> lastResponse;
    private CustomerResponse lastCustomerResponse;

    public void clear() {
        createdCustomerId = null;
        createdCustomerIds.clear();
        lastResponse = null;
        lastCustomerResponse = null;
    }

    public String getCreatedCustomerId() {
        return this.createdCustomerId;
    }

    public void setCreatedCustomerId(String createdCustomerId) {
        this.createdCustomerId = createdCustomerId;
    }

    public List<String> getCreatedCustomerIds() {
        return this.createdCustomerIds;
    }

    public void addCreatedCustomerId(String customerId) {
        this.createdCustomerIds.add(customerId);
    }

    public HttpResponse<String> getLastResponse() {
        return this.lastResponse;
    }

    public void setLastResponse(HttpResponse<String> lastResponse) {
        this.lastResponse = lastResponse;
    }

    public CustomerResponse getLastCustomerResponse() {
        return this.lastCustomerResponse;
    }

    public void setLastCustomerResponse(CustomerResponse lastCustomerResponse) {
        this.lastCustomerResponse = lastCustomerResponse;
    }
}
