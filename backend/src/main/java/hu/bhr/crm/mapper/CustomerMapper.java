package hu.bhr.crm.mapper;

import hu.bhr.crm.controller.dto.CustomerRequest;
import hu.bhr.crm.controller.dto.CustomerResponse;
import hu.bhr.crm.model.Customer;
import hu.bhr.crm.repository.entity.CustomerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring", uses = ResidenceMapper.class)
public interface CustomerMapper {

    Customer customerEntityToCustomer(CustomerEntity customerEntity);

    CustomerEntity customerToCustomerEntity(Customer customer);

    CustomerResponse customerToCustomerResponse(Customer customer);

    @Mapping(source = "id", target = "id")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Customer customerRequestToCustomer(UUID id,  CustomerRequest customerRequest);
}
