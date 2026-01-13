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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
@Slf4j
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerDetailsController implements CustomerDetailsControllerApi {

    private final CustomerDetailsService service;
    private final CustomerDetailsMapper mapper;

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

        return new PlatformResponse<>(customerDetailsResponse);
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

        return new PlatformResponse<>(customerDetailsResponses);
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

        return new PlatformResponse<>(customerDetailsResponse);
    }

    /**
     * Permanently deletes a specific detail document.
     * <p>
     * If the operation is successful, no content is returned.
     * </p>
     *
     * @param id the unique UUID of the detail document to delete
     * @throws hu.bhr.crm.exception.CustomerDetailsNotFoundException if the document does not exist (HTTP 404 Not Found)
     */
    @Override
    @CrossOrigin(origins = "http://localhost:5173")
    @DeleteMapping("/details/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomerDetails(@PathVariable UUID id) {
        log.info("Deleting customer details with id: {}", id);
        service.deleteCustomerDetailsById(id);
        log.info("Customer details with id {} deleted successfully", id);
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
    @CrossOrigin(origins = "http://localhost:5173")
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

        return new PlatformResponse<>(customerDetailsResponse);
    }
}
