package hu.bhr.crm.controller;

import hu.bhr.crm.controller.dto.CustomerDetailsRequest;
import hu.bhr.crm.controller.dto.CustomerDetailsResponse;
import hu.bhr.crm.controller.dto.PlatformResponse;
import hu.bhr.crm.mapper.CustomerDetailsFactory;
import hu.bhr.crm.mapper.CustomerDetailsMapper;
import hu.bhr.crm.model.CustomerDetails;
import hu.bhr.crm.service.CustomerDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerDetailsController {

    private final CustomerDetailsService service;
    private final CustomerDetailsMapper mapper;
    private static final Logger log = LoggerFactory.getLogger(CustomerDetailsController.class);

    public CustomerDetailsController(CustomerDetailsService service, CustomerDetailsMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    /**
     * Gets customer details by their unique ID.
     * Responds with 200 OK if the customer details are found.
     *
     * @param id the unique ID of the requested customer details
     * @return a {@link PlatformResponse} containing a {@link CustomerDetailsResponse}
     */
    @GetMapping("/details/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PlatformResponse<CustomerDetailsResponse> getCustomerDetails(@PathVariable UUID id) {
        log.debug("Fetching customer details with id: {}", id);
        CustomerDetails customerDetails = service.getCustomerDetailsById(id);
        CustomerDetailsResponse customerDetailsResponse = mapper.customerDetailsToCustomerDetailsResponse(customerDetails);
        log.info("Customer details with id {} retrieved successfully", id);

        return new PlatformResponse<>("success", "Customer details retrieved successfully", customerDetailsResponse);
    }

    /**
     * Gets all customer details for a specific customer by their unique ID.
     * Responds with 200 OK if the customer details are found.
     *
     * @param customerId the unique ID of the customer whose details are requested
     * @return a {@link PlatformResponse} containing a list of {@link CustomerDetailsResponse}
     */
    @GetMapping("/{customerId}/details")
    @ResponseStatus(HttpStatus.OK)
    public PlatformResponse<List<CustomerDetailsResponse>> getAllCustomerDetails(@PathVariable UUID customerId) {
        log.debug("Fetching all customer details for customer with id: {}", customerId);
        List<CustomerDetails> customerDetailsList = service.getAllCustomerDetails(customerId);
        List<CustomerDetailsResponse> customerDetailsResponses = customerDetailsList.stream()
                .map(mapper::customerDetailsToCustomerDetailsResponse)
                .toList();
        log.info("All customer details for customer with id {} retrieved successfully", customerId);

        return new PlatformResponse<>("success", "All customer details retrieved successfully", customerDetailsResponses);
    }

    /**
     * Creates new customer details and stores it in the database.
     * Responds with 201 Created if the customer is successfully created.
     *
     * @param request the data transfer object containing the new customer details
     * @return a {@link PlatformResponse} containing the created {@link CustomerDetailsResponse}
     */
    @PostMapping("/{customerId}/details")
    @ResponseStatus(HttpStatus.CREATED)
    public PlatformResponse<CustomerDetailsResponse> registerCustomerDetails(
            @PathVariable UUID customerId,
            @RequestBody CustomerDetailsRequest request
    ) {
        CustomerDetails customerDetails = CustomerDetailsFactory.createCustomerDetails(customerId, request);
        CustomerDetails savedCustomerDetails = service.saveCustomerDetails(customerDetails);
        CustomerDetailsResponse customerDetailsResponse = mapper.customerDetailsToCustomerDetailsResponse(savedCustomerDetails);
        log.info("Customer details with id {} saved successfully for customer with id {}", savedCustomerDetails.id(), customerId);

        return new PlatformResponse<>("success", "Customer details saved successfully", customerDetailsResponse);
    }

    /**
     * Deletes customer details by their unique ID.
     * Responds with 200 OK if the customer details are successfully deleted.
     *
     * @param id the unique ID of the customer details to be deleted
     * @return a {@link PlatformResponse} containing the deleted {@link CustomerDetailsResponse}
     */
    @DeleteMapping("/details/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PlatformResponse<CustomerDetailsResponse> deleteCustomerDetails(@PathVariable UUID id) {
        log.debug("Deleting customer details with id: {}", id);
        CustomerDetails deletedCustomerDetails = service.deleteCustomerDetailsById(id);
        CustomerDetailsResponse customerDetailsResponse = mapper.customerDetailsToCustomerDetailsResponse(deletedCustomerDetails);
        log.info("Customer details with id {} deleted successfully", id);

        return new PlatformResponse<>("success", "Customer details deleted successfully", customerDetailsResponse);
    }

    /**
     * Updates customer details by their unique ID.
     * Responds with 200 OK if the customer details are successfully updated.
     *
     * @param id the unique ID of the customer details to be updated
     * @param customerDetailsRequest the data transfer object containing the updated customer details
     * @return a {@link PlatformResponse} containing the updated {@link CustomerDetailsResponse}
     */
    @PutMapping("/details/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PlatformResponse<CustomerDetailsResponse> updateCustomerDetails(
            @PathVariable UUID id,
            @RequestBody CustomerDetailsRequest customerDetailsRequest) {

        log.debug("Updating customer details with id: {}", id);
        CustomerDetails customerDetails = mapper.customerDetailsRequestToCustomerDetails(id, customerDetailsRequest);
        CustomerDetails updatedCustomerDetails = service.updateCustomerDetails(customerDetails);
        CustomerDetailsResponse customerDetailsResponse = mapper.customerDetailsToCustomerDetailsResponse(updatedCustomerDetails);
        log.info("Customer details with id {} updated successfully", id);

        return new PlatformResponse<>("success", "Customer details updated successfully", customerDetailsResponse);
    }
}
