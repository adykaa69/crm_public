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

@RestController
@RequestMapping("/api/v1/task")
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;
    private static final Logger log = LoggerFactory.getLogger(TaskController.class);

    public TaskController(TaskService taskService, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

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
}
