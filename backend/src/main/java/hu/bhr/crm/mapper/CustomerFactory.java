package hu.bhr.crm.mapper;

import hu.bhr.crm.controller.dto.CustomerRequest;
import hu.bhr.crm.model.Customer;
import hu.bhr.crm.model.Residence;

import java.util.UUID;

public class CustomerFactory {

    /**
     * Builds a Customer from a CustomerRequest.
     *
     * @param customerRequest the data transfer object containing the new customer details
     * @return one built customer in a {@link Customer}
     */
    public static Customer createCustomer(CustomerRequest customerRequest) {
        Residence residence = null;
        if (customerRequest.residence() != null) {
            residence = ResidenceFactory.createResidence(customerRequest.residence());
        }

        return Customer.builder()
                .id(UUID.randomUUID())
                .firstName(customerRequest.firstName())
                .lastName(customerRequest.lastName())
                .nickname(customerRequest.nickname())
                .email(customerRequest.email())
                .phoneNumber(customerRequest.phoneNumber())
                .relationship((customerRequest.relationship()))
                .residence(residence)
                .build();
    }
}
