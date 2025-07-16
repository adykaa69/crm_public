package hu.bhr.crm.controller;

import hu.bhr.crm.controller.dto.PlatformResponse;
import hu.bhr.crm.controller.dto.TaskRequest;
import hu.bhr.crm.controller.dto.TaskResponse;
import hu.bhr.crm.mapper.TaskMapper;
import hu.bhr.crm.model.Task;
import hu.bhr.crm.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;
    private static final Logger log = LoggerFactory.getLogger(TaskController.class);

    public TaskController(TaskService taskService, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    /**
     * Gets one task by its unique ID.
     * Responds with 200 OK if the task is found.
     *
     * @param id the unique ID of the requested task
     * @return a {@link PlatformResponse} containing a {@link TaskResponse}
     */
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
     * Gets all tasks.
     * Responds with 200 OK if all tasks are found.
     *
     * @return a {@link PlatformResponse} containing a list of {@link TaskResponse}
     */
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
     * Gets all tasks associated with a specific customer by their unique ID.
     * Responds with 200 OK if the tasks are found.
     *
     * @param customerId the unique ID of the customer whose tasks are requested
     * @return a {@link PlatformResponse} containing a list of {@link TaskResponse}
     */
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
     * Creates a new task and stores it in the database.
     * Responds with 201 Created if the task is successfully created.
     *
     * @param taskRequest the data transfer object containing the new task details
     * @return a {@link PlatformResponse} containing a {@link TaskResponse}
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlatformResponse<TaskResponse> registerTask(@RequestBody TaskRequest taskRequest) {
        Task savedTask = taskService.saveTask(taskRequest);
        TaskResponse taskResponse = taskMapper.taskToTaskResponse(savedTask);

        if (savedTask.customer() == null) {
            log.info("Task with id {} created successfully", savedTask.id());
        } else {
            log.info("Task with id {} created successfully for customer with id {}", savedTask.id(), savedTask.customer().id());
        }

        return new PlatformResponse<>("success", "Task created successfully", taskResponse);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PlatformResponse<TaskResponse> deleteTask(@PathVariable UUID id) {
        log.info("Deleting task with id: {}", id);
        Task deletedTask = taskService.deleteTask(id);
        TaskResponse taskResponse = taskMapper.taskToTaskResponse(deletedTask);
        log.info("Task with id {} deleted successfully", id);

        return new PlatformResponse<>("success", "Task deleted successfully", taskResponse);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PlatformResponse<TaskResponse> updateTask(
            @PathVariable UUID id,
            @RequestBody TaskRequest taskRequest) {
        log.info("Updating task with id: {}", id);
        Task updatedTask = taskService.updateTask(id, taskRequest);
        TaskResponse taskResponse = taskMapper.taskToTaskResponse(updatedTask);
        log.info("Task with id {} updated successfully", id);

        return new PlatformResponse<>("success", "Task updated successfully", taskResponse);
    }

}
