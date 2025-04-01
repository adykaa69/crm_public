package hu.bhr.crm.service;

import hu.bhr.crm.exception.CustomerNotFoundException;
import hu.bhr.crm.mapper.CustomerMapper;
import hu.bhr.crm.model.Customer;
import hu.bhr.crm.repository.CustomerRepository;
import hu.bhr.crm.repository.entity.CustomerEntity;
import hu.bhr.crm.validation.EmailValidation;
import hu.bhr.crm.validation.FieldValidation;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepository repository;
    private final CustomerMapper customerMapper;

    public CustomerService(CustomerRepository repository, CustomerMapper customerMapper) {
        this.repository = repository;
        this.customerMapper = customerMapper;
    }

    /**
     * Gets one customer by their unique ID.
     * Responses with 200 OK if the customer is responded with
     *
     * @param id the unique ID of the requested customer
     * @throws CustomerNotFoundException if the customer with the given ID does not exist (returns HTTP 404 Not Found)
     * @return one customer in a {@link Customer}
     */
    public Customer getCustomerById(String id) {

        // Find CustomerEntity by ID
        CustomerEntity customerEntity = repository.findById(id)
                 .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        return customerMapper.customerEntityToCustomer(customerEntity);
    }

    /**
     * Creates a new customer and stores it in the database.
     * Responses with 201 Created if the customer is successfully created.
     *
     * @param customer the built Customer containing the new customer details
     * @throws hu.bhr.crm.exception.InvalidEmailException if the given email is invalid
     * @throws hu.bhr.crm.exception.MissingFieldException if the relationship is not set
     * @return the created customer in a {@link Customer}
     */
    public Customer registerCustomer(Customer customer) {

        FieldValidation.validateNotEmpty(customer.relationship(), "Relationship");
        EmailValidation.validate(customer.email());

        // Customer -> CustomerEntity
        CustomerEntity customerEntity = customerMapper.customerToCustomerEntity((customer));

        // Save CustomerEntity to DB
        CustomerEntity savedCustomerEntity = repository.save(customerEntity);

        return customerMapper.customerEntityToCustomer(savedCustomerEntity);
    }
}
