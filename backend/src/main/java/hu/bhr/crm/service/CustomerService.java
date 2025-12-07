package hu.bhr.crm.service;

import hu.bhr.crm.exception.CustomerNotFoundException;
import hu.bhr.crm.mapper.CustomerMapper;
import hu.bhr.crm.model.Customer;
import hu.bhr.crm.model.Residence;
import hu.bhr.crm.repository.CustomerRepository;
import hu.bhr.crm.repository.entity.CustomerEntity;
import hu.bhr.crm.repository.entity.ResidenceEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Service class for managing Customer entities and their associated Residence data.
 * <p>
 * This service handles the lifecycle of tasks, including creation, updates, deletion,
 * and retrieval.
 * It encapsulates the logic for cascading operations to the {@link Residence} entity,
 * ensuring that address details are correctly created, updated,
 * or removed alongside the customer record.
 * </p>
 */
@Service
public class CustomerService {

    private final CustomerRepository repository;
    private final CustomerMapper customerMapper;

    public CustomerService(CustomerRepository repository, CustomerMapper customerMapper) {
        this.repository = repository;
        this.customerMapper = customerMapper;
    }

    /**
     * Retrieves a single customer by their unique identifier.
     *
     * @param id the unique UUID of the customer
     * @return the {@link Customer} domain object
     * @throws CustomerNotFoundException if no customer exists with the given ID
     */
    public Customer getCustomerById(UUID id) {
        CustomerEntity customerEntity = repository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        return customerMapper.customerEntityToCustomer(customerEntity);
    }

    /**
     * Retrieves all customers stored in the database.
     *
     * @return a {@link List} of all {@link Customer} objects
     */
    public List<Customer> getAllCustomers() {
        return repository.findAll().stream()
                .map(customerMapper::customerEntityToCustomer)
                .toList();
    }

    /**
     * Registers a new customer and persists them in the database.
     * <p>
     * If the customer object contains residence details, they are persisted
     * simultaneously via JPA cascading configuration.
     * </p>
     *
     * @param customer the domain object containing new customer details
     * @return the persisted {@link Customer} object with generated IDs
     */
    public Customer registerCustomer(Customer customer) {
        CustomerEntity customerEntity = customerMapper.customerToCustomerEntity(customer);
        CustomerEntity savedCustomerEntity = repository.save(customerEntity);

        return customerMapper.customerEntityToCustomer(savedCustomerEntity);
    }

    /**
     * Deletes a customer by their unique identifier.
     * <p>
     * This operation will cascade to the associated Residence entity (if exists),
     * removing it as well.
     * </p>
     *
     * @param id the unique UUID of the customer to delete
     * @return the {@link Customer} object that was just deleted (for response purposes)
     * @throws CustomerNotFoundException if the customer with the given ID does not exist
     */
    public Customer deleteCustomer(UUID id) {
        CustomerEntity customerEntity = repository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        Customer deletedCustomer = customerMapper.customerEntityToCustomer(customerEntity);
        repository.deleteById(id);

        return deletedCustomer;
    }

    /**
     * Updates an existing customer's information.
     * <p>
     * This method retrieves the current state from the database to ensure data integrity.
     * It specifically addresses the synchronization of the associated {@link Residence} entity
     * by reconciling the incoming data with the existing database record IDs before saving.
     * </p>
     *
     * @param customerPayload the domain object containing updated fields (ID must be present)
     * @return the updated {@link Customer} object
     * @throws CustomerNotFoundException if the customer with the given ID does not exist
     */
    public Customer updateCustomer(Customer customerPayload) {
        CustomerEntity customerEntity = repository.findById(customerPayload.id())
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        Customer updatedCustomer = mergeResidence(customerEntity.getResidence(), customerPayload);
        CustomerEntity savedCustomerEntity = repository.save(customerMapper.customerToCustomerEntity(updatedCustomer));

        return customerMapper.customerEntityToCustomer(savedCustomerEntity);
    }

    /**
     * Synchronizes the ID of the incoming Residence object with the existing database record.
     * <p>
     * This ensures that the JPA provider performs an update on the existing row rather than
     * attempting to insert a new row (which would violate unique constraints) or creating
     * orphan records.
     * </p>
     * <ul>
     * <li>If the payload has no residence (is null), returns the payload as is.</li>
     * <li>If the payload has a residence but an existing residence is found, the existing ID is assigned to the payload residence to force an update.</li>
     * <li>If the payload has a residence and no existing residence is found, a new UUID is generated for creation.</li>
     * </ul>
     *
     * @param existingResidence the residence entity currently stored in the database (can be null)
     * @param customerPayload the incoming data for the update
     * @return a new {@link Customer} object with the residence ID correctly aligned with the database state
     */
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

    /**
     * Validates if a customer exists without retrieving the full entity.
     *
     * @param id the UUID to check
     * @throws CustomerNotFoundException if the customer does not exist
     */
    public void validateCustomerExists(UUID id) {
        if (!repository.existsById(id)) {
            throw new CustomerNotFoundException("Customer not found");
        }
    }
}