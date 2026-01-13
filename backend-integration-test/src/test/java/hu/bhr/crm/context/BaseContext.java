package hu.bhr.crm.context;

import lombok.Getter;
import lombok.Setter;

import java.net.http.HttpResponse;

@Getter
@Setter
public abstract class BaseContext {

    protected HttpResponse<String> lastResponse;
    protected String customerId;

    protected abstract void clear();
}
