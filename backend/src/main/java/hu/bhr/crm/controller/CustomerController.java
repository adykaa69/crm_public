package hu.bhr.crm.controller;

import hu.bhr.crm.controller.dto.CustomerRequest;
import hu.bhr.crm.controller.dto.CustomerResponse;
import hu.bhr.crm.controller.dto.PlatformResponse;
import hu.bhr.crm.mapper.CustomerFactory;
import hu.bhr.crm.mapper.CustomerMapper;
import hu.bhr.crm.model.Customer;
import hu.bhr.crm.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerMapper customerMapper;
    private static final Logger log = LoggerFactory.getLogger(CustomerController.class);

    public CustomerController(CustomerService customerService, CustomerMapper customerMapper) {
        this.customerService = customerService;
        this.customerMapper = customerMapper;
    }

    /**
     * Gets one customer by their unique ID.
     * Responds with 200 OK if the customer is found.
     *
     * @param id the unique ID of the requested customer
     * @return a {@link PlatformResponse} containing a {@link CustomerResponse}
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PlatformResponse<CustomerResponse> getCustomer(@PathVariable UUID id) {
        log.debug("Fetching customer with id: {}", id);
        Customer customer = customerService.getCustomerById(id);
        CustomerResponse customerResponse = customerMapper.customerToCustomerResponse(customer);
        log.info("Customer with id {} retrieved successfully", id);

        return new PlatformResponse<>("success", "Customer retrieved successfully", customerResponse);
    }

    /**
     * Gets all customers.
     * Responds with 200 OK if all customers are found.
     *
     * @return a {@link PlatformResponse} containing a list of {@link CustomerResponse}
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PlatformResponse<List<CustomerResponse>> getAllCustomers() {
        log.debug("Fetching all customers");
        List<Customer> customers = customerService.getAllCustomers();

        List<CustomerResponse> customerResponses = customers.stream()
                .map(customerMapper::customerToCustomerResponse)
                .toList();
        log.info("All customers retrieved successfully");

        return new PlatformResponse<>("success", "All customers retrieved successfully", customerResponses);
    }

    /**
     * Creates a new customer and stores it in the database.
     * Responds with 201 Created if the customer is successfully created.
     *
     * @param customerRequest the data transfer object containing the new customer details
     * @return a {@link PlatformResponse} containing the created {@link CustomerResponse}
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlatformResponse<CustomerResponse> registerCustomer(@RequestBody CustomerRequest customerRequest) {
        Customer customer = CustomerFactory.createCustomer(customerRequest);
        Customer createdCustomer = customerService.registerCustomer(customer);
        CustomerResponse customerResponse = customerMapper.customerToCustomerResponse(createdCustomer);
        log.info("Customer created successfully with id: {}", createdCustomer.id());

        return new PlatformResponse<>("success", "Customer created successfully", customerResponse);
    }

    /**
     * Deletes one customer from the database by their unique ID.
     * Responds with 200 OK if the customer is successfully deleted.
     *
     * @param id the unique ID of the requested customer
     * @return a {@link PlatformResponse} containing the deleted {@link CustomerResponse}
     */
    @CrossOrigin(origins = "http://localhost:5173")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PlatformResponse<CustomerResponse> deleteCustomer(@PathVariable UUID id) {
        log.debug("Deleting customer with id: {}", id);
        Customer deletedCustomer = customerService.deleteCustomer(id);
        CustomerResponse customerResponse = customerMapper.customerToCustomerResponse(deletedCustomer);
        log.info("Customer deleted successfully with id: {}", id);

        return new PlatformResponse<>("success", "Customer has been deleted successfully", customerResponse);
    }

    /**
     * Updates a customer in the database by their unique ID.
     * Responds with 200 OK if the customer is successfully updated.
     *
     * @param id the unique ID of the requested customer
     * @param customerRequest the data transfer object containing the updated customer details
     * @return a {@link PlatformResponse} containing the updated {@link CustomerResponse}
     */
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PlatformResponse<CustomerResponse> updateCustomer(
            @PathVariable UUID id,
            @RequestBody CustomerRequest customerRequest) {

        log.debug("Updating customer with id: {}", id);
        Customer customerPayload = customerMapper.customerRequestToCustomer(id, customerRequest);
        Customer updatedCustomer = customerService.updateCustomer(customerPayload);
        CustomerResponse customerResponse = customerMapper.customerToCustomerResponse(updatedCustomer);
        log.info("Customer updated successfully with id: {}", id);

        return new PlatformResponse<>("success", "Customer updated successfully", customerResponse);
    }
}

