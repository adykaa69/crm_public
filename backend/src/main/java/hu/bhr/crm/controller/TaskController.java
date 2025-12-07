package hu.bhr.crm.controller;

import hu.bhr.crm.controller.api.TaskControllerApi;
import hu.bhr.crm.controller.dto.PlatformResponse;
import hu.bhr.crm.controller.dto.TaskRequest;
import hu.bhr.crm.controller.dto.TaskResponse;
import hu.bhr.crm.mapper.TaskMapper;
import hu.bhr.crm.model.Task;
import hu.bhr.crm.service.TaskService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * REST Controller for managing Task resources.
 * <p>
 * This controller provides endpoints for CRUD operations on Tasks.
 * It handles HTTP request mapping, input validation, and transforms domain models
 * into API responses.
 * </p>
 */
@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController implements TaskControllerApi {

    private final TaskService taskService;
    private final TaskMapper taskMapper;
    private static final Logger log = LoggerFactory.getLogger(TaskController.class);

    public TaskController(TaskService taskService, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    /**
     * Retrieves a specific task by their unique identifier.
     *
     * @param id the UUID of the task to retrieve
     * @return a {@link PlatformResponse} containing the {@link TaskResponse} DTO (HTTP 200 OK)
     * @throws hu.bhr.crm.exception.TaskNotFoundException if the customer does not exist (HTTP 404 Not Found)
     */
    @Override
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PlatformResponse<TaskResponse> getTask(@PathVariable UUID id) {
        log.info("Fetching task with id: {}", id);
        Task task = taskService.getTaskById(id);
        TaskResponse taskResponse = taskMapper.taskToTaskResponse(task);
        log.info("Task with id {} retrieved successfully", id);

        return new PlatformResponse<>("success", "Task retrieved successfully", taskResponse);
    }

    /**
     * Retrieves a list of all tasks.
     *
     * @return a {@link PlatformResponse} containing a {@link List} of {@link TaskResponse} DTOs (HTTP 200 OK)
     */
    @Override
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PlatformResponse<List<TaskResponse>> getAllTasks() {
        log.info("Fetching all tasks");
        List<Task> tasks = taskService.getAllTasks();
        List<TaskResponse> taskResponses = tasks.stream()
                .map(taskMapper::taskToTaskResponse)
                .toList();
        log.info("Retrieved all tasks");

        return new PlatformResponse<>("success", "Tasks retrieved successfully", taskResponses);
    }

    /**
     * Retrieves all tasks associated with a specific customer.
     *
     * @param customerId the unique UUID of the customer
     * @return a {@link PlatformResponse} containing a {@link List} of the customer's tasks (HTTP 200 OK)
     */
    @Override
    @GetMapping("/{customerId}/tasks")
    @ResponseStatus(HttpStatus.OK)
    public PlatformResponse<List<TaskResponse>> getAllTasksByCustomerId(@PathVariable UUID customerId) {
        log.info("Fetching all tasks for customer with id: {}", customerId);
        List<Task> tasks = taskService.getAllTasksByCustomerId(customerId);
        List<TaskResponse> taskResponses = tasks.stream()
                .map(taskMapper::taskToTaskResponse)
                .toList();
        log.info("Retrieved all tasks for customer with id {}", customerId);

        return new PlatformResponse<>("success", "Tasks retrieved successfully", taskResponses);
    }

    /**
     * Creates and persists a new task.
     * <p>
     * Validates the request body. If the task is associated with a customer (via customerId),
     * the existence of the customer is verified.
     * If validation fails, returns HTTP 400 Bad Request.
     * </p>
     *
     * @param taskRequest the DTO containing the new task details
     * @return a {@link PlatformResponse} containing the created task data (HTTP 201 Created)
     * @throws hu.bhr.crm.exception.CustomerNotFoundException if the referenced customerId does not exist (HTTP 404 Not Found)
     */
    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlatformResponse<TaskResponse> registerTask(@RequestBody @Valid TaskRequest taskRequest) {
        Task savedTask = taskService.saveTask(taskRequest);
        TaskResponse taskResponse = taskMapper.taskToTaskResponse(savedTask);

        if (savedTask.customer() == null) {
            log.info("Task with id {} created successfully", savedTask.id());
        } else {
            log.info("Task with id {} created successfully for customer with id {}", savedTask.id(), savedTask.customer().id());
        }

        return new PlatformResponse<>("success", "Task created successfully", taskResponse);
    }

    /**
     * Permanently deletes a task by its unique identifier.
     *
     * @param id the unique UUID of the task to delete
     * @return a {@link PlatformResponse} containing the details of the deleted task (HTTP 200 OK)
     * @throws hu.bhr.crm.exception.TaskNotFoundException if the task does not exist (HTTP 404 Not Found)
     */
    @Override
    @CrossOrigin(origins = "http://localhost:5173")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PlatformResponse<TaskResponse> deleteTask(@PathVariable UUID id) {
        log.info("Deleting task with id: {}", id);
        Task deletedTask = taskService.deleteTask(id);
        TaskResponse taskResponse = taskMapper.taskToTaskResponse(deletedTask);
        log.info("Task with id {} deleted successfully", id);

        return new PlatformResponse<>("success", "Task deleted successfully", taskResponse);
    }

    /**
     * Updates an existing task's details.
     * <p>
     * Validates the request body. If the update includes a customerId reference,
     * it ensures the customer exists. If validation fails, returns HTTP 400 Bad Request.
     * </p>
     *
     * @param id the unique UUID of the task to update
     * @param taskRequest the DTO containing the updated task details
     * @return a {@link PlatformResponse} containing the updated task data (HTTP 200 OK)
     * @throws hu.bhr.crm.exception.TaskNotFoundException if the task does not exist (HTTP 404 Not Found)
     * @throws hu.bhr.crm.exception.CustomerNotFoundException if the referenced customerId does not exist (HTTP 404 Not Found)
     */
    @Override
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PlatformResponse<TaskResponse> updateTask(
            @PathVariable UUID id,
            @RequestBody @Valid TaskRequest taskRequest) {
        log.info("Updating task with id: {}", id);
        Task updatedTask = taskService.updateTask(id, taskRequest);
        TaskResponse taskResponse = taskMapper.taskToTaskResponse(updatedTask);
        log.info("Task with id {} updated successfully", id);

        return new PlatformResponse<>("success", "Task updated successfully", taskResponse);
    }

}
