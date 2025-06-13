package hu.bhr.crm.mapper;

import hu.bhr.crm.controller.dto.CustomerDetailsRequest;
import hu.bhr.crm.model.CustomerDetails;

import java.util.UUID;

public class CustomerDetailsFactory {

    public static CustomerDetails createCustomerDetails(UUID customerId, CustomerDetailsRequest customerDetailsRequest) {
        return CustomerDetails.builder()
                .id(UUID.randomUUID())
                .customerId(customerId)
                .note(customerDetailsRequest.note())
                .build();
    }
}
