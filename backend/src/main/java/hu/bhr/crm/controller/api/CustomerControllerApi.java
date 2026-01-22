package hu.bhr.crm.controller.api;

import hu.bhr.crm.controller.api.annotation.BadRequestResponse;
import hu.bhr.crm.controller.api.annotation.InternalErrorResponse;
import hu.bhr.crm.controller.api.annotation.NotFoundResponse;
import hu.bhr.crm.controller.dto.CustomerRequest;
import hu.bhr.crm.controller.dto.CustomerResponse;
import hu.bhr.crm.controller.dto.PlatformResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.UUID;

@Tag(name = "Customer Management", description = "Operations for managing customers")
@InternalErrorResponse
public interface CustomerControllerApi {

    @Operation(summary = "Get customer by ID", description = "Retrieves detailed information about a specific customer.")
    @ApiResponse(responseCode = "200", description = "Customer retrieved successfully")
    @NotFoundResponse
    PlatformResponse<CustomerResponse> getCustomer(
            @Parameter(description = "Unique identifier of the customer", example = "a1b2c3d4-e5f6-7890-1234-567890abcdef")
            UUID id
    );

    @Operation(summary = "List all customers", description = "Retrieves a list of all registered customers in the system.")
    @ApiResponse(responseCode = "200", description = "List of customers retrieved successfully")
    PlatformResponse<List<CustomerResponse>> getAllCustomers();

    @Operation(summary = "Register a new customer", description = "Creates a new customer entry with the provided details.")
    @ApiResponse(responseCode = "201", description = "Customer created successfully")
    @BadRequestResponse
    PlatformResponse<CustomerResponse> registerCustomer(
            @Parameter(description = "Customer data payload", required = true)
            CustomerRequest customerRequest
    );

    @Operation(
            summary = "Delete a customer",
            description = """
                Permanently removes a customer. Side effects:
                1. Associated Address (Residence) is deleted.
                2. Associated Customer Details are deleted.
                3. Associated Tasks are NOT deleted, but detached from the customer.
                   A system note ("The related customer has been deleted") is appended to the task description.
                """
    )
    @ApiResponse(responseCode = "200", description = "Customer deleted successfully")
    @NotFoundResponse
    void deleteCustomer(
            @Parameter(description = "Unique identifier of the customer to delete", example = "a1b2c3d4-e5f6-7890-1234-567890abcdef")
            UUID id
    );

    @Operation(summary = "Update customer details", description = "Updates existing customer information. Only provided fields will be modified.")
    @ApiResponse(responseCode = "200", description = "Customer updated successfully")
    @NotFoundResponse
    @BadRequestResponse
    PlatformResponse<CustomerResponse> updateCustomer(
            @Parameter(description = "Unique identifier of the customer to update", example = "a1b2c3d4-e5f6-7890-1234-567890abcdef")
            UUID id,

            @Parameter(description = "Updated customer data payload", required = true)
            CustomerRequest customerRequest
    );

}
