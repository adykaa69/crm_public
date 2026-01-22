package hu.bhr.crm.controller.api;

import hu.bhr.crm.controller.api.annotation.BadRequestResponse;
import hu.bhr.crm.controller.api.annotation.NotFoundResponse;
import hu.bhr.crm.controller.dto.CustomerDetailsRequest;
import hu.bhr.crm.controller.dto.CustomerDetailsResponse;
import hu.bhr.crm.controller.dto.PlatformResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.UUID;

@Tag(name = "Customer Details (Notes) Management", description = "Operations for managing customer documents")
public interface CustomerDetailsControllerApi {

    @Operation(summary = "Get details by ID", description = "Retrieves a specific customer document by its unique ID.")
    @ApiResponse(responseCode = "200", description = "Customer document retrieved successfully")
    @NotFoundResponse
    PlatformResponse<CustomerDetailsResponse> getCustomerDetails(
            @Parameter(description = "Unique identifier of the customer document", example = "aeee97ed-29d5-47f5-9b25-ef2f49cbf420")
            UUID id
    );

    @Operation(summary = "List all notes for a customer", description = "Retrieves all documents associated with a specific customer.")
    @ApiResponse(responseCode = "200", description = "List of details retrieved successfully")
    PlatformResponse<List<CustomerDetailsResponse>> getAllCustomerDetails(
            @Parameter(description = "Unique identifier of the related customer", example = "2098a60f-8893-4818-8bfb-256c12c0beec")
            UUID customerId
    );

    @Operation(summary = "Add note to customer", description = "Creates a new document for a specific customer.")
    @ApiResponse(responseCode = "201", description = "Note added successfully")
    @NotFoundResponse
    @BadRequestResponse
    PlatformResponse<CustomerDetailsResponse> registerCustomerDetails(
            @Parameter(description = "Unique identifier of the related customer", example = "993f9fac-99c5-4f32-bb4c-625677e7233e")
            UUID customerId,

            @Parameter(description = "Customer document data payload", required = true)
            CustomerDetailsRequest customerDetailsRequest
    );

    @Operation(summary = "Delete a document", description = "Permanently removes a specific document/detail entry.")
    @ApiResponse(responseCode = "200", description = "Document deleted successfully")
    @NotFoundResponse
    void deleteCustomerDetails(
            @Parameter(description = "Unique identifier of the customer document to delete", example = "5f1d7e2c-3b6a-4c8e-9f1e-2d3c4b5a6e7f")
            UUID id
    );

    @Operation(summary = "Update a note", description = "Modifies the content of an existing document.")
    @ApiResponse(responseCode = "200", description = "Document updated successfully")
    @NotFoundResponse
    @BadRequestResponse
    PlatformResponse<CustomerDetailsResponse> updateCustomerDetails(
            @Parameter(description = "Unique identifier of the document to update", example = "c4d5e6f7-8901-2345-6789-0abcdef12345")
            UUID id,

            @Parameter(description = "Updated customer document data payload", required = true)
            CustomerDetailsRequest customerDetailsRequest);

}
