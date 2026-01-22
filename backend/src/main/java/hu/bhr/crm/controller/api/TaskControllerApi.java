package hu.bhr.crm.controller.api;

import hu.bhr.crm.controller.api.annotation.BadRequestResponse;
import hu.bhr.crm.controller.api.annotation.InternalErrorResponse;
import hu.bhr.crm.controller.api.annotation.NotFoundResponse;
import hu.bhr.crm.controller.dto.PlatformResponse;
import hu.bhr.crm.controller.dto.TaskRequest;
import hu.bhr.crm.controller.dto.TaskResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.UUID;

@InternalErrorResponse
@Tag(name = "Task Management", description = "Operations for managing tasks, reminders")
public interface TaskControllerApi {

    @Operation(summary = "Get task by ID", description = "Retrieves detailed information about a specific task.")
    @ApiResponse(responseCode = "200", description = "Task retrieved successfully")
    @NotFoundResponse
    PlatformResponse<TaskResponse> getTask(
            @Parameter(description = "Unique identifier of the task", example = "b2c3d4e5-f6a7-8901-2345-678901abcdef")
            UUID id
    );

    @Operation(
            summary = "List all tasks",
            description = "Retrieves a list of all tasks in the system.")
    @ApiResponse(responseCode = "200", description = "List of tasks retrieved successfully")
    PlatformResponse<List<TaskResponse>> getAllTasks();

    @Operation(
            summary = "List tasks by Customer",
            description = "Retrieves all tasks associated with a specific customer.")
    @ApiResponse(responseCode = "200", description = "List of customer tasks retrieved successfully")
    PlatformResponse<List<TaskResponse>> getAllTasksByCustomerId(
            @Parameter(description = "Unique identifier of the customer", example = "a1b2c3d4-e5f6-7890-1234-567890abcdef")
            UUID userId
    );

    @Operation(
            summary = "Create a new task",
            description = """
                Creates a new task. Side effects:
                1. If 'reminder' is set, an email notification is scheduled.
                2. If 'status' is set to 'COMPLETED', the 'completedAt' timestamp is automatically set.
                """
    )
    @ApiResponse(responseCode = "201", description = "Task created successfully")
    @NotFoundResponse
    @BadRequestResponse
    PlatformResponse<TaskResponse> registerTask(
            @Parameter(description = "Task data payload", required = true)
            TaskRequest taskRequest
    );

    @Operation(
            summary = "Delete a task",
            description = "Permanently removes a task. Side effect: Any scheduled email reminder associated with this task is also cancelled.")
    @ApiResponse(responseCode = "200", description = "Task deleted successfully")
    @NotFoundResponse
    void deleteTask(
            @Parameter(description = "Unique identifier of the task to delete", example = "b2c3d4e5-f6a7-8901-2345-678901abcdef")
            UUID id
    );

    @Operation(
            summary = "Update a task",
            description = """
                Updates an existing task.
                Side effects:
                1. If 'reminder' date is changed, the email schedule is updated/created/deleted accordingly.
                2. If 'status' is changed to 'COMPLETED', the 'completedAt' timestamp is updated.
                """
    )
    @ApiResponse(responseCode = "200", description = "Task updated successfully")
    @NotFoundResponse
    @BadRequestResponse
    PlatformResponse<TaskResponse> updateTask(
            @Parameter(description = "Unique identifier of the task to update", example = "b2c3d4e5-f6a7-8901-2345-678901abcdef")
            UUID id,

            @Parameter(description = "Updated task data payload", required = true)
            TaskRequest taskRequest
    );
}
