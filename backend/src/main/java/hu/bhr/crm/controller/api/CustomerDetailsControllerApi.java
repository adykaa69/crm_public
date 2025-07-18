package hu.bhr.crm.controller.api;

import hu.bhr.crm.controller.dto.CustomerDetailsRequest;
import hu.bhr.crm.controller.dto.CustomerDetailsResponse;
import hu.bhr.crm.controller.dto.PlatformResponse;

import java.util.List;
import java.util.UUID;

public interface CustomerDetailsControllerApi {

    PlatformResponse<CustomerDetailsResponse> getCustomerDetails(UUID id);
    PlatformResponse<List<CustomerDetailsResponse>> getAllCustomerDetails(UUID customerId);
    PlatformResponse<CustomerDetailsResponse> registerCustomerDetails(UUID customerId, CustomerDetailsRequest customerDetailsRequest);
    PlatformResponse<CustomerDetailsResponse> deleteCustomerDetails(UUID id);
    PlatformResponse<CustomerDetailsResponse> updateCustomerDetails(UUID id, CustomerDetailsRequest customerDetailsRequest);

}
