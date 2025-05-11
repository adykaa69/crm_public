package hu.bhr.crm.service;

import hu.bhr.crm.exception.CustomerNotFoundException;
import hu.bhr.crm.mapper.CustomerMapper;
import hu.bhr.crm.model.Customer;
import hu.bhr.crm.repository.CustomerRepository;
import hu.bhr.crm.repository.entity.CustomerEntity;
import hu.bhr.crm.validation.EmailValidation;
import hu.bhr.crm.validation.FieldValidation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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
     * Responds with 200 OK if the customer is found.
     *
     * @param id the unique ID of the requested customer
     * @throws CustomerNotFoundException if the customer with the given ID does not exist (returns HTTP 404 Not Found)
     * @return a {@link Customer} object corresponding to the given ID
     */
    public Customer getCustomerById(UUID id) {

        // Find CustomerEntity by ID
        CustomerEntity customerEntity = repository.findById(id)
                 .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        return customerMapper.customerEntityToCustomer(customerEntity);
    }

    /**
     * Gets all customers.
     * Responds with 200 OK if all customers are found.
     *
     * @return a list of {@link Customer} objects
     */
    public List<Customer> getAllCustomers() {
        return repository.findAll().stream()
                .map(customerMapper::customerEntityToCustomer)
                .toList();
    }

    /**
     * Creates a new customer and stores it in the database.
     * Responds with 201 Created if the customer is successfully created.
     *
     * @param customer the built Customer containing the new customer details
     * @throws hu.bhr.crm.exception.MissingFieldException if neither first name nor nickname is set, or if relationship is missing
     * @throws hu.bhr.crm.exception.InvalidEmailException if the given email is invalid
     * @return the created {@link Customer} object
     */
    public Customer registerCustomer(Customer customer) {

        // Validations
        FieldValidation.validateAtLeastOneIsNotEmpty(customer.firstName(), "First Name", customer.nickname(), "Nickname");
        FieldValidation.validateNotEmpty(customer.relationship(), "Relationship");
        EmailValidation.validate(customer.email());

        // Customer -> CustomerEntity
        CustomerEntity customerEntity = customerMapper.customerToCustomerEntity((customer));

        // Save CustomerEntity to DB
        CustomerEntity savedCustomerEntity = repository.save(customerEntity);

        return customerMapper.customerEntityToCustomer(savedCustomerEntity);
    }

    /**
     * Deletes one customer from the database by their unique ID.
     * Responds with 200 OK if the customer is successfully deleted.
     *
     * @throws CustomerNotFoundException if the customer with the given ID does not exist (returns HTTP 404 Not Found)
     * @param id the unique ID of the requested customer
     * @return the deleted {@link Customer} object
     */
    public Customer deleteCustomer(UUID id) {
        CustomerEntity customerEntity = repository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        Customer deletedCustomer = customerMapper.customerEntityToCustomer(customerEntity);
        repository.deleteById(id);

        return deletedCustomer;
    }

    /**
     * Updates a customer in the database by their unique ID.
     * Responds with 200 OK if the customer is successfully updated.
     *
     * @param customer the mapped Customer containing the updated customer details
     * @throws CustomerNotFoundException if the customer with the given ID does not exist (returns HTTP 404 Not Found)
     * @throws hu.bhr.crm.exception.MissingFieldException if neither first name nor nickname is set, or if relationship is missing
     * @throws hu.bhr.crm.exception.InvalidEmailException if the given email is invalid
     * @return the updated {@link Customer} object
     */
    public Customer updateCustomer(Customer customer) {

        // Find CustomerEntity by ID
        CustomerEntity customerEntity = repository.findById(customer.id())
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        // Validations
        FieldValidation.validateAtLeastOneIsNotEmpty(customer.firstName(), "First Name", customer.nickname(), "Nickname");
        FieldValidation.validateNotEmpty(customer.relationship(), "Relationship");
        EmailValidation.validate(customer.email());

        // Save CustomerEntity to DB
        CustomerEntity savedCustomerEntity = repository.save(customerMapper.customerToCustomerEntity(customer));

        return customerMapper.customerEntityToCustomer(savedCustomerEntity);
    }

}
