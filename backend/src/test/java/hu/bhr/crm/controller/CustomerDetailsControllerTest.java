package hu.bhr.crm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.bhr.crm.controller.dto.CustomerDetailsRequest;
import hu.bhr.crm.controller.dto.CustomerDetailsResponse;
import hu.bhr.crm.exception.CustomerDetailsNotFoundException;
import hu.bhr.crm.exception.CustomerNotFoundException;
import hu.bhr.crm.mapper.CustomerDetailsMapper;
import hu.bhr.crm.model.CustomerDetails;
import hu.bhr.crm.service.CustomerDetailsService;
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

import static org.hamcrest.Matchers.hasSize;
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

@WebMvcTest(CustomerDetailsController.class)
class CustomerDetailsControllerTest {

    private static final String MESSAGE_ERROR_NOT_FOUND = "Customer details not found";
    private static final String MESSAGE_ERROR_CUSTOMER_NOT_FOUND = "Customer not found";
    private static final String MESSAGE_ERROR_NOTE_MISSING = "Note is required";
    private static final String TITLE_ERROR_VALIDATION = "Validation error during request processing";
    private static final String TITLE_ERROR_NOT_FOUND = "Error occurred during customer details retrieval";
    private static final String TITLE_ERROR_CUSTOMER_NOT_FOUND = "Error occurred during requesting customer";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CustomerDetailsService customerDetailsService;

    @MockitoBean
    private CustomerDetailsMapper customerDetailsMapper;

    @Nested
    class GetCustomerDetailsTests {

        private UUID customerDetailsId;

        @BeforeEach
        void setUp() {
            customerDetailsId = UUID.randomUUID();
        }

        @Test
        void shouldReturnCustomerDetailsResponseAndStatusOkWhenCustomerDetailsExist() throws Exception {
            // Given
            CustomerDetails customerDetails = createCustomerDetails(customerDetailsId, "Note");
            CustomerDetailsResponse customerDetailsResponse = createCustomerDetailsResponse(customerDetails);

            when(customerDetailsService.getCustomerDetailsById(customerDetailsId))
                    .thenReturn(customerDetails);
            when(customerDetailsMapper.customerDetailsToCustomerDetailsResponse(any(CustomerDetails.class)))
                    .thenReturn(customerDetailsResponse);

            // When & Then
            mockMvc.perform(get("/api/v1/customers/details/{id}", customerDetailsId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content.id").value(customerDetailsId.toString()))
                    .andExpect(jsonPath("$.content.note").value("Note"));
        }

        @Test
        void shouldThrowCustomerDetailsNotFoundExceptionAndReturnStatusNotFoundWhenCustomerDetailsDoNotExist() throws Exception {
            // Given
            when(customerDetailsService.getCustomerDetailsById(customerDetailsId))
                    .thenThrow(new CustomerDetailsNotFoundException("Customer details not found"));

            // When & Then
            mockMvc.perform(get("/api/v1/customers/details/{id}", customerDetailsId))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.title").value(TITLE_ERROR_NOT_FOUND))
                    .andExpect(jsonPath("$.errorMessages[0]").value(MESSAGE_ERROR_NOT_FOUND));
        }
    }

    @Nested
    class GetAllCustomerDetailsByCustomerIdTests {

        private UUID customerId;

        @BeforeEach
        void setUp() {
            customerId = UUID.randomUUID();
        }

        @Test
        void shouldReturnListOfCustomerDetailsAndStatusOkWhenCustomerAndCustomerDetailsExist() throws Exception {
            // Given
            CustomerDetails details1 = createCustomerDetails(UUID.randomUUID(),"Note 1");
            CustomerDetails details2 = createCustomerDetails(UUID.randomUUID(),"Note 2");

            CustomerDetailsResponse customerDetailsResponse1 = createCustomerDetailsResponse(details1);
            CustomerDetailsResponse customerDetailsResponse2 = createCustomerDetailsResponse(details2);

            when(customerDetailsService.getAllCustomerDetails(customerId))
                    .thenReturn(List.of(details1, details2));
            when(customerDetailsMapper.customerDetailsToCustomerDetailsResponse(details1))
                    .thenReturn(customerDetailsResponse1);
            when(customerDetailsMapper.customerDetailsToCustomerDetailsResponse(details2))
                    .thenReturn(customerDetailsResponse2);

            // When & Then
            mockMvc.perform(get("/api/v1/customers/{customerId}/details", customerId)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content", hasSize(2)))
                    .andExpect(jsonPath("$.content[0].note").value("Note 1"))
                    .andExpect(jsonPath("$.content[1].note").value("Note 2"));
        }

        @Test
        void shouldReturnEmptyListAndStatusOkWhenNoCustomerDetailsExist() throws Exception {
            // Given
            when(customerDetailsService.getAllCustomerDetails(customerId))
                    .thenReturn(List.of());

            // When & Then
            mockMvc.perform(get("/api/v1/customers/{customerId}/details", customerId)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content").isEmpty());
        }

        @Test
        void shouldReturnEmptyListAndStatusOkWhenCustomerDoesNotExist() throws Exception {
            // Given
            UUID nonExistentCustomerId = customerId;
            when(customerDetailsService.getAllCustomerDetails(nonExistentCustomerId))
                    .thenReturn(List.of());

            // When & Then
            mockMvc.perform(get("/api/v1/customers/{customerId}/details", nonExistentCustomerId)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content").isEmpty());
        }
    }

    @Nested
    class RegisterCustomerDetailsTests {

        private UUID customerId;

        @BeforeEach
        void setUp() {
            customerId = UUID.randomUUID();
        }

        @Test
        void shouldCreateCustomerDetailsAndReturnStatusCreatedWhenRequestIsValid() throws Exception {
            // Given
            final UUID customerDetailsId = UUID.randomUUID();

            CustomerDetailsRequest customerDetailsRequest = new CustomerDetailsRequest("New Note");
            CustomerDetails customerDetails = createCustomerDetails(customerDetailsId, customerDetailsRequest.note());
            CustomerDetailsResponse customerDetailsResponse = createCustomerDetailsResponse(customerDetails);

            when(customerDetailsService.saveCustomerDetails(any(CustomerDetails.class)))
                    .thenReturn(customerDetails);
            when(customerDetailsMapper.customerDetailsToCustomerDetailsResponse(customerDetails))
                    .thenReturn(customerDetailsResponse);

            // When & Then
            mockMvc.perform(post("/api/v1/customers/{customerId}/details", customerId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(customerDetailsRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.content.id").value(customerDetailsId.toString()))
                    .andExpect(jsonPath("$.content.note").value("New Note"));
        }

        @Test
        void shouldReturnBadRequestWhenNoteIsEmpty() throws Exception {
            // Given
            CustomerDetailsRequest invalidCustomerDetailsRequest = new CustomerDetailsRequest("");

            // When / Then
            mockMvc.perform(post("/api/v1/customers/{customerId}/details", customerId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidCustomerDetailsRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.title").value(TITLE_ERROR_VALIDATION))
                    .andExpect(jsonPath("$.errorMessages[0]").value(MESSAGE_ERROR_NOTE_MISSING));
        }

        @Test
        void shouldThrowCustomerNotFoundExceptionAndReturnNotFoundWhenCustomerDoesNotExist() throws Exception {
            // Given
            UUID nonExistentCustomerId = customerId;
            CustomerDetailsRequest customerDetailsRequest = new CustomerDetailsRequest("Note for non-existent customer");

            when(customerDetailsService.saveCustomerDetails(any(CustomerDetails.class)))
                    .thenThrow(new CustomerNotFoundException("Customer not found"));

            // When / Then
            mockMvc.perform(post("/api/v1/customers/{customerId}/details", nonExistentCustomerId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(customerDetailsRequest)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.title").value(TITLE_ERROR_CUSTOMER_NOT_FOUND))
                    .andExpect(jsonPath("$.errorMessages[0]").value(MESSAGE_ERROR_CUSTOMER_NOT_FOUND));
        }
    }

    @Nested
    class UpdateCustomerDetailsTests {

        private UUID customerDetailsId;

        @BeforeEach
        void setUp() {
            customerDetailsId = UUID.randomUUID();
        }

        @Test
        void shouldReturnUpdatedCustomerDetailsAndStatusOkWhenRequestIsValid() throws Exception {
            // Given
            CustomerDetailsRequest customerDetailsRequest = new CustomerDetailsRequest("Updated Note");
            CustomerDetails updatedCustomerDetails = createCustomerDetails(customerDetailsId, customerDetailsRequest.note());
            CustomerDetailsResponse updatedCustomerDetailsResponse = createCustomerDetailsResponse(updatedCustomerDetails);

            when(customerDetailsMapper.customerDetailsRequestToCustomerDetails(eq(customerDetailsId), any(CustomerDetailsRequest.class)))
                    .thenReturn(updatedCustomerDetails);
            when(customerDetailsService.updateCustomerDetails(any(CustomerDetails.class)))
                    .thenReturn(updatedCustomerDetails);
            when(customerDetailsMapper.customerDetailsToCustomerDetailsResponse(any(CustomerDetails.class)))
                    .thenReturn(updatedCustomerDetailsResponse);

            // When & Then
            mockMvc.perform(put("/api/v1/customers/details/{id}", customerDetailsId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(customerDetailsRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content.id").value(customerDetailsId.toString()))
                    .andExpect(jsonPath("$.content.note").value("Updated Note"));
        }

        @Test
        void shouldReturnBadRequestWhenNoteIsEmpty() throws Exception {
            // Given
            CustomerDetailsRequest invalidCustomerDetailsRequest = new CustomerDetailsRequest("");

            // When / Then
            mockMvc.perform(put("/api/v1/customers/details/{id}", customerDetailsId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidCustomerDetailsRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.title").value(TITLE_ERROR_VALIDATION))
                    .andExpect(jsonPath("$.errorMessages[0]").value(MESSAGE_ERROR_NOTE_MISSING));
        }

        @Test
        void shouldThrowCustomerDetailsNotFoundExceptionAndReturnNotFoundWhenCustomerDetailsDoNotExist() throws Exception {
            // Given
            CustomerDetailsRequest customerDetailsRequest = new CustomerDetailsRequest("Non-existent Note");
            CustomerDetails customerDetails = createCustomerDetails(customerDetailsId, customerDetailsRequest.note());

            when(customerDetailsMapper.customerDetailsRequestToCustomerDetails(eq(customerDetailsId), any(CustomerDetailsRequest.class)))
                    .thenReturn(customerDetails);
            when(customerDetailsService.updateCustomerDetails(any(CustomerDetails.class)))
                    .thenThrow(new CustomerDetailsNotFoundException("Customer details not found"));

            // When / Then
            mockMvc.perform(put("/api/v1/customers/details/{id}", customerDetailsId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(customerDetailsRequest)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.title").value(TITLE_ERROR_NOT_FOUND))
                    .andExpect(jsonPath("$.errorMessages[0]").value(MESSAGE_ERROR_NOT_FOUND));
        }

        @Test
        void shouldThrowCustomerNotFoundExceptionAndReturnNotFoundWhenCustomerDoesNotExist() throws Exception {
            // Given
            CustomerDetailsRequest customerDetailsRequest = new CustomerDetailsRequest("Note for non-existent customer");
            CustomerDetails customerDetails = createCustomerDetails(customerDetailsId, customerDetailsRequest.note());

            when(customerDetailsMapper.customerDetailsRequestToCustomerDetails(eq(customerDetailsId), any(CustomerDetailsRequest.class)))
                    .thenReturn(customerDetails);
            when(customerDetailsService.updateCustomerDetails(any(CustomerDetails.class)))
                    .thenThrow(new CustomerNotFoundException("Customer not found"));

            // When / Then
            mockMvc.perform(put("/api/v1/customers/details/{id}", customerDetailsId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(customerDetailsRequest)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.title").value(TITLE_ERROR_CUSTOMER_NOT_FOUND))
                    .andExpect(jsonPath("$.errorMessages[0]").value(MESSAGE_ERROR_CUSTOMER_NOT_FOUND));
        }
    }

    @Nested
    class DeleteCustomerDetailsTests {

        private UUID customerDetailsId;

        @BeforeEach
        void setUp() {
            customerDetailsId = UUID.randomUUID();
        }

        @Test
        void shouldReturnStatusNoContentWhenCustomerDetailsSuccessfullyDeleted() throws Exception {
            // Given
            doNothing().when(customerDetailsService).deleteCustomerDetailsById(customerDetailsId);

            // When & Then
            mockMvc.perform(delete("/api/v1/customers/details/{id}", customerDetailsId))
                    .andExpect(status().isNoContent());
        }

        @Test
        void shouldThrowCustomerDetailsNotFoundExceptionAndReturnStatusNotFoundWhenCustomerDetailsDoNotExist() throws Exception {
            // Given
            doThrow(new CustomerDetailsNotFoundException("Customer details not found"))
                    .when(customerDetailsService).deleteCustomerDetailsById(customerDetailsId);

            // When & Then
            mockMvc.perform(delete("/api/v1/customers/details/{id}", customerDetailsId))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.title").value(TITLE_ERROR_NOT_FOUND))
                    .andExpect(jsonPath("$.errorMessages[0]").value(MESSAGE_ERROR_NOT_FOUND));
        }
    }

    private CustomerDetails createCustomerDetails(UUID id, String note) {
        return CustomerDetails.builder()
                .id(id)
                .note(note)
                .build();
    }

    private CustomerDetailsResponse createCustomerDetailsResponse(CustomerDetails customerDetails) {
        return new CustomerDetailsResponse(
                customerDetails.id(),
                customerDetails.customerId(),
                customerDetails.note(),
                null,
                null
        );
    }
}