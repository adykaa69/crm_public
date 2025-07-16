package hu.bhr.crm.service;

import hu.bhr.crm.model.Customer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CustomerServiceFacade {

    private final CustomerService customerService;
    private final CustomerDetailsService customerDetailsService;
    private final TaskService taskService;

    public CustomerServiceFacade(CustomerService customerService,
                                 CustomerDetailsService customerDetailsService,
                                 TaskService taskService) {
        this.customerService = customerService;
        this.customerDetailsService = customerDetailsService;
        this.taskService = taskService;
    }

    public Customer getCustomerById(UUID id) {
        return customerService.getCustomerById(id);
    }

    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    public Customer registerCustomer(Customer customer) {
        return customerService.registerCustomer(customer);
    }

    public Customer deleteCustomer(UUID id) {
        customerDetailsService.deleteCustomerDetailsByCustomerId(id);
        taskService.detachCustomerFromTasks(id);
        return customerService.deleteCustomer(id);
    }

    public Customer updateCustomer(Customer customer) {
        return customerService.updateCustomer(customer);
    }
}
