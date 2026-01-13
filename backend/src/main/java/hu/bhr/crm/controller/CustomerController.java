package hu.bhr.crm.controller;

import hu.bhr.crm.controller.api.CustomerControllerApi;
import hu.bhr.crm.controller.dto.CustomerRequest;
import hu.bhr.crm.controller.dto.CustomerResponse;
import hu.bhr.crm.controller.dto.PlatformResponse;
import hu.bhr.crm.mapper.CustomerFactory;
import hu.bhr.crm.mapper.CustomerMapper;
import hu.bhr.crm.model.Customer;
import hu.bhr.crm.service.CustomerServiceFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
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
 * REST Controller for managing Customer resources.
 * <p>
 * This controller provides endpoints for CRUD operations on Customers.
 * It handles HTTP request mapping, input validation, and transforms domain models
 * into API responses.
 * </p>
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController implements CustomerControllerApi {

    private final CustomerServiceFacade customerServiceFacade;
    private final CustomerMapper customerMapper;

    /**
     * Retrieves a specific customer by their unique identifier.
     *
     * @param id the UUID of the customer to retrieve
     * @return a {@link PlatformResponse} containing the {@link CustomerResponse} DTO (HTTP 200 OK)
     * @throws hu.bhr.crm.exception.CustomerNotFoundException if the customer does not exist (HTTP 404 Not Found)
     */
    @Override
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PlatformResponse<CustomerResponse> getCustomer(@PathVariable UUID id) {
        log.info("Fetching customer with id: {}", id);
        Customer customer = customerServiceFacade.getCustomerById(id);
        CustomerResponse customerResponse = customerMapper.customerToCustomerResponse(customer);
        log.info("Customer with id {} retrieved successfully", id);

        return new PlatformResponse<>(customerResponse);
    }

    /**
     * Retrieves a list of all registered customers.
     *
     * @return a {@link PlatformResponse} containing a {@link List} of {@link CustomerResponse} DTOs (HTTP 200 OK)
     */
    @Override
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PlatformResponse<List<CustomerResponse>> getAllCustomers() {
        log.info("Fetching all customers");
        List<Customer> customers = customerServiceFacade.getAllCustomers();

        List<CustomerResponse> customerResponses = customers.stream()
                .map(customerMapper::customerToCustomerResponse)
                .toList();
        log.info("All customers retrieved successfully");

        return new PlatformResponse<>(customerResponses);
    }

    /**
     * Creates and registers a new customer in the system.
     * <p>
     * Validates the request body for required fields and valid email format.
     * If validation fails, the system returns an HTTP 400 Bad Request.
     * </p>
     *
     * @param customerRequest the DTO containing new customer details
     * @return a {@link PlatformResponse} containing the created customer data (HTTP 201 Created)
     */
    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlatformResponse<CustomerResponse> registerCustomer(@RequestBody @Valid CustomerRequest customerRequest) {
        Customer customer = CustomerFactory.createCustomer(customerRequest);
        Customer createdCustomer = customerServiceFacade.registerCustomer(customer);
        CustomerResponse customerResponse = customerMapper.customerToCustomerResponse(createdCustomer);
        log.info("Customer created successfully with id: {}", createdCustomer.id());

        return new PlatformResponse<>(customerResponse);
    }

    /**
     * Permanently deletes a customer by their unique identifier.
     * <p>
     * If the operation is successful, no content is returned.
     * </p>
     *
     * @param id the UUID of the customer to delete
     * @throws hu.bhr.crm.exception.CustomerNotFoundException if the customer does not exist (HTTP 404 Not Found)
     */
    @Override
    @CrossOrigin(origins = "http://localhost:5173")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomer(@PathVariable UUID id) {
        log.info("Deleting customer with id: {}", id);
        customerServiceFacade.deleteCustomer(id);
        log.info("Customer deleted successfully with id: {}", id);
    }

    /**
     * Updates an existing customer's details.
     * <p>
     * Validates the request body. If validation fails, returns HTTP 400.
     * If the customer does not exist, returns HTTP 404.
     * </p>
     *
     * @param id the UUID of the customer to update
     * @param customerRequest the DTO containing updated customer details
     * @return a {@link PlatformResponse} containing the updated customer data (HTTP 200 OK)
     * @throws hu.bhr.crm.exception.CustomerNotFoundException if the customer does not exist (HTTP 404 Not Found)
     */
    @Override
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PlatformResponse<CustomerResponse> updateCustomer(
            @PathVariable UUID id,
            @RequestBody @Valid CustomerRequest customerRequest) {

        log.info("Updating customer with id: {}", id);
        Customer customerPayload = customerMapper.customerRequestToCustomer(id, customerRequest);
        Customer updatedCustomer = customerServiceFacade.updateCustomer(customerPayload);
        CustomerResponse customerResponse = customerMapper.customerToCustomerResponse(updatedCustomer);
        log.info("Customer updated successfully with id: {}", id);

        return new PlatformResponse<>(customerResponse);
    }
}

