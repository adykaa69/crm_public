package hu.bhr.crm.context;

import hu.bhr.crm.step_definition.dto.CustomerDetailsResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
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

    public void addCreatedCustomerDetailsId(String id) {
        this.createdCustomerDetailsIds.add(id);
    }
}
