package hu.bhr.crm.context;

import hu.bhr.crm.step_definition.dto.CustomerDetailsResponse;

import java.util.ArrayList;
import java.util.List;

public class CustomerDetailsContext extends BaseContext {

    private String createdCustomerDetailsId;
    private final List<String> createdCustomerDetailsIds = new ArrayList<>();
    private CustomerDetailsResponse lastCustomerDetailsResponse;
    private String customerId;

    @Override
    public void clear() {
        createdCustomerDetailsId = null;
        createdCustomerDetailsIds.clear();
        lastResponse = null;
        lastCustomerDetailsResponse = null;
        customerId = null;
    }

    public String getCreatedCustomerDetailsId() {
        return createdCustomerDetailsId;
    }

    public void setCreatedCustomerDetailsId(String id) {
        this.createdCustomerDetailsId = id;
    }

    public List<String> getCreatedCustomerDetailsIds() {
        return createdCustomerDetailsIds;
    }

    public void addCreatedCustomerDetailsId(String id) {
        this.createdCustomerDetailsIds.add(id);
    }

    public CustomerDetailsResponse getLastCustomerDetailsResponse() {
        return lastCustomerDetailsResponse;
    }

    public void setLastCustomerDetailsResponse(CustomerDetailsResponse response) {
        this.lastCustomerDetailsResponse = response;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
