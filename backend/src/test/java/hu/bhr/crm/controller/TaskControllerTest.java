package hu.bhr.crm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.bhr.crm.controller.dto.TaskRequest;
import hu.bhr.crm.controller.dto.TaskResponse;
import hu.bhr.crm.exception.TaskNotFoundException;
import hu.bhr.crm.mapper.TaskMapper;
import hu.bhr.crm.model.Task;
import hu.bhr.crm.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    private static final String MESSAGE_SUCCESS_GET = "Task retrieved successfully";
    private static final String MESSAGE_SUCCESS_GET_ALL = "Tasks retrieved successfully";
    private static final String MESSAGE_SUCCESS_CREATE = "Task created successfully";
    private static final String MESSAGE_SUCCESS_UPDATE = "Task updated successfully";
    private static final String MESSAGE_SUCCESS_DELETE = "Task deleted successfully";
    private static final String MESSAGE_ERROR_VALIDATION = "Validation error during request processing";
    private static final String MESSAGE_ERROR_NOT_FOUND = "Error occurred during task retrieval";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TaskService taskService;

    @MockitoBean
    private TaskMapper taskMapper;

    @Nested
    class GetTaskByIdTests {

        private UUID taskId;

        @BeforeEach
        void setUp() {
            taskId = UUID.randomUUID();
        }

        @Test
        void shouldReturnTaskResponseAndStatusOkWhenTaskExists() throws Exception {
            // Given
            Task task = createTask(taskId, "Test Task");
            TaskResponse taskResponse = createTaskResponse(task);

            when(taskService.getTaskById(taskId))
                    .thenReturn(task);
            when(taskMapper.taskToTaskResponse(any(Task.class)))
                    .thenReturn(taskResponse);

            // When / Then
            mockMvc.perform(get("/api/v1/tasks/{id}", taskId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.message").value(MESSAGE_SUCCESS_GET))
                    .andExpect(jsonPath("$.data.id").value(taskId.toString()))
                    .andExpect(jsonPath("$.data.title").value("Test Task"));
        }

        @Test
        void shouldThrowTaskNotFoundExceptionAndReturnStatusNotFoundWhenTaskDoesNotExist() throws Exception {
            // Given
            when(taskService.getTaskById(taskId))
                    .thenThrow(new TaskNotFoundException("Task not found"));

            // When / Then
            mockMvc.perform(get("/api/v1/tasks/{id}", taskId))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.message").value(MESSAGE_ERROR_NOT_FOUND));
        }
    }

    @Nested
    class GetAllTasksTests {

        @Test
        void shouldReturnListOfTasksAndStatusOkWhenTasksExist() throws Exception {
            // Given
            Task task1 = createTask(UUID.randomUUID(), "Task 1");
            Task task2 = createTask(UUID.randomUUID(), "Task 2");

            TaskResponse taskResponse1 = createTaskResponse(task1);
            TaskResponse taskResponse2 = createTaskResponse(task2);

            when(taskService.getAllTasks()).thenReturn(List.of(task1, task2));
            when(taskMapper.taskToTaskResponse(task1)).thenReturn(taskResponse1);
            when(taskMapper.taskToTaskResponse(task2)).thenReturn(taskResponse2);

            // When / Then
            mockMvc.perform(get("/api/v1/tasks")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.message").value(MESSAGE_SUCCESS_GET_ALL))
                    .andExpect(jsonPath("$.data", hasSize(2)))
                    .andExpect(jsonPath("$.data[0].title").value("Task 1"))
                    .andExpect(jsonPath("$.data[1].title").value("Task 2"));
        }

        @Test
        void shouldReturnEmptyListAndStatusOkWhenNoTasksExist() throws Exception {
            // Given
            when(taskService.getAllTasks()).thenReturn(List.of());

            // When / Then
            mockMvc.perform(get("/api/v1/tasks")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.message").value(MESSAGE_SUCCESS_GET_ALL))
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data").isEmpty());
        }
    }

    @Nested
    class GetAllTasksByCustomerIdTests {

        private final UUID customerId = UUID.randomUUID();

        @Test
        void shouldReturnListOfTasksAndStatusOkWhenCustomerAndTasksExist() throws Exception {
            // Given
            Task task1 = createTask(UUID.randomUUID(), "Task 1");
            Task task2 = createTask(UUID.randomUUID(), "Task 2");

            TaskResponse taskResponse1 = createTaskResponse(task1);
            TaskResponse taskResponse2 = createTaskResponse(task2);

            when(taskService.getAllTasksByCustomerId(customerId)).thenReturn(List.of(task1, task2));
            when(taskMapper.taskToTaskResponse(task1)).thenReturn(taskResponse1);
            when(taskMapper.taskToTaskResponse(task2)).thenReturn(taskResponse2);

            // When / Then
            mockMvc.perform(get("/api/v1/tasks/{customerId}/tasks", customerId)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.message").value(MESSAGE_SUCCESS_GET_ALL))
                    .andExpect(jsonPath("$.data", hasSize(2)))
                    .andExpect(jsonPath("$.data[0].title").value("Task 1"))
                    .andExpect(jsonPath("$.data[1].title").value("Task 2"));
        }

        @Test
        void shouldReturnEmptyListAndStatusOkWhenNoTasksExistForExistingCustomer() throws Exception {
            // Given
            when(taskService.getAllTasksByCustomerId(customerId)).thenReturn(List.of());

            // When / Then
            mockMvc.perform(get("/api/v1/tasks/{customerId}/tasks", customerId)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.message").value(MESSAGE_SUCCESS_GET_ALL))
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data").isEmpty());
        }

        @Test
        void shouldReturnEmptyListAndStatusOkWhenCustomerDoesNotExist() throws Exception {
            // Given
            UUID nonExistentCustomerId = customerId;
            when(taskService.getAllTasksByCustomerId(nonExistentCustomerId)).thenReturn(List.of());

            // When / Then
            mockMvc.perform(get("/api/v1/tasks/{customerId}/tasks", nonExistentCustomerId)
                        .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.message").value(MESSAGE_SUCCESS_GET_ALL))
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data").isEmpty());
        }
    }

    @Nested
    class RegisterTasksTests {

        @Test
        void shouldCreateTaskAndReturnStatusCreatedWhenRequestIsValid() throws Exception {
            // Given
            final UUID taskId = UUID.randomUUID();

            TaskRequest taskRequest = new TaskRequest(
                    null, "New Task", null,
                    ZonedDateTime.now().plusSeconds(1),
                    ZonedDateTime.now().plusDays(1),
                    "OPEN"
            );

            Task task = createTask(taskId, taskRequest.title());
            TaskResponse taskResponse = createTaskResponse(task);

            when(taskService.saveTask(any(TaskRequest.class)))
                    .thenReturn(task);
            when(taskMapper.taskToTaskResponse(task))
                    .thenReturn(taskResponse);

            // When / Then
            mockMvc.perform(post("/api/v1/tasks")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(taskRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.message").value(MESSAGE_SUCCESS_CREATE))
                    .andExpect(jsonPath("$.data.id").value(taskId.toString()))
                    .andExpect(jsonPath("$.data.title").value("New Task"));
        }

        @ParameterizedTest
        @MethodSource("invalidTaskRequests")
        void shouldReturnBadRequestForInvalidTaskRequests(TaskRequest invalidTaskRequest) throws Exception {
            mockMvc.perform(post("/api/v1/tasks")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidTaskRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.message").value(MESSAGE_ERROR_VALIDATION));
        }

        private static Stream<TaskRequest> invalidTaskRequests() {
            return TaskControllerTest.invalidTaskRequests();
        }
    }

    @Nested
    class UpdateTaskTests {

        private UUID taskId;

        @BeforeEach
        void setUp() {
            taskId = UUID.randomUUID();
        }

        @Test
        void shouldUpdateTaskAndReturnStatusOkWhenRequestIsValid() throws Exception {
            //Given
            TaskRequest taskRequest = new TaskRequest(
                    null, "Updated Task", null,
                    ZonedDateTime.now().plusSeconds(1),
                    ZonedDateTime.now().plusDays(1),
                    "IN_PROGRESS"
            );

            Task updatedTask = createTask(taskId, taskRequest.title());
            TaskResponse taskResponse = createTaskResponse(updatedTask);

            when(taskService.updateTask(any(UUID.class), any(TaskRequest.class)))
                    .thenReturn(updatedTask);
            when(taskMapper.taskToTaskResponse(updatedTask))
                    .thenReturn(taskResponse);

            // When / Then
            mockMvc.perform(put("/api/v1/tasks/{id}", taskId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(taskRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.message").value(MESSAGE_SUCCESS_UPDATE))
                    .andExpect(jsonPath("$.data.id").value(taskId.toString()))
                    .andExpect(jsonPath("$.data.title").value("Updated Task"));
        }

        @Test
        void shouldThrowTaskNotFoundExceptionAndReturnStatusNotFoundWhenTaskDoesNotExist() throws Exception {
            // Given
            TaskRequest taskRequest = new TaskRequest(
                    null, "Updated Task", null,
                    ZonedDateTime.now().plusSeconds(1),
                    ZonedDateTime.now().plusDays(1),
                    "IN_PROGRESS"
            );

            when(taskService.updateTask(any(UUID.class), any(TaskRequest.class)))
                    .thenThrow(new TaskNotFoundException("Task not found"));

            // When / Then
            mockMvc.perform(put("/api/v1/tasks/{id}", taskId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(taskRequest)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.message").value(MESSAGE_ERROR_NOT_FOUND));
        }

        @ParameterizedTest
        @MethodSource("invalidTaskRequests")
        void shouldReturnBadRequestForInvalidTaskRequests(TaskRequest invalidTaskRequest) throws Exception {
            mockMvc.perform(put("/api/v1/tasks/{id}", taskId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidTaskRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.message").value(MESSAGE_ERROR_VALIDATION));
        }

        private static Stream<TaskRequest> invalidTaskRequests() {
            return TaskControllerTest.invalidTaskRequests();
        }
    }

    @Nested
    class DeleteTaskTests {

        private UUID taskId;

        @BeforeEach
        void setUp() {
            taskId = UUID.randomUUID();
        }

        @Test
        void shouldReturnStatusOkWhenTaskIsSuccessfullyDeleted() throws Exception {
            // Given
            Task deletedTask = createTask(taskId, "Deleted Task");
            TaskResponse taskResponse = createTaskResponse(deletedTask);

            when(taskService.deleteTask(taskId))
                    .thenReturn(deletedTask);
            when(taskMapper.taskToTaskResponse(any(Task.class)))
                    .thenReturn(taskResponse);

            // When / Then
            mockMvc.perform(delete("/api/v1/tasks/{id}", taskId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.message").value(MESSAGE_SUCCESS_DELETE))
                    .andExpect(jsonPath("$.data.id").value(taskId.toString()))
                    .andExpect(jsonPath("$.data.title").value("Deleted Task"));
        }

        @Test
        void shouldThrowTaskNotFoundExceptionAndReturnStatusNotFoundWhenTaskDoesNotExist() throws Exception {
            // Given
            when(taskService.deleteTask(taskId))
                    .thenThrow(new TaskNotFoundException("Task not found"));

            // When / Then
            mockMvc.perform(delete("/api/v1/tasks/{id}", taskId))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.message").value(MESSAGE_ERROR_NOT_FOUND));
        }
    }

    private static Stream<TaskRequest> invalidTaskRequests() {
        return Stream.of(
                new TaskRequest(null, "", "Title is missing", null, null, null),
                new TaskRequest(null, "Task with past reminder", null, ZonedDateTime.now().minusNanos(1), null, null),
                new TaskRequest(null, "Task with present due date", null, ZonedDateTime.now(), null, null),
                new TaskRequest(null, "Task with invalid status", null, null, null, "INVALID_STATUS")
        );
    }

    private Task createTask(UUID id, String title) {
        return Task.builder()
                .id(id)
                .title(title)
                .build();
    }

    private TaskResponse createTaskResponse(Task task) {
        UUID customerId = task.customer() != null
                ? task.customer().id()
                : null;

        return new TaskResponse(
                task.id(),
                customerId,
                task.title(),
                task.description(),
                task.reminder(),
                task.dueDate(),
                task.status(),
                null,
                null,
                null
        );
    }
}