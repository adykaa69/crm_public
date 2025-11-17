package hu.bhr.crm.context;

import java.net.http.HttpResponse;

public abstract class BaseContext {

    protected HttpResponse<String> lastResponse;
    protected String customerId;

    public HttpResponse<String> getLastResponse() {
        return lastResponse;
    }

    public void setLastResponse(HttpResponse<String> lastResponse) {
        this.lastResponse = lastResponse;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    protected abstract void clear();
}
