package hu.bhr.crm.service;

import hu.bhr.crm.exception.CustomerNotFoundException;
import hu.bhr.crm.mapper.CustomerMapper;
import hu.bhr.crm.model.Customer;
import hu.bhr.crm.model.Residence;
import hu.bhr.crm.repository.CustomerRepository;
import hu.bhr.crm.repository.entity.CustomerEntity;
import hu.bhr.crm.repository.entity.ResidenceEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private CustomerMapper customerMapper;
    private CustomerService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerRepository, customerMapper);
    }

    @Nested
    class GetCustomerByIdTests {

        private UUID customerId;

        @BeforeEach
        void setUp() {
            customerId = UUID.randomUUID();
        }

        @Test
        void shouldReturnCustomerWhenCustomerExists() {
            // Given
            CustomerEntity customerEntity = new CustomerEntity();
            customerEntity.setId(customerId);

            Customer expectedCustomer = Customer.builder()
                    .id(customerId)
                    .firstName("John")
                    .lastName("Doe")
                    .build();

            when(customerRepository.findById(customerId)).thenReturn(Optional.of(customerEntity));
            when(customerMapper.customerEntityToCustomer(customerEntity)).thenReturn(expectedCustomer);

            // When
            Customer result = underTest.getCustomerById(customerId);

            // Then
            assertEquals(expectedCustomer, result);
        }

        @Test
        void shouldThrowCustomerNotFoundExceptionWhenCustomerDoesNotExist() {
            // Given
            when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

            // When / Then
            assertThrows(CustomerNotFoundException.class, () -> underTest.getCustomerById(customerId));
        }
    }

    @Nested
    class GetAllCustomersTests {
        @Test
        void shouldReturnAllCustomers() {
            // Given
            CustomerEntity customerEntity1 = new CustomerEntity();
            customerEntity1.setId(UUID.randomUUID());
            CustomerEntity customerEntity2 = new CustomerEntity();
            customerEntity2.setId(UUID.randomUUID());

            Customer customer1 = Customer.builder()
                    .id(customerEntity1.getId())
                    .firstName("John")
                    .lastName("Doe")
                    .build();

            Customer customer2 = Customer.builder()
                    .id(customerEntity2.getId())
                    .firstName("Harry")
                    .lastName("Potter")
                    .build();

            when(customerRepository.findAll()).thenReturn(List.of(customerEntity1, customerEntity2));
            when(customerMapper.customerEntityToCustomer(customerEntity1)).thenReturn(customer1);
            when(customerMapper.customerEntityToCustomer(customerEntity2)).thenReturn(customer2);

            // When
            List<Customer> result = underTest.getAllCustomers();

            // Then
            assertEquals(2, result.size());
            assertEquals(List.of(customer1, customer2), result);
        }

        @Test
        void shouldReturnEmptyListWhenNoCustomersExist() {
            // Given
            when(customerRepository.findAll()).thenReturn(List.of());

            // When
            List<Customer> result = underTest.getAllCustomers();

            // Then
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    class RegisterCustomerTests {

        private UUID customerId;

        @BeforeEach
        void setUp() {
            customerId = UUID.randomUUID();
        }

        @Test
        void shouldRegisterCustomerWithoutResidence() {
            // Given
            Customer customer = Customer.builder()
                    .id(customerId)
                    .firstName("John")
                    .lastName("Doe")
                    .build();

            CustomerEntity customerEntity = new CustomerEntity();

            when(customerMapper.customerToCustomerEntity(customer)).thenReturn(customerEntity);
            when(customerRepository.save(customerEntity)).thenReturn(customerEntity);
            when(customerMapper.customerEntityToCustomer(customerEntity)).thenReturn(customer);

            // When
            Customer result = underTest.registerCustomer(customer);

            // Then
            assertEquals(customer, result);
            assertEquals("John", result.firstName());
            assertNull(result.residence());
        }

        @Test
        void shouldRegisterCustomerWithResidence() {
            // Given
            UUID residenceId = UUID.randomUUID();

            Customer expectedCustomer = Customer.builder()
                    .id(customerId)
                    .residence(Residence.builder()
                            .id(residenceId)
                            .city("Budapest")
                            .build())
                    .build();

            CustomerEntity customerEntity = new CustomerEntity();

            when(customerMapper.customerToCustomerEntity(expectedCustomer)).thenReturn(customerEntity);
            when(customerRepository.save(customerEntity)).thenReturn(customerEntity);
            when(customerMapper.customerEntityToCustomer(customerEntity)).thenReturn(expectedCustomer);

            // When
            Customer result = underTest.registerCustomer(expectedCustomer);

            // Then
            assertEquals(expectedCustomer, result);
            assertEquals(expectedCustomer.residence(), result.residence());
            assertEquals("Budapest", result.residence().city());
        }

        @Test
        void shouldRegisterCustomerWithEmptyResidencePayload() {
            // Given
            Customer customerPayload = Customer.builder()
                    .id(customerId)
                    .firstName("John")
                    .lastName("Doe")
                    .residence(Residence.builder().build())
                    .build();

            Residence expectedResidence = customerPayload.residence().
                    withId(UUID.randomUUID());

            Customer expectedCustomer = customerPayload.withResidence(expectedResidence);
            CustomerEntity savedCustomerEntity = new CustomerEntity();

            when(customerMapper.customerToCustomerEntity(customerPayload))
                    .thenReturn(savedCustomerEntity);
            when(customerRepository.save(savedCustomerEntity)).thenReturn(savedCustomerEntity);
            when(customerMapper.customerEntityToCustomer(savedCustomerEntity))
                    .thenReturn(expectedCustomer);

            // When
            Customer result = underTest.registerCustomer(customerPayload);

            // Then
            assertEquals(expectedCustomer, result);
            assertNotNull(result.residence());
            assertNotNull(result.residence().id());
            assertNull(result.residence().city());
            assertNull(result.residence().zipCode());
            assertNull(result.residence().streetAddress());
            assertNull(result.residence().addressLine2());
            assertNull(result.residence().country());
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
        void shouldUpdateCustomerWithoutResidence() {
            // Given
            Customer updatedCustomer = Customer.builder()
                    .id(customerId)
                    .firstName("Harry")
                    .lastName("Potter")
                    .build();

            CustomerEntity existingCustomerEntity = new CustomerEntity();
            CustomerEntity updatedCustomerEntity = new CustomerEntity();

            when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomerEntity));
            when(customerMapper.customerToCustomerEntity(updatedCustomer)).thenReturn(updatedCustomerEntity);
            when(customerRepository.save(updatedCustomerEntity)).thenReturn(updatedCustomerEntity);
            when(customerMapper.customerEntityToCustomer(updatedCustomerEntity)).thenReturn(updatedCustomer);

            // When
            Customer result = underTest.updateCustomer(updatedCustomer);

            // Then
            assertEquals(updatedCustomer, result);
            assertEquals("Harry", result.firstName());
        }

        @Test
        void shouldUpdateCustomerWithResidence() {
            // Given
            Customer updatedCustomer = Customer.builder()
                    .id(customerId)
                    .firstName("Harry")
                    .lastName("Potter")
                    .residence(Residence.builder()
                            .city("Hogwarts")
                            .build())
                    .build();

            CustomerEntity existingCustomerEntity = new CustomerEntity();
            CustomerEntity updatedCustomerEntity = new CustomerEntity();

            when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomerEntity));
            when(customerMapper.customerToCustomerEntity(any(Customer.class))).thenReturn(updatedCustomerEntity);
            when(customerRepository.save(updatedCustomerEntity)).thenReturn(updatedCustomerEntity);
            when(customerMapper.customerEntityToCustomer(updatedCustomerEntity)).thenReturn(updatedCustomer);

            // When
            Customer result = underTest.updateCustomer(updatedCustomer);

            // Then
            assertEquals(updatedCustomer, result);
            assertEquals(updatedCustomer.residence(), result.residence());
            assertEquals("Harry", result.firstName());
            assertEquals("Hogwarts", result.residence().city());
        }

        @Test
        void shouldThrowCustomerNotFoundExceptionWhenCustomerDoesNotExist() {
            // Given
            Customer updatedCustomer = Customer.builder()
                    .id(customerId)
                    .firstName("Harry")
                    .lastName("Potter")
                    .build();

            when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

            // When / Then
            assertThrows(CustomerNotFoundException.class, () -> underTest.updateCustomer(updatedCustomer));
            verify(customerRepository, never()).save(any(CustomerEntity.class));
        }

        @Test
        void shouldAddResidenceWhenCustomerHasNoResidenceYet() {
            // Given
            UUID generatedResidenceId = UUID.randomUUID();

            CustomerEntity existingCustomerEntity = new CustomerEntity();

            Residence newResidence = Residence.builder()
                    .city("Hogwarts")
                    .build();

            Customer customerPayload = Customer.builder()
                    .id(customerId)
                    .firstName("Harry")
                    .residence(newResidence)
                    .build();

            CustomerEntity updatedCustomerEntity = new CustomerEntity();

            Customer updatedCustomer = customerPayload.withResidence(
                    newResidence.withId(generatedResidenceId)
            );

            when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomerEntity));
            when(customerMapper.customerToCustomerEntity(any(Customer.class)))
                    .thenReturn(updatedCustomerEntity);
            when(customerRepository.save(updatedCustomerEntity)).thenReturn(updatedCustomerEntity);
            when(customerMapper.customerEntityToCustomer(updatedCustomerEntity))
                    .thenReturn(updatedCustomer);

            // When
            Customer result = underTest.updateCustomer(customerPayload);

            // Then
            assertEquals(updatedCustomer, result);
            assertEquals("Harry", result.firstName());
            assertEquals("Hogwarts", result.residence().city());
            assertNotNull(result.residence().id());
        }

        @Test
        void shouldRemoveResidenceWhenCustomerHadResidenceBeforeButNotProvidedInUpdate() {
            // Given
            CustomerEntity existingCustomerEntity = new CustomerEntity();
            ResidenceEntity residenceEntity = new ResidenceEntity();
            residenceEntity.setId(UUID.randomUUID());
            existingCustomerEntity.setResidence(residenceEntity);

            Customer customerPayload = Customer.builder()
                    .id(customerId)
                    .firstName("Harry")
                    .lastName("Potter")
                    .residence(null)
                    .build();

            Residence updatedResidence = customerPayload.residence();
            Customer expectedUpdatedCustomer = customerPayload.withResidence(updatedResidence);
            CustomerEntity updatedCustomerEntity = new CustomerEntity();

            when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomerEntity));
            when(customerMapper.customerToCustomerEntity(any(Customer.class))).thenReturn(updatedCustomerEntity);
            when(customerRepository.save(updatedCustomerEntity)).thenReturn(updatedCustomerEntity);
            when(customerMapper.customerEntityToCustomer(updatedCustomerEntity)).thenReturn(expectedUpdatedCustomer);

            // When
            Customer result = underTest.updateCustomer(customerPayload);

            // Then
            assertEquals(expectedUpdatedCustomer, result);
            assertNull(result.residence());
        }

        @Test
        void shouldKeepExistingResidenceIdWhenUpdatingResidence() {
            // Given
            UUID existingResidenceId = UUID.randomUUID();

            CustomerEntity existingCustomerEntity = new CustomerEntity();
            ResidenceEntity existingResidenceEntity = new ResidenceEntity();
            existingResidenceEntity.setId(existingResidenceId);
            existingResidenceEntity.setCity("Old City");
            existingCustomerEntity.setResidence(existingResidenceEntity);

            Residence updatedResidence = Residence.builder()
                    .city("New City")
                    .build();

            Customer customerPayload = Customer.builder()
                    .id(customerId)
                    .firstName("Harry")
                    .lastName("Potter")
                    .residence(updatedResidence)
                    .build();

            CustomerEntity updatedCustomerEntity = new CustomerEntity();

            Customer expectedUpdatedCustomer = customerPayload.withResidence(
                    updatedResidence.withId(existingResidenceId)
            );

            when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomerEntity));
            when(customerMapper.customerToCustomerEntity(any(Customer.class))).thenReturn(updatedCustomerEntity);
            when(customerRepository.save(updatedCustomerEntity)).thenReturn(updatedCustomerEntity);
            when(customerMapper.customerEntityToCustomer(updatedCustomerEntity)).thenReturn(expectedUpdatedCustomer);

            // When
            Customer result = underTest.updateCustomer(customerPayload);

            // Then
            assertEquals(expectedUpdatedCustomer, result);
            assertNotNull(result.residence());
            assertEquals(existingResidenceId, result.residence().id());
            assertEquals("New City", result.residence().city());
        }

        @Test
        void shouldUpdateCustomerWithEmptyResidencePayload() {
            // Given
            CustomerEntity existingCustomerEntity = new CustomerEntity();
            existingCustomerEntity.setResidence(null);

            Customer customerPayload = Customer.builder()
                    .id(customerId)
                    .firstName("John")
                    .lastName("Doe")
                    .residence(Residence.builder().build())
                    .build();


            Residence expectedResidence = customerPayload.residence()
                    .withId(UUID.randomUUID());

            Customer expectedCustomer = customerPayload.withResidence(expectedResidence);
            CustomerEntity updatedCustomerEntity = new CustomerEntity();

            when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomerEntity));
            when(customerMapper.customerToCustomerEntity(any(Customer.class))).thenReturn(updatedCustomerEntity);
            when(customerRepository.save(updatedCustomerEntity)).thenReturn(updatedCustomerEntity);
            when(customerMapper.customerEntityToCustomer(updatedCustomerEntity)).thenReturn(expectedCustomer);

            // When
            Customer result = underTest.updateCustomer(customerPayload);

            // Then
            assertNotNull(result.residence());
            assertNotNull(result.residence().id()); // generált ID
            assertNull(result.residence().city());
            assertNull(result.residence().zipCode());
            assertNull(result.residence().streetAddress());
            assertNull(result.residence().addressLine2());
            assertNull(result.residence().country());
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
        void shouldDeleteCustomerWhenCustomerExists() {
            // Given
            Customer customer = Customer.builder()
                    .id(customerId)
                    .firstName("John")
                    .build();

            CustomerEntity customerEntity = new CustomerEntity();

            when(customerRepository.findById(customerId)).thenReturn(Optional.of(customerEntity));
            when(customerMapper.customerEntityToCustomer(customerEntity)).thenReturn(customer);

            // When
            Customer result = underTest.deleteCustomer(customerId);

            // Then
            assertEquals(customer, result);
            verify(customerRepository).deleteById(customerId);
        }

        @Test
        void shouldThrowCustomerNotFoundExceptionWhenCustomerDoesNotExist() {
            // Given
            when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

            // When / Then
            assertThrows(CustomerNotFoundException.class, () -> underTest.deleteCustomer(customerId));
            verify(customerRepository, never()).deleteById(customerId);
        }
    }

    @Nested
    class ValidateCustomerExistsTests {

        private UUID customerId;

        @BeforeEach
        void setUp() {
            customerId = UUID.randomUUID();
        }

        @Test
        void shouldNotThrowWhenCustomerExists() {
            // Given
            when(customerRepository.existsById(customerId)).thenReturn(true);

            // When + Then
            assertDoesNotThrow(() -> underTest.validateCustomerExists(customerId));
            verify(customerRepository).existsById(customerId);
        }

        @Test
        void shouldThrowWhenCustomerDoesNotExist() {
            // Given
            when(customerRepository.existsById(customerId)).thenReturn(false);

            // When + Then
            assertThrows(CustomerNotFoundException.class,
                    () -> underTest.validateCustomerExists(customerId));

            verify(customerRepository).existsById(customerId);
        }
    }
}