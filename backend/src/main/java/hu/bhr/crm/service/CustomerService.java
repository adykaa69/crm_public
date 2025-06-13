package hu.bhr.crm.service;

import hu.bhr.crm.exception.CustomerNotFoundException;
import hu.bhr.crm.mapper.CustomerMapper;
import hu.bhr.crm.model.Customer;
import hu.bhr.crm.model.Residence;
import hu.bhr.crm.repository.CustomerRepository;
import hu.bhr.crm.repository.entity.CustomerEntity;
import hu.bhr.crm.repository.entity.ResidenceEntity;
import hu.bhr.crm.validation.EmailValidation;
import hu.bhr.crm.validation.FieldValidation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CustomerService {

    private final CustomerRepository repository;
    private final CustomerDetailsService customerDetailsService;
    private final CustomerMapper customerMapper;

    public CustomerService(CustomerRepository repository, CustomerDetailsService customerDetailsService, CustomerMapper customerMapper) {
        this.repository = repository;
        this.customerDetailsService = customerDetailsService;
        this.customerMapper = customerMapper;
    }

    /**
     * Gets one customer by their unique ID.
     * Responds with 200 OK if the customer is found.
     *
     * @param id the unique ID of the requested customer
     * @return a {@link Customer} object corresponding to the given ID
     * @throws CustomerNotFoundException if the customer with the given ID does not exist (returns HTTP 404 Not Found)
     */
    public Customer getCustomerById(UUID id) {
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
     * Creates a new customer along with their residence and stores it in the database.
     * Responds with 201 Created if the customer is successfully created.
     *
     * @param customer the built Customer containing the new customer details,
     *                 including their residence information
     * @return the created {@link Customer} object
     * @throws hu.bhr.crm.exception.MissingFieldException if neither first name nor nickname is set, or if relationship is missing
     * @throws hu.bhr.crm.exception.InvalidEmailException if the given email is invalid
     */
    public Customer registerCustomer(Customer customer) {
        validateFields(customer);
        CustomerEntity customerEntity = customerMapper.customerToCustomerEntity(customer);
        CustomerEntity savedCustomerEntity = repository.save(customerEntity);

        return customerMapper.customerEntityToCustomer(savedCustomerEntity);
    }

    /**
     * Deletes one customer from the database by their unique ID.
     * Responds with 200 OK if the customer is successfully deleted.
     *
     * @param id the unique ID of the requested customer
     * @return the deleted {@link Customer} object
     * @throws CustomerNotFoundException if the customer with the given ID does not exist (returns HTTP 404 Not Found)
     */
    public Customer deleteCustomer(UUID id) {
        CustomerEntity customerEntity = repository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        Customer deletedCustomer = customerMapper.customerEntityToCustomer(customerEntity);
        repository.deleteById(id);

        // Delete all documents related to the customer
        customerDetailsService.deleteCustomerDetailsByCustomerId(id);

        return deletedCustomer;
    }

    /**
     * Updates a customer in the database by their unique ID.
     * Responds with 200 OK if the customer is successfully updated.
     *
     * @param customerPayload the mapped Customer containing the updated customer details
     * @return the updated {@link Customer} object
     * @throws CustomerNotFoundException                  if the customer with the given ID does not exist (returns HTTP 404 Not Found)
     * @throws hu.bhr.crm.exception.MissingFieldException if neither first name nor nickname is set, or if relationship is missing
     * @throws hu.bhr.crm.exception.InvalidEmailException if the given email is invalid
     */
    public Customer updateCustomer(Customer customerPayload) {
        CustomerEntity customerEntity = repository.findById(customerPayload.id())
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        validateFields(customerPayload);
        Customer updatedCustomer = mergeResidence(customerEntity.getResidence(), customerPayload);
        CustomerEntity savedCustomerEntity = repository.save(customerMapper.customerToCustomerEntity(updatedCustomer));

        return customerMapper.customerEntityToCustomer(savedCustomerEntity);
    }

    private Customer mergeResidence(ResidenceEntity existingResidence, Customer customerPayload) {
        Residence updatedResidence = customerPayload.residence();

        if (updatedResidence == null) {
            return customerPayload;
        }

        if (existingResidence != null) {
            updatedResidence = updatedResidence.withId(existingResidence.getId());
        } else {
            updatedResidence = updatedResidence.withId(UUID.randomUUID());
        }

        return customerPayload.withResidence(updatedResidence);
    }

    private void validateFields(Customer customer) {
        FieldValidation.validateAtLeastOneIsNotEmpty(customer.firstName(), "First Name", customer.nickname(), "Nickname");
        FieldValidation.validateNotEmpty(customer.relationship(), "Relationship");
        EmailValidation.validate(customer.email());
    }

}