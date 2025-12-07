package hu.bhr.crm.service;

import hu.bhr.crm.exception.CustomerDetailsNotFoundException;
import hu.bhr.crm.exception.CustomerNotFoundException;
import hu.bhr.crm.mapper.CustomerDetailsMapper;
import hu.bhr.crm.model.CustomerDetails;
import hu.bhr.crm.repository.mongo.CustomerDocumentRepository;
import hu.bhr.crm.repository.mongo.document.CustomerDocument;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Service class for managing additional, potentially unstructured Customer Details.
 * <p>
 * This service manages the lifecycle of supplementary customer information. It acts as a
 * coordinator between the core Customer entity management and the secondary details' storage.
 * It ensures referential integrity by validating the existence of the primary Customer entity
 * before allowing creation or modification of associated detail records.
 * </p>
 */
@Service
public class CustomerDetailsService {

    private final CustomerDocumentRepository customerDocumentRepository;
    private final CustomerDetailsMapper mapper;
    private final CustomerService customerService;

    public CustomerDetailsService(CustomerDocumentRepository customerDocumentRepository, CustomerDetailsMapper mapper, CustomerService customerService) {
        this.customerDocumentRepository = customerDocumentRepository;
        this.mapper = mapper;
        this.customerService = customerService;
    }

    /**
     * Retrieves a specific customer detail document by its unique identifier.
     * <p>
     * Note: The ID refers to the specific detail record, not the Customer's primary ID.
     * </p>
     *
     * @param id the unique UUID of the customer detail document
     * @return the {@link CustomerDetails} domain object
     * @throws CustomerDetailsNotFoundException if the document with the given ID does not exist
     */
    public CustomerDetails getCustomerDetailsById(UUID id) {
        CustomerDocument customerDocument = customerDocumentRepository.findById(id)
                .orElseThrow(() -> new CustomerDetailsNotFoundException("Customer details not found"));

        return mapper.customerDocumentToCustomerDetails(customerDocument);
    }

    /**
     * Retrieves all detail records associated with a specific customer.
     * <p>
     * Before querying the details storage, this method validates that the referenced
     * customer actually exists in the primary database system to ensure data consistency.
     * </p>
     *
     * @param customerId the unique UUID of the customer whose details are requested
     * @return a {@link List} of {@link CustomerDetails} objects belonging to the customer
     * @throws CustomerNotFoundException if the provided customerId does not exist in the primary repository
     */
    public List<CustomerDetails> getAllCustomerDetails(UUID customerId) {
        customerService.validateCustomerExists(customerId);
        return customerDocumentRepository.findAllByCustomerId(customerId).stream()
                .map(mapper::customerDocumentToCustomerDetails)
                .toList();
    }

    /**
     * Persists new customer details.
     * <p>
     * Performs a pre-validation check against the primary customer repository to ensure the
     * referenced customer exists. This prevents the creation of detail records
     * that point to non-existent customers.
     * </p>
     *
     * @param customerDetails the domain object containing the note and the associated customer ID
     * @return the saved {@link CustomerDetails} object with the generated ID
     * @throws CustomerNotFoundException if the referenced customer does not exist in the system
     */
    public CustomerDetails saveCustomerDetails(CustomerDetails customerDetails) {
        customerService.validateCustomerExists(customerDetails.customerId());

        CustomerDocument customerDocument = mapper.customerDetailsToCustomerDocument(customerDetails);
        CustomerDocument savedDocument = customerDocumentRepository.save(customerDocument);

        return mapper.customerDocumentToCustomerDetails(savedDocument);
    }

    /**
     * Deletes a specific customer detail record by its unique ID.
     *
     * @param id the unique UUID of the detail record to delete
     * @return the {@link CustomerDetails} object that was deleted (for confirmation purposes)
     * @throws CustomerDetailsNotFoundException if the record to delete cannot be found
     */
    public CustomerDetails deleteCustomerDetailsById(UUID id) {
        CustomerDocument customerDocument = customerDocumentRepository.findById(id)
                .orElseThrow(() -> new CustomerDetailsNotFoundException("Customer details not found"));

        CustomerDetails deletedCustomerDetails = mapper.customerDocumentToCustomerDetails(customerDocument);
        customerDocumentRepository.delete(customerDocument);
        return deletedCustomerDetails;
    }

    /**
     * Updates an existing customer detail record.
     * <p>
     * This method performs a dual validation:
     * <ul>
     * <li>Checks if the detail record itself exists.</li>
     * <li>Checks if the referenced customer still exists in the primary system.</li>
     * </ul>
     *
     * @param customerDetails the domain object containing the updated information
     * @return the updated {@link CustomerDetails} object
     * @throws CustomerDetailsNotFoundException if the record to update does not exist
     * @throws CustomerNotFoundException if the associated customer does not exist
     */
    public CustomerDetails updateCustomerDetails(CustomerDetails customerDetails) {
        // Check if the customer details exist
        CustomerDocument existingDocument = customerDocumentRepository.findById(customerDetails.id())
                .orElseThrow(() -> new CustomerDetailsNotFoundException("Customer details not found"));

        customerService.validateCustomerExists(existingDocument.getCustomerId());

        // Update the existing document with new values
        existingDocument.setNote(customerDetails.note());

        CustomerDocument updatedDocument = customerDocumentRepository.save(existingDocument);
        return mapper.customerDocumentToCustomerDetails(updatedDocument);
    }

    /**
     * Deletes all detail records related to a specific customer.
     * <p>
     * This is a cleanup operation typically triggered when a Customer is deleted from the
     * primary system, ensuring no related orphan data remains in the secondary storage.
     * </p>
     *
     * @param customerId the unique UUID of the customer whose details should be deleted
     */
    public void deleteCustomerDetailsByCustomerId(UUID customerId) {
        // Delete all documents related a customer
        customerDocumentRepository.deleteAllByCustomerId(customerId);
    }
}
