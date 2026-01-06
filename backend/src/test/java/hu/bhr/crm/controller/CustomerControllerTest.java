package hu.bhr.crm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.bhr.crm.controller.dto.CustomerRequest;
import hu.bhr.crm.controller.dto.CustomerResponse;
import hu.bhr.crm.controller.dto.ResidenceRequest;
import hu.bhr.crm.controller.dto.ResidenceResponse;
import hu.bhr.crm.exception.CustomerNotFoundException;
import hu.bhr.crm.mapper.CustomerMapper;
import hu.bhr.crm.model.Customer;
import hu.bhr.crm.model.Residence;
import hu.bhr.crm.service.CustomerServiceFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    private static final String TITLE_ERROR_VALIDATION = "Validation error during request processing";
    private static final String TITLE_ERROR_NOT_FOUND = "Error occurred during requesting customer";
    private static final String MESSAGE_ERROR_CUSTOMER_NOT_FOUND = "Customer not found";
    private static final String MESSAGE_ERROR_INVALID_EMAIL = "Invalid email format";
    private static final String MESSAGE_ERROR_RELATIONSHIP_MISSING = "Relationship is required";
    private static final String MESSAGE_ERROR_FIRSTNAME_NICKNAME_MISSING = "At least one of First Name or Nickname is required";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CustomerServiceFacade customerServiceFacade;

    @MockitoBean
    private CustomerMapper customerMapper;

    @Nested
    class GetCustomerByIdTests {

        private UUID customerId;

        @BeforeEach
        void setUp() {
            customerId = UUID.randomUUID();
        }

        @Test
        void shouldReturnCustomerResponseAndStatusOkWhenCustomerExists() throws Exception {
            // Given
            Customer customer = createCustomer(customerId, "Harry", "Potter");
            CustomerResponse customerResponse = createCustomerResponse(customer);

            when(customerServiceFacade.getCustomerById(customerId))
                    .thenReturn(customer);
            when(customerMapper.customerToCustomerResponse(any(Customer.class)))
                    .thenReturn(customerResponse);

            // When / Then
            mockMvc.perform(get("/api/v1/customers/{id}", customerId)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content.id").value(customerId.toString()))
                    .andExpect(jsonPath("$.content.firstName").value("Harry"))
                    .andExpect(jsonPath("$.content.lastName").value("Potter"));
        }

        @Test
        void shouldThrowCustomerNotFoundExceptionAndReturnStatusNotFoundWhenCustomerDoesNotExist() throws Exception {
            // Given
            when(customerServiceFacade.getCustomerById(customerId))
                    .thenThrow(new CustomerNotFoundException("Customer not found"));

            // When / Then
            mockMvc.perform(get("/api/v1/customers/{id}", customerId)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.title").value(TITLE_ERROR_NOT_FOUND))
                    .andExpect(jsonPath("$.errorMessages[0]").value(MESSAGE_ERROR_CUSTOMER_NOT_FOUND));
        }
    }

    @Nested
    class GetAllCustomersTests {

        @Test
        void shouldReturnAllCustomerResponsesAndStatusOk() throws Exception {
            // Given
            final UUID customerId1 = UUID.randomUUID();
            final UUID customerId2 = UUID.randomUUID();

            Customer customer1 = createCustomer(customerId1, "Harry", "Potter");
            Customer customer2 = createCustomer(customerId2, "Tom", "Riddle");

            CustomerResponse customerResponse1 = createCustomerResponse(customer1);
            CustomerResponse customerResponse2 = createCustomerResponse(customer2);

            when(customerServiceFacade.getAllCustomers()).thenReturn(List.of(customer1, customer2));
            when(customerMapper.customerToCustomerResponse(customer1)).thenReturn(customerResponse1);
            when(customerMapper.customerToCustomerResponse(customer2)).thenReturn(customerResponse2);

            // When / Then
            mockMvc.perform(get("/api/v1/customers")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].id").value(customerId1.toString()))
                    .andExpect(jsonPath("$.content[0].firstName").value("Harry"))
                    .andExpect(jsonPath("$.content[0].lastName").value("Potter"))
                    .andExpect(jsonPath("$.content[1].id").value(customerId2.toString()))
                    .andExpect(jsonPath("$.content[1].firstName").value("Tom"))
                    .andExpect(jsonPath("$.content[1].lastName").value("Riddle"));
        }

        @Test
        void shouldReturnEmptyListAndStatusOkWhenNoCustomersExist() throws Exception {
            // Given
            when(customerServiceFacade.getAllCustomers()).thenReturn(List.of());

            // When / Then
            mockMvc.perform(get("/api/v1/customers")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content").isEmpty());
        }
    }

    @Nested
    class RegisterCustomerTests {

        @Test
        void shouldReturnCustomerResponseAndStatusCreatedWhenCustomerIsSuccessfullyCreated() throws Exception {
            // Given
            final UUID customerId = UUID.randomUUID();
            final UUID residenceId = UUID.randomUUID();

            CustomerRequest customerRequest = new CustomerRequest(
                    "Harry", "Potter", null,
                    "harry.potter@gryffindor.com", null, "gryffindor",
                    new ResidenceRequest(null, null, null, "Hogwarts", "Scotland")
            );

            Customer customer = Customer.builder()
                    .id(customerId)
                    .firstName(customerRequest.firstName())
                    .lastName(customerRequest.lastName())
                    .email(customerRequest.email())
                    .relationship(customerRequest.relationship())
                    .residence(Residence.builder()
                            .id(residenceId)
                            .city(customerRequest.residence().city())
                            .country(customerRequest.residence().country())
                            .build())
                    .build();

            CustomerResponse customerResponse = createCustomerResponse(customer);

            when(customerServiceFacade.registerCustomer(any())).thenReturn(customer);
            when(customerMapper.customerToCustomerResponse(customer)).thenReturn(customerResponse);

            // When / Then
            mockMvc.perform(post("/api/v1/customers")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(customerRequest))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.content.id").value(customerId.toString()))
                    .andExpect(jsonPath("$.content.firstName").value("Harry"))
                    .andExpect(jsonPath("$.content.lastName").value("Potter"))
                    .andExpect(jsonPath("$.content.email").value("harry.potter@gryffindor.com"))
                    .andExpect(jsonPath("$.content.relationship").value("gryffindor"))
                    .andExpect(jsonPath("$.content.residence.id").value(residenceId.toString()))
                    .andExpect(jsonPath("$.content.residence.city").value("Hogwarts"))
                    .andExpect(jsonPath("$.content.residence.country").value("Scotland"));
        }

        @Test
        void shouldReturnStatusBadRequestWhenFirstNameAndNicknameMissing() throws Exception {
            // Given
            CustomerRequest customerRequest = new CustomerRequest(
                    null, "Potter", null,
                    "harry.potter@gryffindor.com", null, "gryffindor", null
            );

            // When / Then
            mockMvc.perform(post("/api/v1/customers")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(customerRequest))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.title").value(TITLE_ERROR_VALIDATION))
                    .andExpect(jsonPath("$.errorMessages[0]").value(MESSAGE_ERROR_FIRSTNAME_NICKNAME_MISSING));
        }

        @Test
        void shouldReturnStatusBadRequestWhenEmailIsInvalid() throws Exception {
            // Given
            CustomerRequest customerRequest = new CustomerRequest(
                    "Harry", "Potter", null,
                    "harry.potter.invalid.email", null, "gryffindor", null
            );

            // When / Then
            mockMvc.perform(post("/api/v1/customers")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(customerRequest))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.title").value(TITLE_ERROR_VALIDATION))
                    .andExpect(jsonPath("$.errorMessages[0]").value(MESSAGE_ERROR_INVALID_EMAIL));
        }

        @Test
        void shouldReturnStatusBadRequestWhenRelationshipIsMissing() throws Exception {
            // Given
            CustomerRequest customerRequest = new CustomerRequest(
                    "Harry", "Potter", null,
                    "harry.potter@gryffindor.com", null, null, null
            );

            // When / Then
            mockMvc.perform(post("/api/v1/customers")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(customerRequest))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.title").value(TITLE_ERROR_VALIDATION))
                    .andExpect(jsonPath("$.errorMessages[0]").value(MESSAGE_ERROR_RELATIONSHIP_MISSING));
        }
    }

    @Nested
    class UpdateCustomerTests {

        private UUID customerId;

        @BeforeEach
        void setUp() {
            customerId = UUID.randomUUID();
        }

        @Test
        void shouldReturnCustomerResponseAndStatusOkWhenCustomerIsSuccessfullyUpdated() throws Exception {
            // Given
            final UUID residenceId = UUID.randomUUID();

            CustomerRequest updateRequest = new CustomerRequest(
                    "Harry", "Potter", null,
                    "harry.potter@hogwarts.com", null, "gryffindor",
                    new ResidenceRequest(null, null, null, "Hogwarts", "Scotland")
            );

            Customer updatedCustomer = Customer.builder()
                    .id(customerId)
                    .firstName(updateRequest.firstName())
                    .lastName(updateRequest.lastName())
                    .email(updateRequest.email())
                    .relationship(updateRequest.relationship())
                    .residence(Residence.builder()
                            .id(residenceId)
                            .city(updateRequest.residence().city())
                            .country(updateRequest.residence().country())
                            .build())
                    .build();

            CustomerResponse customerResponse = createCustomerResponse(updatedCustomer);

            when(customerMapper.customerRequestToCustomer(eq(customerId), any(CustomerRequest.class)))
                    .thenReturn(updatedCustomer);
            when(customerServiceFacade.updateCustomer(any(Customer.class)))
                    .thenReturn(updatedCustomer);
            when(customerMapper.customerToCustomerResponse(any(Customer.class)))
                    .thenReturn(customerResponse);

            // When / Then
            mockMvc.perform(put("/api/v1/customers/{id}", customerId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateRequest))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content.id").value(customerId.toString()))
                    .andExpect(jsonPath("$.content.firstName").value("Harry"))
                    .andExpect(jsonPath("$.content.lastName").value("Potter"))
                    .andExpect(jsonPath("$.content.email").value("harry.potter@hogwarts.com"))
                    .andExpect(jsonPath("$.content.relationship").value("gryffindor"))
                    .andExpect(jsonPath("$.content.residence.id").value(residenceId.toString()))
                    .andExpect(jsonPath("$.content.residence.city").value("Hogwarts"))
                    .andExpect(jsonPath("$.content.residence.country").value("Scotland"));
        }

        @Test
        void shouldThrowCustomerNotFoundExceptionAndReturnStatusNotFoundWhenCustomerDoesNotExist() throws Exception{
            // Given
            CustomerRequest updateRequest = new CustomerRequest(
                    "Harry", "Potter", null,
                    "harry.potter@hogwarts.com", null, "gryffindor", null
            );

            when(customerMapper.customerRequestToCustomer(eq(customerId), any(CustomerRequest.class)))
                    .thenReturn(Customer.builder().id(customerId).build());
            when(customerServiceFacade.updateCustomer(any(Customer.class)))
                    .thenThrow(new CustomerNotFoundException("Customer not found"));

            // When / Then
            mockMvc.perform(put("/api/v1/customers/{id}", customerId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateRequest))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.title").value(TITLE_ERROR_NOT_FOUND))
                    .andExpect(jsonPath("$.errorMessages[0]").value(MESSAGE_ERROR_CUSTOMER_NOT_FOUND));
        }

        @Test
        void shouldReturnStatusBadRequestWhenFirstNameAndNicknameMissing() throws Exception{
            // Given
            CustomerRequest updateRequest = new CustomerRequest(
                    null, "Potter", null,
                    "harry.potter@hogwarts.com", null, "gryffindor", null
            );

            // When / Then
            mockMvc.perform(put("/api/v1/customers/{id}", customerId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateRequest))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.title").value(TITLE_ERROR_VALIDATION))
                    .andExpect(jsonPath("$.errorMessages[0]").value(MESSAGE_ERROR_FIRSTNAME_NICKNAME_MISSING));
        }

        @Test
        void shouldReturnStatusBadRequestWhenEmailIsInvalid() throws Exception {
            // Given
            CustomerRequest updateRequest = new CustomerRequest(
                    "Harry", "Potter", null,
                    "harry.potter.invalid.email", null, "gryffindor", null
            );

            // When / Then
            mockMvc.perform(put("/api/v1/customers/{id}", customerId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateRequest))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.title").value(TITLE_ERROR_VALIDATION))
                    .andExpect(jsonPath("$.errorMessages[0]").value(MESSAGE_ERROR_INVALID_EMAIL));
        }

        @Test
        void shouldReturnStatusBadRequestWhenRelationshipIsMissing() throws Exception {
            // Given
            CustomerRequest updateRequest = new CustomerRequest(
                    "Harry", "Potter", null,
                    "harry.potter@hogwarts.com", null, null, null
            );

            // When / Then
            mockMvc.perform(put("/api/v1/customers/{id}", customerId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateRequest))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.title").value(TITLE_ERROR_VALIDATION))
                    .andExpect(jsonPath("$.errorMessages[0]").value(MESSAGE_ERROR_RELATIONSHIP_MISSING));
        }
    }

    @Nested
    class DeleteCustomerTests {

        private UUID customerId;

        @BeforeEach
        void setUp() {
            customerId = UUID.randomUUID();
        }

        @Test
        void shouldReturnStatusNoContentWhenCustomerIsSuccessfullyDeleted() throws Exception {
            // Given
            doNothing().when(customerServiceFacade).deleteCustomer(customerId);

            // When / Then
            mockMvc.perform(delete("/api/v1/customers/{id}", customerId)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());
        }

        @Test
        void shouldThrowCustomerNotFoundExceptionAndReturnStatusNotFoundWhenCustomerDoesNotExist() throws Exception {
            // Given
            doThrow(new CustomerNotFoundException("Customer not found"))
                    .when(customerServiceFacade).deleteCustomer(customerId);

            // When / Then
            mockMvc.perform(delete("/api/v1/customers/{id}", customerId)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.title").value(TITLE_ERROR_NOT_FOUND))
                    .andExpect(jsonPath("$.errorMessages[0]").value(MESSAGE_ERROR_CUSTOMER_NOT_FOUND));
        }
    }

    private Customer createCustomer(UUID id, String firstName, String lastName) {
        return Customer.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .build();
    }

    private CustomerResponse createCustomerResponse(Customer customer) {
        return new CustomerResponse(
                customer.id(),
                customer.firstName(),
                customer.lastName(),
                customer.nickname(),
                customer.email(),
                customer.phoneNumber(),
                customer.relationship(),
                customer.residence() != null
                        ? new ResidenceResponse(
                        customer.residence().id(),
                        customer.residence().zipCode(),
                        customer.residence().streetAddress(),
                        customer.residence().addressLine2(),
                        customer.residence().city(),
                        customer.residence().country(),
                        null,
                        null)
                        : null,
                null,
                null
        );
    }
}

