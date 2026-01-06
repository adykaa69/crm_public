package hu.bhr.crm.service;

import hu.bhr.crm.exception.TaskNotFoundException;
import hu.bhr.crm.mapper.TaskMapper;
import hu.bhr.crm.model.Task;
import hu.bhr.crm.model.TaskStatus;
import hu.bhr.crm.repository.TaskRepository;
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
import static org.mockito.Mockito.doNothing;
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
            Task task = Task.builder()
                    .id(UUID.randomUUID())
                    .title("title")
                    .description("description")
                    .build();

            TaskEntity savedEntity = new TaskEntity();
            savedEntity.setId(task.id());
            savedEntity.setTitle(task.title());
            savedEntity.setDescription(task.description());

            mockSaveTaskEntity(savedEntity);
            when(taskMapper.taskEntityToTask(savedEntity)).thenReturn(task);

            // When
            Task result = underTest.saveTask(task);

            // Then
            assertEquals(savedEntity.getId(), result.id());
            assertEquals("title", result.title());
            assertEquals("description", result.description());
            verify(customerService, never()).validateCustomerExists(any());
            verify(emailSchedulerService, never()).scheduleEmail(any(), any());
        }

        @Test
        void shouldSaveTaskWithCustomer() {
            // Given
            UUID customerId = UUID.randomUUID();
            Task task = Task.builder()
                    .id(UUID.randomUUID())
                    .customerId(customerId)
                    .title("title")
                    .description("description")
                    .build();

            TaskEntity savedEntity = new TaskEntity();
            savedEntity.setId(task.id());
            savedEntity.setCustomerId(customerId);

            doNothing().when(customerService).validateCustomerExists(customerId);

            mockSaveTaskEntity(savedEntity);
            when(taskMapper.taskEntityToTask(savedEntity)).thenReturn(task);

            // When
            Task result = underTest.saveTask(task);

            // Then
            assertEquals(savedEntity.getId(), result.id());
            assertEquals(customerId, result.customerId());
            verify(customerService).validateCustomerExists(customerId);
            verify(emailSchedulerService, never()).scheduleEmail(any(), any());
        }

        @Test
        void shouldScheduleTaskWhenReminderProvided() {
            // Given
            ZonedDateTime reminderZdt = ZonedDateTime.now().plusDays(1);
            Instant reminderInstant = reminderZdt.toInstant();

            Task task = Task.builder()
                    .id(UUID.randomUUID())
                    .title("title")
                    .description("description")
                    .reminder(reminderInstant)
                    .build();

            TaskEntity savedEntity = new TaskEntity();
            savedEntity.setId(task.id());

            mockSaveTaskEntity(savedEntity);
            when(taskMapper.taskEntityToTask(savedEntity)).thenReturn(task);

            // When
            Task result = underTest.saveTask(task);

            // Then
            assertEquals(savedEntity.getId(), result.id());
            verify(customerService, never()).getCustomerById(any());
            verify(emailSchedulerService).scheduleEmail(savedEntity.getId(), reminderInstant);
        }

        @Test
        void shouldSetCompletedAtWhenStatusIsCompleted() {
            // Given
            Task task = Task.builder()
                    .id(UUID.randomUUID())
                    .title("title")
                    .description("description")
                    .status(TaskStatus.fromString("COMPLETED"))
                    .build();

            TaskEntity savedEntity = new TaskEntity();
            savedEntity.setId(task.id());
            savedEntity.setStatus((task.status()));

            savedEntity.setCompletedAt(null);

            mockSaveTaskEntity(savedEntity);
            when(taskMapper.taskEntityToTask(savedEntity)).thenReturn(task);

            // When
            Task result = underTest.saveTask(task);

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
        private Task task;

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
            task = Task.builder()
                    .id(taskId)
                    .title("updated title")
                    .description("updated description")
                    .status(TaskStatus.fromString("ON_HOLD"))
                    .build(
            );

            oldTaskEntity.setTitle("old title");
            updatedTaskEntity.setTitle(task.title());
            updatedTaskEntity.setDescription(task.description());

            mockUpdateTaskEntity(oldTaskEntity, updatedTaskEntity);
            when(taskMapper.taskEntityToTask(updatedTaskEntity))
                    .thenReturn(task);

            // When
            Task result = underTest.updateTask(task);

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

            task = Task.builder()
                    .id(taskId)
                    .customerId(customerId)
                    .title("updated title")
                    .description("updated description")
                    .status(TaskStatus.fromString("ON_HOLD"))
                    .build(
            );

            doNothing().when(customerService).validateCustomerExists(customerId);

            mockUpdateTaskEntity(oldTaskEntity, updatedTaskEntity);
            when(taskMapper.taskEntityToTask(updatedTaskEntity))
                    .thenReturn(task);

            // When
            Task result = underTest.updateTask(task);

            // Then
            assertEquals(taskId, result.id());
            assertEquals(customerId, result.customerId());
            verify(customerService).validateCustomerExists(customerId);
        }

        @Test
        void shouldSetCompletedAtWhenStatusIsUpdatedToCompleted() {
            // Given
            task = Task.builder()
                    .id(taskId)
                    .title("updated title")
                    .description("updated description")
                    .status(TaskStatus.fromString("COMPLETED"))
                    .build();

            oldTaskEntity.setStatus(TaskStatus.IN_PROGRESS);
            updatedTaskEntity.setStatus(task.status());

            mockUpdateTaskEntity(oldTaskEntity, updatedTaskEntity);
            when(taskMapper.taskEntityToTask(updatedTaskEntity))
                    .thenReturn(Task.builder()
                            .id(taskId)
                            .status(updatedTaskEntity.getStatus())
                            .build());

            // When
            Task result = underTest.updateTask(task);

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

            task = Task.builder()
                    .id(taskId)
                    .title("updated title")
                    .description("updated description")
                    .status(TaskStatus.COMPLETED)
                    .build();

            updatedTaskEntity.setStatus(TaskStatus.COMPLETED);
            updatedTaskEntity.setCompletedAt(null); // Mapper creates new entity, initially null

            mockUpdateTaskEntity(oldTaskEntity, updatedTaskEntity);
            when(taskMapper.taskEntityToTask(updatedTaskEntity)).thenReturn(task);

            // When
            underTest.updateTask(task);

            // Then
            assertEquals(originalTime, updatedTaskEntity.getCompletedAt(), "Should preserve original timestamp (Idempotency)");
        }

        @Test
        void shouldClearCompletedAtWhenTaskIsReopened() {
            // Given
            oldTaskEntity.setStatus(TaskStatus.COMPLETED);
            oldTaskEntity.setCompletedAt(Instant.now());

            task = Task.builder()
                    .id(taskId)
                    .title("updated title")
                    .description("updated description")
                    .status(TaskStatus.IN_PROGRESS)
                    .build();

            updatedTaskEntity.setStatus(TaskStatus.IN_PROGRESS);
            updatedTaskEntity.setCompletedAt(oldTaskEntity.getCompletedAt());

            mockUpdateTaskEntity(oldTaskEntity, updatedTaskEntity);
            when(taskMapper.taskEntityToTask(updatedTaskEntity)).thenReturn(
                    Task.builder().id(taskId).status(TaskStatus.IN_PROGRESS).build());

            // When
            underTest.updateTask(task);

            // Then
            assertNull(updatedTaskEntity.getCompletedAt(), "Timestamp should be null when task is reopened");
        }

        @Test
        void shouldThrowTaskNotFoundExceptionWhenTaskDoesNotExist() {
            // Given

            task = Task.builder()
                    .id(taskId)
                    .title("updated title")
                    .description("updated description")
                    .status(TaskStatus.fromString("ON_HOLD"))
                    .build();

            when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

            // When / Then
            assertThrows(TaskNotFoundException.class, () -> underTest.updateTask(task));
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
        private Task task;

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
            Instant newReminderInstant = Instant.now();

            oldTaskEntity.setReminder(null);
            newTaskEntity.setReminder(newReminderInstant);

            mockRepositoryUpdate(oldTaskEntity, newTaskEntity);

            task = Task.builder()
                    .id(taskId)
                    .title("title")
                    .description("description")
                    .reminder(newReminderInstant)
                    .status(TaskStatus.fromString("ON_HOLD"))
                    .build();

            // When
            underTest.updateTask(task);

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

            task = Task.builder()
                    .id(taskId)
                    .title("title")
                    .description("description")
                    .status(TaskStatus.fromString("ON_HOLD"))
                    .build();

            // When
            underTest.updateTask(task);

            // Then
            verify(emailSchedulerService).deleteEmailSchedule(taskId);
            verify(emailSchedulerService, never()).scheduleEmail(any(), any());
            verify(emailSchedulerService, never()).updateEmailScheduleTime(any(), any());
        }

        @Test
        void shouldUpdateEmailScheduleWhenReminderDateIsUpdated() {
            // Given
            Instant oldReminderInstant = Instant.now();
            Instant newReminderInstant = Instant.now().plusSeconds(3600);


            oldTaskEntity.setReminder(oldReminderInstant);
            newTaskEntity.setReminder(newReminderInstant);

            mockRepositoryUpdate(oldTaskEntity, newTaskEntity);

            task = Task.builder()
                    .id(taskId)
                    .title("title")
                    .description("description")
                    .reminder(newReminderInstant)
                    .status(TaskStatus.fromString("ON_HOLD"))
                    .build();

            // When
            underTest.updateTask(task);

            // Then
            verify(emailSchedulerService).updateEmailScheduleTime(taskId, newTaskEntity.getReminder());
            verify(emailSchedulerService, never()).scheduleEmail(any(), any());
            verify(emailSchedulerService, never()).deleteEmailSchedule(any());
        }

        @Test
        void shouldDoNothingWhenReminderDateRemainsTheSame() {
            // Given
            Instant reminderInstant = Instant.now();

            oldTaskEntity.setReminder(reminderInstant);
            newTaskEntity.setReminder(reminderInstant);

            mockRepositoryUpdate(oldTaskEntity, newTaskEntity);

            task = Task.builder()
                    .id(taskId)
                    .title("title")
                    .description("description")
                    .reminder(reminderInstant)
                    .status(TaskStatus.fromString("ON_HOLD"))
                    .build();

            // Then
            underTest.updateTask(task);

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

            task = Task.builder()
                    .id(taskId)
                    .title("title")
                    .description("description")
                    .status(TaskStatus.fromString("ON_HOLD"))
                    .build();

            // Then
            underTest.updateTask(task);

            // When
            verify(emailSchedulerService, never()).scheduleEmail(any(), any());
            verify(emailSchedulerService, never()).deleteEmailSchedule(any());
            verify(emailSchedulerService, never()).updateEmailScheduleTime(any(), any());
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
        void shouldDeleteTaskWhenTaskExists() {
            // Given
            when(taskRepository.existsById(taskId)).thenReturn(true);

            // When
            underTest.deleteTask(taskId);

            // Then
            verify(taskRepository).deleteById(taskId);
            verify(emailSchedulerService).deleteEmailSchedule(taskId);
        }

        @Test
        void shouldThrowTaskNotFoundExceptionWhenTaskDoesNotExist() {
            // Given
            when(taskRepository.existsById(taskId)).thenReturn(false);

            // When / Then
            assertThrows(TaskNotFoundException.class, () -> underTest.deleteTask(taskId));
            verify(taskRepository, never()).deleteById(any());
            verify(emailSchedulerService, never()).deleteEmailSchedule(any());
        }

        @Test
        void shouldDeleteScheduleBeforeTaskEntity() {
            // Given
            when(taskRepository.existsById(taskId)).thenReturn(true);

            // When
            underTest.deleteTask(taskId);

            // Then
            InOrder inOrder = inOrder(taskRepository, taskMapper, emailSchedulerService);
            inOrder.verify(taskRepository).existsById(taskId);
            inOrder.verify(emailSchedulerService).deleteEmailSchedule(taskId);
            inOrder.verify(taskRepository).deleteById(taskId);
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
            assertNull(taskEntity.getCustomerId());
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
            assertNull(taskEntity.getCustomerId());
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
            assertNull(taskEntity1.getCustomerId());
            assertEquals("The related customer has been deleted.", taskEntity1.getDescription());

            assertNull(taskEntity2.getCustomerId());
            assertEquals("Existing description. The related customer has been deleted.", taskEntity2.getDescription());

            verify(taskRepository).findAllByCustomerId(customerId);
            verify(taskRepository).saveAll(List.of(taskEntity1, taskEntity2));
        }

        private TaskEntity createTaskEntityWithCustomerAndDescription(UUID customerId, String description) {
            TaskEntity taskEntity = new TaskEntity();
            taskEntity.setCustomerId(customerId);
            taskEntity.setDescription(description);
            return taskEntity;
        }
    }
}