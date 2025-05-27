package hu.bhr.crm.mapper;

import hu.bhr.crm.controller.dto.CustomerRequest;
import hu.bhr.crm.controller.dto.CustomerResponse;
import hu.bhr.crm.controller.dto.ResidenceResponse;
import hu.bhr.crm.model.Customer;
import hu.bhr.crm.model.Residence;
import hu.bhr.crm.repository.entity.CustomerEntity;
import hu.bhr.crm.repository.entity.ResidenceEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CustomerMapper {

    private final ResidenceMapper residenceMapper;

    public CustomerMapper(ResidenceMapper residenceMapper) {
        this.residenceMapper = residenceMapper;
    }

    // Entity -> Response DTO
    public Customer customerEntityToCustomer(CustomerEntity customerEntity) {

        Residence residence = residenceMapper.residenceEntityToResidence(customerEntity.getResidence());

        return new Customer(
                customerEntity.getId(),
                customerEntity.getFirstName(),
                customerEntity.getLastName(),
                customerEntity.getNickname(),
                customerEntity.getEmail(),
                customerEntity.getPhoneNumber(),
                customerEntity.getRelationship(),
                residence,
                customerEntity.getCreatedAt(),
                customerEntity.getUpdatedAt()
        );
    }

    // Customer -> Entity
    public CustomerEntity customerToCustomerEntity(Customer customer) {
        CustomerEntity customerEntity = new CustomerEntity();

        customerEntity.setId(customer.id());
        customerEntity.setFirstName(customer.firstName());
        customerEntity.setLastName(customer.lastName());
        customerEntity.setNickname(customer.nickname());
        customerEntity.setEmail(customer.email());
        customerEntity.setPhoneNumber(customer.phoneNumber());
        customerEntity.setRelationship(customer.relationship());

        ResidenceEntity residenceEntity = residenceMapper.residenceToResidenceEntity(customer.residence());
        if (residenceEntity != null) {
            customerEntity.setResidence(residenceEntity);
        }

        return customerEntity;
    }

    public CustomerResponse customerToCustomerResponse(Customer customer) {
        ResidenceResponse residenceResponse = null;
        if (customer.residence() != null) {
            residenceResponse = residenceMapper.residenceToResidenceResponse(customer.residence());
        }

        return new CustomerResponse(
                customer.id(),
                customer.firstName(),
                customer.lastName(),
                customer.nickname(),
                customer.email(),
                customer.phoneNumber(),
                customer.relationship(),
                residenceResponse,
                customer.createdAt(),
                customer.updatedAt()
        );
    }

    public Customer customerRequestToCustomer(UUID id, CustomerRequest customerRequest) {
        return new Customer(
                id,
                customerRequest.firstName(),
                customerRequest.lastName(),
                customerRequest.nickname(),
                customerRequest.email(),
                customerRequest.phoneNumber(),
                customerRequest.relationship(),
                residenceMapper.residenceRequestToResidence(customerRequest.residence()),
                null,
                null
        );
    }
}
