package hu.bhr.crm.service;

import hu.bhr.crm.controller.dto.TaskRequest;
import hu.bhr.crm.exception.TaskNotFoundException;
import hu.bhr.crm.mapper.TaskMapper;
import hu.bhr.crm.model.Customer;
import hu.bhr.crm.model.Task;
import hu.bhr.crm.model.TaskStatus;
import hu.bhr.crm.repository.TaskRepository;
import hu.bhr.crm.repository.entity.CustomerEntity;
import hu.bhr.crm.repository.entity.TaskEntity;
import hu.bhr.crm.scheduler.EmailSchedulerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;
    @Mock
    private TaskMapper taskMapper;
    @Mock
    private CustomerService customerService;
    @Mock
    private EmailSchedulerService emailSchedulerService;
    private TaskService underTest;

    @BeforeEach
    void setUp() {
        underTest = new TaskService(taskRepository, taskMapper, customerService, emailSchedulerService);
    }


    @Nested
    class GetTaskByIdTests {

        private UUID taskId;

        @BeforeEach
        void setUp() {
            taskId = UUID.randomUUID();
        }

        @Test
        void shouldReturnTaskWhenTaskExists() {
            // Given
            TaskEntity taskEntity = new TaskEntity();
            taskEntity.setId(taskId);

            Task mappedTask = new Task(taskId, null, "title", "description",
                    null, null, null, null, null, null
            );

            when(taskRepository.findById(taskId)).thenReturn(Optional.of(taskEntity));
            when(taskMapper.taskEntityToTask(taskEntity)).thenReturn(mappedTask);

            // When
            Task result = underTest.getTaskById(taskId);

            // Then
            assertEquals(mappedTask, result);
        }

        @Test
        void shouldThrowTaskNotFoundExceptionWhenTaskDoesNotExist() {
            // Given
            when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

            // When / Then
            assertThrows(TaskNotFoundException.class, () -> underTest.getTaskById(taskId));
        }
    }

    @Nested
    class GetAllTasksTests {
        @Test
        void shouldReturnAllTasks() {
            // Given
            TaskEntity taskEntity1 = new TaskEntity();
            taskEntity1.setId(UUID.randomUUID());
            TaskEntity taskEntity2 = new TaskEntity();
            taskEntity2.setId(UUID.randomUUID());

            when(taskRepository.findAll()).thenReturn(List.of(taskEntity1, taskEntity2));

            Task task1 = new Task(taskEntity1.getId(), null, "title1", "description1",
                    null, null, null, null, null, null
            );
            Task task2 = new Task(taskEntity2.getId(), null, "title2", "description2",
                    null, null, null, null, null, null
            );

            when(taskMapper.taskEntityToTask(taskEntity1)).thenReturn(task1);
            when(taskMapper.taskEntityToTask(taskEntity2)).thenReturn(task2);

            // When
            List<Task> result = underTest.getAllTasks();

            // Then
            assertEquals(2, result.size());
            assertEquals(List.of(task1, task2), result);
        }

        @Test
        void shouldReturnEmptyListWhenNoTasksExist() {
            // Given
            when(taskRepository.findAll()).thenReturn(List.of());

            // When
            List<Task> result = underTest.getAllTasks();

            // Then
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    class GetAllTasksByCustomerIdTests {

        private UUID customerId;
        private TaskEntity taskEntity1;
        private TaskEntity taskEntity2;
        private Task task1;
        private Task task2;

        @BeforeEach
        void setUp() {
            customerId = UUID.randomUUID();
            taskEntity1 = new TaskEntity();
            taskEntity1.setId(UUID.randomUUID());
            taskEntity2 = new TaskEntity();
            taskEntity2.setId(UUID.randomUUID());

            task1 = new Task(taskEntity1.getId(), null, "title1", "description1",
                    null, null, null, null, null, null
            );
            task2 = new Task(taskEntity2.getId(), null, "title2", "description2",
                    null, null, null, null, null, null
            );
        }

        @Test
        void shouldReturnAllTasksByCustomerIdWhenCustomerExists() {
            // Given
            when(taskRepository.findAllByCustomerId(customerId)).thenReturn(List.of(taskEntity1, taskEntity2));
            when(taskMapper.taskEntityToTask(taskEntity1)).thenReturn(task1);
            when(taskMapper.taskEntityToTask(taskEntity2)).thenReturn(task2);

            // When
            List<Task> result = underTest.getAllTasksByCustomerId(customerId);

            // Then
            assertEquals(2, result.size());
            assertEquals(List.of(task1, task2), result);
        }

        @Test
        void shouldReturnEmptyListWhenNoTasksForCustomer() {
            // Given
            when(taskRepository.findAllByCustomerId(customerId)).thenReturn(List.of());

            // When
            List<Task> result = underTest.getAllTasksByCustomerId(customerId);

            // Then
            assertTrue(result.isEmpty());
        }

        @Test
        void shouldReturnEmptyListWhenCustomerDoesNotExist() {
            // Given
            UUID nonExistentCustomerId = customerId;
            when(taskRepository.findAllByCustomerId(nonExistentCustomerId)).thenReturn(List.of());

            // When
            List<Task> result = underTest.getAllTasksByCustomerId(nonExistentCustomerId);

            // Then
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    class SaveTaskTests {

        @Test
        void shouldSaveTaskWithoutCustomerAndWithoutReminderSchedule() {
            // Given
            TaskRequest request = new TaskRequest(
                    null,
                    "title",
                    "description",
                    null,
                    null,
                    "ON_HOLD"
            );

            TaskEntity savedEntity = new TaskEntity();
            savedEntity.setId(UUID.randomUUID());
            savedEntity.setTitle(request.title());
            savedEntity.setDescription(request.description());

            mockSaveTaskEntity(savedEntity);
            when(taskMapper.taskEntityToTask(savedEntity)).thenReturn(
                    Task.builder()
                            .id(savedEntity.getId())
                            .title(savedEntity.getTitle())
                            .description(savedEntity.getDescription())
                            .build()
            );

            // When
            Task result = underTest.saveTask(request);

            // Then
            assertEquals(savedEntity.getId(), result.id());
            assertEquals("title", result.title());
            assertEquals("description", result.description());
            verify(customerService, never()).getCustomerById(any());
            verify(emailSchedulerService, never()).scheduleEmail(any(), any());
        }

        @Test
        void shouldSaveTaskWithCustomer() {
            // Given
            UUID customerId = UUID.randomUUID();
            TaskRequest request = new TaskRequest(
                    customerId,
                    "title",
                    "description",
                    null,
                    null,
                    "ON_HOLD"
            );

            Customer customer = Customer.builder().id(customerId).build();

            TaskEntity savedEntity = new TaskEntity();
            savedEntity.setId(UUID.randomUUID());

            when(customerService.getCustomerById(customerId)).thenReturn(customer);

            mockSaveTaskEntity(savedEntity);
            when(taskMapper.taskEntityToTask(savedEntity)).thenReturn(
                    Task.builder()
                            .id(savedEntity.getId())
                            .customer(customer)
                            .build());

            // When
            Task result = underTest.saveTask(request);

            // Then
            assertEquals(savedEntity.getId(), result.id());
            assertEquals(customer, result.customer());
            verify(customerService).getCustomerById(customerId);
            verify(emailSchedulerService, never()).scheduleEmail(any(), any());
        }

        @Test
        void shouldScheduleTaskWhenReminderProvided() {
            // Given
            ZonedDateTime reminderZdt = ZonedDateTime.now().plusDays(1);
            Instant reminderInstant = reminderZdt.toInstant();

            TaskRequest request = new TaskRequest(
                    null,
                    "title",
                    "description",
                    reminderZdt,
                    null,
                    "ON_HOLD"
            );

            TaskEntity savedEntity = new TaskEntity();
            savedEntity.setId(UUID.randomUUID());

            mockSaveTaskEntity(savedEntity);
            when(taskMapper.taskEntityToTask(savedEntity)).thenReturn(
                    Task.builder()
                            .id(savedEntity.getId())
                            .build());

            // When
            Task result = underTest.saveTask(request);

            // Then
            assertEquals(savedEntity.getId(), result.id());
            verify(customerService, never()).getCustomerById(any());
            verify(emailSchedulerService).scheduleEmail(savedEntity.getId(), reminderInstant);
        }

        @Test
        void shouldSetCompletedAtWhenStatusIsCompleted() {
            // Given
            TaskRequest request = new TaskRequest(
                    null,
                    "title",
                    "description",
                    null,
                    null,
                    "COMPLETED"
            );

            TaskEntity savedEntity = new TaskEntity();
            savedEntity.setId(UUID.randomUUID());
            savedEntity.setStatus(TaskStatus.fromString(request.status()));

            savedEntity.setCompletedAt(null);

            mockSaveTaskEntity(savedEntity);
            when(taskMapper.taskEntityToTask(savedEntity)).thenReturn(
                    Task.builder()
                            .id(savedEntity.getId())
                            .status(savedEntity.getStatus())
                            .build());

            // When
            Task result = underTest.saveTask(request);

            // Then
            assertEquals(savedEntity.getId(), result.id());
            assertEquals(TaskStatus.COMPLETED, result.status());
            assertNotNull(savedEntity.getCompletedAt(), "Service should generate timestamp for new COMPLETED tasks");
        }

        private void mockSaveTaskEntity(TaskEntity savedEntity) {
            when(taskMapper.taskToTaskEntity(any())).thenReturn(savedEntity);
            when(taskRepository.save(any())).thenReturn(savedEntity);
        }
    }

    @Nested
    class UpdateTaskTests {

        private UUID taskId;
        private TaskEntity oldTaskEntity;
        private TaskEntity updatedTaskEntity;
        private TaskRequest request;

        @BeforeEach
        void setUp() {
            taskId = UUID.randomUUID();

            oldTaskEntity = new TaskEntity();
            oldTaskEntity.setId(taskId);

            updatedTaskEntity = new TaskEntity();
            updatedTaskEntity.setId(taskId);
        }

        @Test
        void shouldUpdateTaskWithoutCustomer() {
            // Given
            request = new TaskRequest(
                    null,
                    "updated title",
                    "updated description",
                    null,
                    null,
                    "ON_HOLD"
            );

            oldTaskEntity.setTitle("old title");
            updatedTaskEntity.setTitle(request.title());
            updatedTaskEntity.setDescription(request.description());

            mockUpdateTaskEntity(oldTaskEntity, updatedTaskEntity);
            when(taskMapper.taskEntityToTask(updatedTaskEntity))
                    .thenReturn(Task.builder()
                            .id(taskId)
                            .title(updatedTaskEntity.getTitle())
                            .description(updatedTaskEntity.getDescription())
                            .build());

            // When
            Task result = underTest.updateTask(taskId, request);

            // Then
            assertEquals(taskId, result.id());
            assertEquals("updated title", result.title());
            assertEquals("updated description", result.description());
            verify(customerService, never()).getCustomerById(any());
        }

        @Test
        void shouldUpdateTaskWithCustomer() {
            // Given
            UUID customerId = UUID.randomUUID();
            request = new TaskRequest(
                    customerId,
                    "title",
                    "description",
                    null,
                    null,
                    "ON_HOLD"
            );

            Customer customer = Customer.builder().id(customerId).build();

            when(customerService.getCustomerById(customerId)).thenReturn(customer);

            mockUpdateTaskEntity(oldTaskEntity, updatedTaskEntity);
            when(taskMapper.taskEntityToTask(updatedTaskEntity))
                    .thenReturn(Task.builder()
                            .id(taskId)
                            .customer(customer)
                            .build());

            // When
            Task result = underTest.updateTask(taskId, request);

            // Then
            assertEquals(taskId, result.id());
            assertEquals(customer, result.customer());
            verify(customerService).getCustomerById(customerId);
        }

        @Test
        void shouldSetCompletedAtWhenStatusIsUpdatedToCompleted() {
            // Given
            request = new TaskRequest(
                    null,
                    "title",
                    "description",
                    null,
                    null,
                    "COMPLETED"
            );

            oldTaskEntity.setStatus(TaskStatus.IN_PROGRESS);
            updatedTaskEntity.setStatus(TaskStatus.fromString(request.status()));

            mockUpdateTaskEntity(oldTaskEntity, updatedTaskEntity);
            when(taskMapper.taskEntityToTask(updatedTaskEntity))
                    .thenReturn(Task.builder()
                            .id(taskId)
                            .status(updatedTaskEntity.getStatus())
                            .build());

            // When
            Task result = underTest.updateTask(taskId, request);

            // Then
            assertEquals(taskId, result.id());
            assertEquals(TaskStatus.COMPLETED, result.status());
            assertNotNull(updatedTaskEntity.getCompletedAt());
        }

        @Test
        void shouldPreserveOriginalCompletedAtWhenTaskStatusRemainsCompleted() {
            // Given
            Instant originalTime = Instant.now().minusSeconds(3600);
            oldTaskEntity.setStatus(TaskStatus.COMPLETED);
            oldTaskEntity.setCompletedAt(originalTime);

            request = new TaskRequest(
                    null,
                    "new desc",
                    "desc",
                    null,
                    null,
                    "COMPLETED"
            );

            updatedTaskEntity.setStatus(TaskStatus.COMPLETED);
            updatedTaskEntity.setCompletedAt(null); // Mapper creates new entity, initially null

            mockUpdateTaskEntity(oldTaskEntity, updatedTaskEntity);
            when(taskMapper.taskEntityToTask(updatedTaskEntity)).thenReturn(
                    Task.builder().id(taskId).status(TaskStatus.COMPLETED).build());

            // When
            underTest.updateTask(taskId, request);

            // Then
            assertEquals(originalTime, updatedTaskEntity.getCompletedAt(), "Should preserve original timestamp (Idempotency)");
        }

        @Test
        void shouldClearCompletedAtWhenTaskIsReopened() {
            // Given
            oldTaskEntity.setStatus(TaskStatus.COMPLETED);
            oldTaskEntity.setCompletedAt(Instant.now());

            request = new TaskRequest(
                    null,
                    "title",
                    "desc",
                    null,
                    null,
                    "IN_PROGRESS"
            );

            updatedTaskEntity.setStatus(TaskStatus.IN_PROGRESS);
            updatedTaskEntity.setCompletedAt(oldTaskEntity.getCompletedAt());

            mockUpdateTaskEntity(oldTaskEntity, updatedTaskEntity);
            when(taskMapper.taskEntityToTask(updatedTaskEntity)).thenReturn(
                    Task.builder().id(taskId).status(TaskStatus.IN_PROGRESS).build());

            // When
            underTest.updateTask(taskId, request);

            // Then
            assertNull(updatedTaskEntity.getCompletedAt(), "Timestamp should be null when task is reopened");
        }

        @Test
        void shouldThrowTaskNotFoundExceptionWhenTaskDoesNotExist() {
            // Given
            request = new TaskRequest(
                    null,
                    "title",
                    "description",
                    null,
                    null,
                    "ON_HOLD"
            );

            when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

            // When / Then
            assertThrows(TaskNotFoundException.class, () -> underTest.updateTask(taskId, request));
        }

        private void mockUpdateTaskEntity(TaskEntity oldTaskEntity, TaskEntity updatedTaskEntity) {
            when(taskRepository.findById(oldTaskEntity.getId()))
                    .thenReturn(Optional.of(oldTaskEntity));

            when(taskMapper.taskToTaskEntity(any())).thenReturn(updatedTaskEntity);
            when(taskRepository.save(any(TaskEntity.class))).thenReturn(updatedTaskEntity);
        }
    }

    @Nested
    class UpdateTaskReminderTests {

        private UUID taskId;
        private TaskEntity oldTaskEntity;
        private TaskEntity newTaskEntity;
        private TaskRequest request;

        @BeforeEach
        void setUp() {
            taskId = UUID.randomUUID();
            oldTaskEntity = new TaskEntity();
            oldTaskEntity.setId(taskId);
            newTaskEntity = new TaskEntity();
            newTaskEntity.setId(taskId);
        }

        @Test
        void shouldScheduleEmailWhenReminderUpdateIsAdded() {
            // Given
            ZonedDateTime newReminderZdt = ZonedDateTime.now();
            Instant newReminderInstant = newReminderZdt.toInstant();

            oldTaskEntity.setReminder(null);
            newTaskEntity.setReminder(newReminderInstant);

            mockRepositoryUpdate(oldTaskEntity, newTaskEntity);

            request = new TaskRequest(
                    null,
                    "title",
                    "description",
                    newReminderZdt,
                    null,
                    "ON_HOLD"
            );

            // When
            underTest.updateTask(taskId, request);

            // Then
            verify(emailSchedulerService).scheduleEmail(taskId, newTaskEntity.getReminder());
            verify(emailSchedulerService, never()).deleteEmailSchedule(any());
            verify(emailSchedulerService, never()).updateEmailScheduleTime(any(), any());
        }

        @Test
        void shouldDeleteEmailScheduleWhenReminderUpdateIsDeleted() {
            // Given
            oldTaskEntity.setReminder(Instant.now());
            newTaskEntity.setReminder(null);

            mockRepositoryUpdate(oldTaskEntity, newTaskEntity);

            request = new TaskRequest(
                    null,
                    "title",
                    "description",
                    null,
                    null,
                    "ON_HOLD"
            );

            // When
            underTest.updateTask(taskId, request);

            // Then
            verify(emailSchedulerService).deleteEmailSchedule(taskId);
            verify(emailSchedulerService, never()).scheduleEmail(any(), any());
            verify(emailSchedulerService, never()).updateEmailScheduleTime(any(), any());
        }

        @Test
        void shouldUpdateEmailScheduleWhenReminderDateIsUpdated() {
            // Given
            Instant oldReminderInstant = Instant.now();
            ZonedDateTime newReminderZdt = ZonedDateTime.now().plusSeconds(3600);
            Instant newReminderInstant = newReminderZdt.toInstant();


            oldTaskEntity.setReminder(oldReminderInstant);
            newTaskEntity.setReminder(newReminderInstant);

            mockRepositoryUpdate(oldTaskEntity, newTaskEntity);

            request = new TaskRequest(
                    null,
                    "title",
                    "description",
                    newReminderZdt,
                    null,
                    "ON_HOLD"
            );

            // When
            underTest.updateTask(taskId, request);

            // Then
            verify(emailSchedulerService).updateEmailScheduleTime(taskId, newTaskEntity.getReminder());
            verify(emailSchedulerService, never()).scheduleEmail(any(), any());
            verify(emailSchedulerService, never()).deleteEmailSchedule(any());
        }

        @Test
        void shouldDoNothingWhenReminderDateRemainsTheSame() {
            // Given
            ZonedDateTime reminderZdt = ZonedDateTime.now();
            Instant reminderInstant = reminderZdt.toInstant();

            oldTaskEntity.setReminder(reminderInstant);
            newTaskEntity.setReminder(reminderInstant);

            mockRepositoryUpdate(oldTaskEntity, newTaskEntity);

            request = new TaskRequest(
                    null,
                    "title",
                    "description",
                    reminderZdt,
                    null,
                    "ON_HOLD"
            );

            // Then
            underTest.updateTask(taskId, request);

            // When
            verify(emailSchedulerService, never()).scheduleEmail(any(), any());
            verify(emailSchedulerService, never()).deleteEmailSchedule(any());
            verify(emailSchedulerService, never()).updateEmailScheduleTime(any(), any());
        }

        private void mockRepositoryUpdate(TaskEntity oldTaskEntity, TaskEntity newTaskEntity) {
            when(taskRepository.findById(oldTaskEntity.getId())).
                    thenReturn(Optional.of(oldTaskEntity)).
                    thenReturn(Optional.of(newTaskEntity));

            when(taskMapper.taskToTaskEntity(any())).
                    thenReturn(newTaskEntity);
            when(taskRepository.save(any(TaskEntity.class))).
                    thenReturn(newTaskEntity);
        }

        @Test
        void shouldDoNothingWhenReminderDateRemainsNull() {
            // Given
            oldTaskEntity.setReminder(null);
            newTaskEntity.setReminder(null);

            mockRepositoryUpdate(oldTaskEntity, newTaskEntity);

            request = new TaskRequest(
                    null,
                    "title",
                    "description",
                    null,
                    null,
                    "ON_HOLD"
            );

            // Then
            underTest.updateTask(taskId, request);

            // When
            verify(emailSchedulerService, never()).scheduleEmail(any(), any());
            verify(emailSchedulerService, never()).deleteEmailSchedule(any());
            verify(emailSchedulerService, never()).updateEmailScheduleTime(any(), any());
        }
    }

    @Nested
    class DeleteTaskTests {

        private UUID taskId;
        private TaskEntity taskEntity;

        @BeforeEach
        void setUp() {
            taskId = UUID.randomUUID();
            taskEntity = new TaskEntity();
            taskEntity.setId(taskId);
        }

        @Test
        void shouldDeleteTaskWhenTaskExists() {
            // Given
            mockBuildTaskById(taskId);

            // When
            Task result = underTest.deleteTask(taskId);

            // Then
            assertEquals(taskId,result.id());
            verify(taskRepository).deleteById(taskId);
            verify(emailSchedulerService).deleteEmailSchedule(taskId);
        }

        @Test
        void shouldThrowTaskNotFoundExceptionWhenTaskDoesNotExist() {
            // Given
            when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

            // When / Then
            assertThrows(TaskNotFoundException.class, () -> underTest.deleteTask(taskId));
            verify(taskRepository, never()).deleteById(any());
            verify(emailSchedulerService, never()).deleteEmailSchedule(any());
        }

        @Test
        void shouldDeleteScheduleBeforeTaskEntity() {
            // Given
            mockBuildTaskById(taskId);

            // When
            underTest.deleteTask(taskId);

            // Then
            InOrder inOrder = inOrder(taskRepository, taskMapper, emailSchedulerService);
            inOrder.verify(taskRepository).findById(taskId);
            inOrder.verify(emailSchedulerService).deleteEmailSchedule(taskId);
            inOrder.verify(taskMapper).taskEntityToTask(taskEntity);
            inOrder.verify(taskRepository).deleteById(taskId);
        }

        private void mockBuildTaskById(UUID taskId) {
            when(taskRepository.findById(taskId)).thenReturn(Optional.of(taskEntity));
            when(taskMapper.taskEntityToTask(taskEntity))
                    .thenReturn(Task.builder()
                            .id(taskId)
                            .build());
        }
    }

    @Nested
    class DetachCustomerFromTasksTests {

        private UUID customerId;

        @BeforeEach
        void setUp() {
            customerId = UUID.randomUUID();
        }

        @Test
        void shouldDetachCustomerFromTasksAndAddNoteWhenDescriptionIsNull() {
            // Given
            TaskEntity taskEntity = createTaskEntityWithCustomerAndDescription(customerId, null);

            when(taskRepository.findAllByCustomerId(customerId)).thenReturn(List.of(taskEntity));
            when(taskRepository.saveAll(List.of(taskEntity))).thenReturn(List.of(taskEntity));

            // When
            underTest.detachCustomerFromTasks(customerId);

            // Then
            assertNull(taskEntity.getCustomer());
            assertEquals("The related customer has been deleted.", taskEntity.getDescription());
            verify(taskRepository).findAllByCustomerId(customerId);
            verify(taskRepository).saveAll(List.of(taskEntity));
        }

        @Test
        void shouldDetachCustomerFromTasksAndAppendNoteWhenDescriptionExists() {
            // Given
            TaskEntity taskEntity = createTaskEntityWithCustomerAndDescription(customerId, "Existing description.");

            when(taskRepository.findAllByCustomerId(customerId)).thenReturn(List.of(taskEntity));
            when(taskRepository.saveAll(List.of(taskEntity))).thenReturn(List.of(taskEntity));

            // When
            underTest.detachCustomerFromTasks(customerId);

            // Then
            assertNull(taskEntity.getCustomer());
            assertEquals("Existing description. The related customer has been deleted.", taskEntity.getDescription());
            verify(taskRepository).findAllByCustomerId(customerId);
            verify(taskRepository).saveAll(List.of(taskEntity));
        }

        @Test
        void shouldHandleMultipleTaskDetachments() {
            // Given
            TaskEntity taskEntity1 = createTaskEntityWithCustomerAndDescription(customerId, null);
            TaskEntity taskEntity2 = createTaskEntityWithCustomerAndDescription(customerId, "Existing description.");

            when(taskRepository.findAllByCustomerId(customerId)).thenReturn(List.of(taskEntity1, taskEntity2));
            when(taskRepository.saveAll(List.of(taskEntity1, taskEntity2))).thenReturn(List.of(taskEntity1, taskEntity2));

            // When
            underTest.detachCustomerFromTasks(customerId);

            // Then
            assertNull(taskEntity1.getCustomer());
            assertEquals("The related customer has been deleted.", taskEntity1.getDescription());

            assertNull(taskEntity2.getCustomer());
            assertEquals("Existing description. The related customer has been deleted.", taskEntity2.getDescription());

            verify(taskRepository).findAllByCustomerId(customerId);
            verify(taskRepository).saveAll(List.of(taskEntity1, taskEntity2));
        }

        private TaskEntity createTaskEntityWithCustomerAndDescription(UUID customerId, String description) {
            TaskEntity taskEntity = new TaskEntity();
            CustomerEntity customerEntity = new CustomerEntity();
            customerEntity.setId(customerId);
            taskEntity.setCustomer(customerEntity);
            taskEntity.setDescription(description);
            return taskEntity;
        }
    }
}