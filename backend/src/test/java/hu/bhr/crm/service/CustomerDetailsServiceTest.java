package hu.bhr.crm.service;

import hu.bhr.crm.exception.CustomerDetailsNotFoundException;
import hu.bhr.crm.exception.CustomerNotFoundException;
import hu.bhr.crm.mapper.CustomerDetailsMapper;
import hu.bhr.crm.model.CustomerDetails;
import hu.bhr.crm.repository.mongo.CustomerDocumentRepository;
import hu.bhr.crm.repository.mongo.document.CustomerDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerDetailsServiceTest {

    @Mock
    private CustomerDocumentRepository customerDocumentRepository;
    @Mock
    private CustomerDetailsMapper customerDetailsMapper;
    @Mock
    private CustomerService customerService;
    private CustomerDetailsService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CustomerDetailsService(customerDocumentRepository, customerDetailsMapper, customerService);
    }

    @Nested
    class GetCustomerDetailsByIdTests {

        private UUID customerDetailsId;

        @BeforeEach
        void setUp() {
            customerDetailsId = UUID.randomUUID();
        }

        @Test
        void shouldReturnCustomerDetailsWhenExists() {
            // Given
            CustomerDocument document = new CustomerDocument();
            document.setId(customerDetailsId);
            document.setCustomerId(UUID.randomUUID());
            document.setNote("Important note");

            CustomerDetails expectedDetails = CustomerDetails.builder()
                    .id(customerDetailsId)
                    .customerId(document.getCustomerId())
                    .note("Important note")
                    .build();

            when(customerDocumentRepository.findById(customerDetailsId)).thenReturn(Optional.of(document));
            when(customerDetailsMapper.customerDocumentToCustomerDetails(document)).thenReturn(expectedDetails);

            // When
            CustomerDetails result = underTest.getCustomerDetailsById(customerDetailsId);

            // Then
            assertEquals(expectedDetails, result);
        }

        @Test
        void shouldThrowCustomerDetailsNotFoundExceptionWhenNotExists() {
            // Given
            when(customerDocumentRepository.findById(customerDetailsId)).thenReturn(Optional.empty());

            // When / Then
            assertThrows(CustomerDetailsNotFoundException.class, () -> underTest.getCustomerDetailsById(customerDetailsId));
        }
    }

    @Nested
    class GetAllCustomerDetailsTests {

        private UUID customerId;

        @BeforeEach
        void setUp() {
            customerId = UUID.randomUUID();
        }

        @Test
        void shouldReturnAllCustomerDetailsForCustomer() {
            // Given
            CustomerDocument customerDocument1 = new CustomerDocument();
            customerDocument1.setId(UUID.randomUUID());
            customerDocument1.setCustomerId(customerId);

            CustomerDocument customerDocument2 = new CustomerDocument();
            customerDocument2.setId(UUID.randomUUID());
            customerDocument2.setCustomerId(customerId);

            CustomerDetails customerDetails1 = CustomerDetails.builder()
                    .id(customerDocument1.getId())
                    .customerId(customerId)
                    .note("Note 1")
                    .build();

            CustomerDetails customerDetails2 = CustomerDetails.builder()
                    .id(customerDocument2.getId())
                    .customerId(customerId)
                    .note("Note 2")
                    .build();

            when(customerDocumentRepository.findAllByCustomerId(customerId)).thenReturn(List.of(customerDocument1, customerDocument2));
            when(customerDetailsMapper.customerDocumentToCustomerDetails(customerDocument1)).thenReturn(customerDetails1);
            when(customerDetailsMapper.customerDocumentToCustomerDetails(customerDocument2)).thenReturn(customerDetails2);

            // When
            List<CustomerDetails> result = underTest.getAllCustomerDetails(customerId);

            // Then
            assertEquals(2, result.size());
            assertEquals(List.of(customerDetails1, customerDetails2), result);
        }

        @Test
        void shouldReturnEmptyListWhenNoCustomerDetailsExist() {
            // Given
            when(customerDocumentRepository.findAllByCustomerId(customerId)).thenReturn(List.of());

            // When
            List<CustomerDetails> result = underTest.getAllCustomerDetails(customerId);

            // Then
            assertTrue(result.isEmpty());
        }

        @Test
        void shouldReturnEmptyListWhenCustomerIdDoesNotExist() {
            // Given
            doThrow(new CustomerNotFoundException("Customer not found"))
                    .when(customerService).validateCustomerExists(customerId);

            // When / Then
            assertThrows(CustomerNotFoundException.class, () -> underTest.getAllCustomerDetails(customerId));
        }


    }

    @Nested
    class SaveCustomerDetailsTests {

        private UUID customerId;
        private CustomerDetails inputCustomerDetails;
        private CustomerDocument mappedCustomerDocument;
        private CustomerDocument savedCustomerDocument;
        private CustomerDetails expectedCustomerDetails;

        @BeforeEach
        void setUp() {
            customerId = UUID.randomUUID();
            inputCustomerDetails = CustomerDetails.builder()
                    .id(null)
                    .customerId(customerId)
                    .note("Note")
                    .build();

            mappedCustomerDocument = new CustomerDocument();
            mappedCustomerDocument.setId(UUID.randomUUID());
            mappedCustomerDocument.setCustomerId(customerId);
            mappedCustomerDocument.setNote("Note");

            savedCustomerDocument = new CustomerDocument();
            savedCustomerDocument.setId(mappedCustomerDocument.getId());
            savedCustomerDocument.setCustomerId(customerId);
            savedCustomerDocument.setNote("Note");

            expectedCustomerDetails = CustomerDetails.builder()
                    .id(savedCustomerDocument.getId())
                    .customerId(customerId)
                    .note("Note")
                    .build();
        }

        @Test
        void shouldSaveCustomerDetailsWhenCustomerExists() {
            // Given
            when(customerDetailsMapper.customerDetailsToCustomerDocument(inputCustomerDetails))
                    .thenReturn(mappedCustomerDocument);
            when(customerDocumentRepository.save(mappedCustomerDocument))
                    .thenReturn(savedCustomerDocument);
            when(customerDetailsMapper.customerDocumentToCustomerDetails(savedCustomerDocument))
                    .thenReturn(expectedCustomerDetails);

            // When
            CustomerDetails result = underTest.saveCustomerDetails(inputCustomerDetails);

            // Then
            assertEquals(expectedCustomerDetails, result);

            verify(customerService).validateCustomerExists(customerId);
            verify(customerDetailsMapper).customerDetailsToCustomerDocument(inputCustomerDetails);
            verify(customerDocumentRepository).save(mappedCustomerDocument);
            verify(customerDetailsMapper).customerDocumentToCustomerDetails(savedCustomerDocument);
        }

        @Test
        void shouldThrowCustomerNotFoundExceptionWhenCustomerDoesNotExist() {
            // Given
            doThrow(new CustomerNotFoundException("Customer not found"))
                    .when(customerService).validateCustomerExists(customerId);

            // When / Then
            assertThrows(CustomerNotFoundException.class, () -> underTest.saveCustomerDetails(inputCustomerDetails));

            verify(customerService).validateCustomerExists(customerId);
            verify(customerDetailsMapper, never()).customerDetailsToCustomerDocument(any());
            verify(customerDocumentRepository, never()).save(any());
        }
    }

    @Nested
    class UpdateCustomerDetailsTests {

        private UUID customerId;
        private UUID customerDetailsId;
        private CustomerDetails inputCustomerDetails;
        private CustomerDocument existingCustomerDocument;
        private CustomerDocument updatedCustomerDocument;
        private CustomerDetails expectedCustomerDetails;

        @BeforeEach
        void setUp() {
            customerId = UUID.randomUUID();
            customerDetailsId = UUID.randomUUID();
            inputCustomerDetails = CustomerDetails.builder()
                    .id(customerDetailsId)
                    .customerId(customerId)
                    .note("Updated Note")
                    .build();

            existingCustomerDocument = new CustomerDocument();
            existingCustomerDocument.setId(customerDetailsId);
            existingCustomerDocument.setCustomerId(customerId);
            existingCustomerDocument.setNote("Old Note");

            updatedCustomerDocument = new CustomerDocument();
            updatedCustomerDocument.setId(customerDetailsId);
            updatedCustomerDocument.setCustomerId(customerId);
            updatedCustomerDocument.setNote("Updated Note");

            expectedCustomerDetails = CustomerDetails.builder()
                    .id(customerDetailsId)
                    .customerId(customerId)
                    .note("Updated Note")
                    .build();
        }

        @Test
        void shouldUpdateCustomerDetailsWhenCustomerDetailsAndCustomerExist() {
            // Given
            when(customerDocumentRepository.findById(customerDetailsId))
                    .thenReturn(Optional.of(existingCustomerDocument));
            when(customerDocumentRepository.save(existingCustomerDocument))
                    .thenReturn(updatedCustomerDocument);
            when(customerDetailsMapper.customerDocumentToCustomerDetails(updatedCustomerDocument))
                    .thenReturn(expectedCustomerDetails);

            // When
            CustomerDetails result = underTest.updateCustomerDetails(inputCustomerDetails);

            // Then
            assertEquals(expectedCustomerDetails, result);
            assertEquals("Updated Note", updatedCustomerDocument.getNote());

            verify(customerDocumentRepository).findById(customerDetailsId);
            verify(customerService).validateCustomerExists(customerId);
            verify(customerDocumentRepository).save(existingCustomerDocument);
            verify(customerDetailsMapper).customerDocumentToCustomerDetails(updatedCustomerDocument);
        }

        @Test
        void shouldThrowCustomerDetailsNotFoundExceptionWhenCustomerDetailsDoNotExist() {
            // Given
            when(customerDocumentRepository.findById(customerDetailsId))
                    .thenReturn(Optional.empty());

            // When / Then
            assertThrows(CustomerDetailsNotFoundException.class, () -> underTest.updateCustomerDetails(inputCustomerDetails));

            verify(customerDocumentRepository).findById(customerDetailsId);
            verify(customerService, never()).validateCustomerExists(any());
            verify(customerDocumentRepository, never()).save(any());
        }

        @Test
        void shouldThrowCustomerNotFoundExceptionWhenCustomerDoesNotExist() {
            // Given
            when(customerDocumentRepository.findById(customerDetailsId))
                    .thenReturn(Optional.of(existingCustomerDocument));
            doThrow(new CustomerNotFoundException("Customer not found"))
                    .when(customerService).validateCustomerExists(customerId);

            // When / Then
            assertThrows(CustomerNotFoundException.class, () -> underTest.updateCustomerDetails(inputCustomerDetails));

            verify(customerDocumentRepository).findById(customerDetailsId);
            verify(customerService).validateCustomerExists(customerId);
            verify(customerDocumentRepository, never()).save(any());
        }
    }

    @Nested
    class DeleteCustomerDetailsByIdTests {

        private UUID customerDetailsId;

        @BeforeEach
        void setUp() {
            customerDetailsId = UUID.randomUUID();
        }

        @Test
        void shouldDeleteCustomerDetailsWhenCustomerDetailsExist() {
            // Given
            CustomerDocument existingDocument = new CustomerDocument();
            existingDocument.setId(customerDetailsId);
            existingDocument.setCustomerId(UUID.randomUUID());

            CustomerDetails expectedCustomerDetails = CustomerDetails.builder()
                    .id(customerDetailsId)
                    .customerId(existingDocument.getCustomerId())
                    .note(existingDocument.getNote())
                    .build();

            when(customerDocumentRepository.findById(customerDetailsId)).thenReturn(Optional.of(existingDocument));
            when(customerDetailsMapper.customerDocumentToCustomerDetails(existingDocument))
                    .thenReturn(expectedCustomerDetails);

            // When
            CustomerDetails result = underTest.deleteCustomerDetailsById(customerDetailsId);

            // Then
            assertEquals(expectedCustomerDetails, result);

            verify(customerDocumentRepository).findById(customerDetailsId);
            verify(customerDetailsMapper).customerDocumentToCustomerDetails(existingDocument);
            verify(customerDocumentRepository).delete(existingDocument);
        }

        @Test
        void shouldThrowCustomerDetailsNotFoundExceptionWhenCustomerDetailsDoNotExist() {
            // Given
            when(customerDocumentRepository.findById(customerDetailsId)).thenReturn(Optional.empty());

            // When / Then
            assertThrows(CustomerDetailsNotFoundException.class, () -> underTest.deleteCustomerDetailsById(customerDetailsId));

            verify(customerDocumentRepository).findById(customerDetailsId);
            verify(customerDetailsMapper, never()).customerDocumentToCustomerDetails(any());
            verify(customerDocumentRepository, never()).delete(any());
        }
    }
}