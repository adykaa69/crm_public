package hu.bhr.crm.context;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GlobalTestContext {

    private final List<BaseContext> contexts;
    private final List<String> createdCustomerIds = new ArrayList<>();

    public GlobalTestContext(CustomerContext customerContext,
                             CustomerDetailsContext customerDetailsContext) {
        this.contexts = List.of(customerDetailsContext, customerContext);
    }

    public HttpResponse<String> getLastResponse() {
        return contexts.stream()
                .map(BaseContext::getLastResponse)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    public List<String> getCreatedCustomerIds() {
        return createdCustomerIds;
    }

    public void addCreatedCustomerId(String customerId) {
        if (customerId != null && !createdCustomerIds.contains(customerId)) {
            createdCustomerIds.add(customerId);
        }
    }

    public void clear() {
        contexts.forEach(BaseContext::clear);
    }
}
