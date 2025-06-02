package hu.bhr.crm.mapper;

import hu.bhr.crm.controller.dto.CustomerRequest;
import hu.bhr.crm.controller.dto.CustomerResponse;
import hu.bhr.crm.controller.dto.ResidenceResponse;
import hu.bhr.crm.model.Customer;
import hu.bhr.crm.repository.entity.CustomerEntity;
import java.sql.Timestamp;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-02T19:19:01+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.2 (Oracle Corporation)"
)
@Component
public class CustomerMapperImpl implements CustomerMapper {

    @Autowired
    private ResidenceMapper residenceMapper;

    @Override
    public Customer customerEntityToCustomer(CustomerEntity customerEntity) {
        if ( customerEntity == null ) {
            return null;
        }

        Customer.CustomerBuilder customer = Customer.builder();

        customer.id( customerEntity.getId() );
        customer.firstName( customerEntity.getFirstName() );
        customer.lastName( customerEntity.getLastName() );
        customer.nickname( customerEntity.getNickname() );
        customer.email( customerEntity.getEmail() );
        customer.phoneNumber( customerEntity.getPhoneNumber() );
        customer.relationship( customerEntity.getRelationship() );
        customer.residence( residenceMapper.residenceEntityToResidence( customerEntity.getResidence() ) );
        customer.createdAt( customerEntity.getCreatedAt() );
        customer.updatedAt( customerEntity.getUpdatedAt() );

        return customer.build();
    }

    @Override
    public CustomerEntity customerToCustomerEntity(Customer customer) {
        if ( customer == null ) {
            return null;
        }

        CustomerEntity customerEntity = new CustomerEntity();

        customerEntity.setId( customer.id() );
        customerEntity.setFirstName( customer.firstName() );
        customerEntity.setLastName( customer.lastName() );
        customerEntity.setNickname( customer.nickname() );
        customerEntity.setEmail( customer.email() );
        customerEntity.setPhoneNumber( customer.phoneNumber() );
        customerEntity.setRelationship( customer.relationship() );
        customerEntity.setResidence( residenceMapper.residenceToResidenceEntity( customer.residence() ) );
        customerEntity.setCreatedAt( customer.createdAt() );
        customerEntity.setUpdatedAt( customer.updatedAt() );

        return customerEntity;
    }

    @Override
    public CustomerResponse customerToCustomerResponse(Customer customer) {
        if ( customer == null ) {
            return null;
        }

        UUID id = null;
        String firstName = null;
        String lastName = null;
        String nickname = null;
        String email = null;
        String phoneNumber = null;
        String relationship = null;
        ResidenceResponse residence = null;
        Timestamp createdAt = null;
        Timestamp updatedAt = null;

        id = customer.id();
        firstName = customer.firstName();
        lastName = customer.lastName();
        nickname = customer.nickname();
        email = customer.email();
        phoneNumber = customer.phoneNumber();
        relationship = customer.relationship();
        residence = residenceMapper.residenceToResidenceResponse( customer.residence() );
        createdAt = customer.createdAt();
        updatedAt = customer.updatedAt();

        CustomerResponse customerResponse = new CustomerResponse( id, firstName, lastName, nickname, email, phoneNumber, relationship, residence, createdAt, updatedAt );

        return customerResponse;
    }

    @Override
    public Customer customerRequestToCustomer(UUID id, CustomerRequest customerRequest) {
        if ( id == null && customerRequest == null ) {
            return null;
        }

        Customer.CustomerBuilder customer = Customer.builder();

        if ( customerRequest != null ) {
            customer.firstName( customerRequest.firstName() );
            customer.lastName( customerRequest.lastName() );
            customer.nickname( customerRequest.nickname() );
            customer.email( customerRequest.email() );
            customer.phoneNumber( customerRequest.phoneNumber() );
            customer.relationship( customerRequest.relationship() );
            customer.residence( residenceMapper.residenceRequestToResidence( customerRequest.residence() ) );
        }
        customer.id( id );

        return customer.build();
    }
}
