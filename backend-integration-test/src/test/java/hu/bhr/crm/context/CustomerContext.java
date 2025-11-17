package hu.bhr.crm.context;

import hu.bhr.crm.step_definition.dto.CustomerResponse;

public class CustomerContext extends BaseContext {

    private String createdCustomerId;
    private CustomerResponse lastCustomerResponse;

    @Override
    public void clear() {
        createdCustomerId = null;
        lastResponse = null;
        lastCustomerResponse = null;
    }

    public String getCreatedCustomerId() {
        return this.createdCustomerId;
    }

    public void setCreatedCustomerId(String createdCustomerId) {
        this.createdCustomerId = createdCustomerId;
    }

    public CustomerResponse getLastCustomerResponse() {
        return this.lastCustomerResponse;
    }

    public void setLastCustomerResponse(CustomerResponse lastCustomerResponse) {
        this.lastCustomerResponse = lastCustomerResponse;
    }
}
