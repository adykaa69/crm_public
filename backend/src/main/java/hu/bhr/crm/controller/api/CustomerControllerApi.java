package hu.bhr.crm.controller.api;

import hu.bhr.crm.controller.dto.CustomerRequest;
import hu.bhr.crm.controller.dto.CustomerResponse;
import hu.bhr.crm.controller.dto.PlatformResponse;

import java.util.List;
import java.util.UUID;

public interface CustomerControllerApi {

    PlatformResponse<CustomerResponse> getCustomer(UUID id);
    PlatformResponse<List<CustomerResponse>> getAllCustomers();
    PlatformResponse<CustomerResponse> registerCustomer(CustomerRequest customerRequest);
    PlatformResponse<CustomerResponse> deleteCustomer(UUID id);
    PlatformResponse<CustomerResponse> updateCustomer(UUID id, CustomerRequest customerRequest);

}
