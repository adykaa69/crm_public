package hu.bhr.crm.controller;

import hu.bhr.crm.controller.api.CustomerDetailsControllerApi;
import hu.bhr.crm.controller.dto.CustomerDetailsRequest;
import hu.bhr.crm.controller.dto.CustomerDetailsResponse;
import hu.bhr.crm.controller.dto.PlatformResponse;
import hu.bhr.crm.mapper.CustomerDetailsFactory;
import hu.bhr.crm.mapper.CustomerDetailsMapper;
import hu.bhr.crm.model.CustomerDetails;
import hu.bhr.crm.service.CustomerDetailsService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST Controller for managing supplementary Customer Details.
 * <p>
 * This controller provides endpoints for CRUD operations on Customer Details.
 * It handles HTTP request mapping, input validation, and transforms domain models
 * into API responses.
 * </p>
 */
@RestController
@RequestMapping("/api/v1/customers")
public class CustomerDetailsController implements CustomerDetailsControllerApi {

    private final CustomerDetailsService service;
    private final CustomerDetailsMapper mapper;
    private static final Logger log = LoggerFactory.getLogger(CustomerDetailsController.class);

    public CustomerDetailsController(CustomerDetailsService service, CustomerDetailsMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    /**
     * Retrieves a specific customer detail document by its unique ID.
     *
     * @param id the unique UUID of the detail document to retrieve
     * @return a {@link PlatformResponse} containing the details (HTTP 200 OK)
     * @throws hu.bhr.crm.exception.CustomerDetailsNotFoundException if the document is not found (HTTP 404 Not Found)
     */
    @Override
    @GetMapping("/details/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PlatformResponse<CustomerDetailsResponse> getCustomerDetails(@PathVariable UUID id) {
        log.info("Fetching customer details with id: {}", id);
        CustomerDetails customerDetails = service.getCustomerDetailsById(id);
        CustomerDetailsResponse customerDetailsResponse = mapper.customerDetailsToCustomerDetailsResponse(customerDetails);
        log.info("Customer details with id {} retrieved successfully", id);

        return new PlatformResponse<>("success", "Customer details retrieved successfully", customerDetailsResponse);
    }

    /**
     * Retrieves all detail documents associated with a specific customer.
     *
     * @param customerId the unique UUID of the customer (parent entity)
     * @return a {@link PlatformResponse} containing a {@link List} of details (HTTP 200 OK)
     * @throws hu.bhr.crm.exception.CustomerNotFoundException if the customerId does not exist (HTTP 404 Not Found)
     */
    @Override
    @GetMapping("/{customerId}/details")
    @ResponseStatus(HttpStatus.OK)
    public PlatformResponse<List<CustomerDetailsResponse>> getAllCustomerDetails(@PathVariable UUID customerId) {
        log.info("Fetching all customer details for customer with id: {}", customerId);
        List<CustomerDetails> customerDetailsList = service.getAllCustomerDetails(customerId);
        List<CustomerDetailsResponse> customerDetailsResponses = customerDetailsList.stream()
                .map(mapper::customerDetailsToCustomerDetailsResponse)
                .toList();
        log.info("All customer details for customer with id {} retrieved successfully", customerId);

        return new PlatformResponse<>("success", "All customer details retrieved successfully", customerDetailsResponses);
    }

    /**
     * Creates a new detail document for a specific customer.
     * <p>
     * Validates the request body and ensures the customer exists.
     * If validation fails, returns HTTP 400 Bad Request.
     * </p>
     *
     * @param customerId the UUID of the customer to attach the details to
     * @param request the DTO containing the detail content (e.g., note)
     * @return a {@link PlatformResponse} with the created document (HTTP 201 Created)
     * @throws hu.bhr.crm.exception.CustomerNotFoundException if the customer does not exist (HTTP 404 Not Found)
     */
    @Override
    @PostMapping("/{customerId}/details")
    @ResponseStatus(HttpStatus.CREATED)
    public PlatformResponse<CustomerDetailsResponse> registerCustomerDetails(
            @PathVariable UUID customerId,
            @RequestBody @Valid CustomerDetailsRequest request
    ) {
        CustomerDetails customerDetails = CustomerDetailsFactory.createCustomerDetails(customerId, request);
        CustomerDetails savedCustomerDetails = service.saveCustomerDetails(customerDetails);
        CustomerDetailsResponse customerDetailsResponse = mapper.customerDetailsToCustomerDetailsResponse(savedCustomerDetails);
        log.info("Customer details with id {} saved successfully for customer with id {}", savedCustomerDetails.id(), customerId);

        return new PlatformResponse<>("success", "Customer details saved successfully", customerDetailsResponse);
    }

    /**
     * Permanently deletes a specific detail document.
     *
     * @param id the unique UUID of the detail document to delete
     * @return a {@link PlatformResponse} containing the deleted document data (HTTP 200 OK)
     * @throws hu.bhr.crm.exception.CustomerDetailsNotFoundException if the document does not exist (HTTP 404 Not Found)
     */
    @Override
    @DeleteMapping("/details/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PlatformResponse<CustomerDetailsResponse> deleteCustomerDetails(@PathVariable UUID id) {
        log.info("Deleting customer details with id: {}", id);
        CustomerDetails deletedCustomerDetails = service.deleteCustomerDetailsById(id);
        CustomerDetailsResponse customerDetailsResponse = mapper.customerDetailsToCustomerDetailsResponse(deletedCustomerDetails);
        log.info("Customer details with id {} deleted successfully", id);

        return new PlatformResponse<>("success", "Customer details deleted successfully", customerDetailsResponse);
    }

    /**
     * Updates an existing detail document.
     * <p>
     * Validates the request body. If validation fails, returns HTTP 400 Bad Request.
     * Checks both the existence of the detail document and the associated customer.
     * </p>
     *
     * @param id the unique UUID of the detail document to update
     * @param customerDetailsRequest the DTO containing the updated content
     * @return a {@link PlatformResponse} with the updated document (HTTP 200 OK)
     * @throws hu.bhr.crm.exception.CustomerDetailsNotFoundException if the document is not found (HTTP 404 Not Found)
     * @throws hu.bhr.crm.exception.CustomerNotFoundException if the associated customer is missing (HTTP 404 Not Found)
     */
    @Override
    @PutMapping("/details/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PlatformResponse<CustomerDetailsResponse> updateCustomerDetails(
            @PathVariable UUID id,
            @RequestBody @Valid CustomerDetailsRequest customerDetailsRequest) {

        log.info("Updating customer details with id: {}", id);
        CustomerDetails customerDetails = mapper.customerDetailsRequestToCustomerDetails(id, customerDetailsRequest);
        CustomerDetails updatedCustomerDetails = service.updateCustomerDetails(customerDetails);
        CustomerDetailsResponse customerDetailsResponse = mapper.customerDetailsToCustomerDetailsResponse(updatedCustomerDetails);
        log.info("Customer details with id {} updated successfully", id);

        return new PlatformResponse<>("success", "Customer details updated successfully", customerDetailsResponse);
    }
}
