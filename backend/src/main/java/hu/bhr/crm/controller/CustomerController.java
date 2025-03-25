package hu.bhr.crm.controller;

import hu.bhr.crm.controller.dto.CustomerRequest;
import hu.bhr.crm.controller.dto.CustomerResponse;
import hu.bhr.crm.controller.dto.PlatformResponse;
import hu.bhr.crm.mapper.CustomerFactory;
import hu.bhr.crm.mapper.CustomerMapper;
import hu.bhr.crm.model.Customer;
import hu.bhr.crm.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerMapper customerMapper;

    public CustomerController(CustomerService customerService, CustomerMapper customerMapper) {
        this.customerService = customerService;
        this.customerMapper = customerMapper;
    }

    /**
     * Gets one customer by their unique ID.
     * Responses with 200 OK if the customer is responded with+-
     *
     * @param id the unique ID of the requested customer
     * @return one customer in a {@link PlatformResponse}
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PlatformResponse<CustomerResponse> getCustomer(@PathVariable String id) {

        Customer customer = customerService.getCustomerById(id);

        CustomerResponse customerResponse = customerMapper.customerToCustomerResponse(customer);

        return new PlatformResponse<>("success", "Customer retrieved successfully", customerResponse);
    }

    /**
     * Creates a new customer and stores it in the database.
     * Responses with 201 Created if the customer is successfully created
     *
     * @param customerRequest the data transfer object containing the new customer details
     * @return the created customer in a {@link PlatformResponse}
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlatformResponse<CustomerResponse> registerCustomer(@RequestBody CustomerRequest customerRequest) {

        Customer customer = CustomerFactory.createCustomer(customerRequest);

        Customer createdCustomer = customerService.registerCustomer(customer);

        CustomerResponse customerResponse = customerMapper.customerToCustomerResponse(createdCustomer);


        return new PlatformResponse<>("success", "Customer created successfully", customerResponse);
    }

}

