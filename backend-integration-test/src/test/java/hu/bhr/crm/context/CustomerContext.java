package hu.bhr.crm.context;

import hu.bhr.crm.step_definition.dto.CustomerResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerContext extends BaseContext {

    private String createdCustomerId;
    private CustomerResponse lastCustomerResponse;

    @Override
    public void clear() {
        createdCustomerId = null;
        lastResponse = null;
        lastCustomerResponse = null;
    }
}
