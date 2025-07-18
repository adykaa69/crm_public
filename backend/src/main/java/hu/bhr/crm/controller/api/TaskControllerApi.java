package hu.bhr.crm.controller.api;

import hu.bhr.crm.controller.dto.PlatformResponse;
import hu.bhr.crm.controller.dto.TaskRequest;
import hu.bhr.crm.controller.dto.TaskResponse;

import java.util.List;
import java.util.UUID;

public interface TaskControllerApi {

    PlatformResponse<TaskResponse> getTask(UUID id);
    PlatformResponse<List<TaskResponse>> getAllTasks();
    PlatformResponse<List<TaskResponse>> getAllTasksByCustomerId(UUID userId);
    PlatformResponse<TaskResponse> registerTask(TaskRequest taskRequest);
    PlatformResponse<TaskResponse> deleteTask(UUID id);
    PlatformResponse<TaskResponse> updateTask(UUID id, TaskRequest taskRequest);
}
