package hu.bhr.crm.service;

import hu.bhr.crm.controller.dto.TaskRequest;
import hu.bhr.crm.mapper.TaskMapper;
import hu.bhr.crm.repository.TaskRepository;
import hu.bhr.crm.repository.entity.TaskEntity;
import hu.bhr.crm.scheduler.EmailSchedulerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TaskServiceTest {

    private TaskRepository taskRepository;
    private TaskMapper taskMapper;
    private CustomerService customerService;
    private EmailSchedulerService emailSchedulerService;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskRepository = mock(TaskRepository.class);
        taskMapper = mock(TaskMapper.class);
        customerService = mock(CustomerService.class);
        emailSchedulerService = mock(EmailSchedulerService.class);
        taskService = new TaskService(taskRepository, taskMapper, customerService, emailSchedulerService);
    }

    @Test
    void testHandleReminderUpdate_whenOldNullAndNewNotNull_shouldScheduleEmail() {
        UUID taskId = UUID.randomUUID();

        TaskEntity oldTaskEntity = new TaskEntity();
        oldTaskEntity.setId(taskId);
        oldTaskEntity.setReminder(null);

        TaskEntity newTaskEntity = new TaskEntity();
        newTaskEntity.setId(taskId);
        newTaskEntity.setReminder(ZonedDateTime.now());

        when(taskRepository.findById(taskId)).
                thenReturn(Optional.of(oldTaskEntity)).
                thenReturn(Optional.of(newTaskEntity));

        when(taskMapper.taskToTaskEntity(any())).
                thenReturn(newTaskEntity);
        when(taskRepository.save(any(TaskEntity.class))).
                thenReturn(newTaskEntity);

        TaskRequest request = new TaskRequest(
                null,
                "title",
                "description",
                newTaskEntity.getReminder(),
                null,
                "ON_HOLD"
        );

        taskService.updateTask(taskId, request);

        verify(emailSchedulerService).scheduleEmail(taskId, newTaskEntity.getReminder());
        verify(emailSchedulerService, never()).deleteEmailSchedule(any());
        verify(emailSchedulerService, never()).updateEmailScheduleTime(any(), any());
    }

    @Test
    void testHandleReminderUpdate_whenOldNotNullAndNewNull_shouldDeleteSchedule() {
        UUID taskId = UUID.randomUUID();

        TaskEntity oldTaskEntity = new TaskEntity();
        oldTaskEntity.setId(taskId);
        oldTaskEntity.setReminder(ZonedDateTime.now());

        TaskEntity newTaskEntity = new TaskEntity();
        newTaskEntity.setId(taskId);
        newTaskEntity.setReminder(null);

        when(taskRepository.findById(taskId)).
                thenReturn(Optional.of(oldTaskEntity)).
                thenReturn(Optional.of(newTaskEntity));

        when(taskMapper.taskToTaskEntity(any())).
                thenReturn(newTaskEntity);
        when(taskRepository.save(any(TaskEntity.class))).
                thenReturn(newTaskEntity);

        TaskRequest request = new TaskRequest(
                null,
                "title",
                "description",
                newTaskEntity.getReminder(),
                null,
                "ON_HOLD"
        );

        taskService.updateTask(taskId, request);

        verify(emailSchedulerService).deleteEmailSchedule(taskId);
        verify(emailSchedulerService, never()).scheduleEmail(any(), any());
        verify(emailSchedulerService, never()).updateEmailScheduleTime(any(), any());
    }

    @Test
    void testHandleReminderUpdate_whenOldNotNullAndNewDifferent_shouldUpdateSchedule() {
        UUID taskId = UUID.randomUUID();

        TaskEntity oldTaskEntity = new TaskEntity();
        oldTaskEntity.setId(taskId);
        oldTaskEntity.setReminder(ZonedDateTime.now());

        TaskEntity newTaskEntity = new TaskEntity();
        newTaskEntity.setId(taskId);
        newTaskEntity.setReminder(ZonedDateTime.now().plusSeconds(3600));

        when(taskRepository.findById(taskId)).
                thenReturn(Optional.of(oldTaskEntity)).
                thenReturn(Optional.of(newTaskEntity));

        when(taskMapper.taskToTaskEntity(any())).
                thenReturn(newTaskEntity);
        when(taskRepository.save(any(TaskEntity.class))).
                thenReturn(newTaskEntity);

        TaskRequest request = new TaskRequest(
                null,
                "title",
                "description",
                newTaskEntity.getReminder(),
                null,
                "ON_HOLD"
        );

        taskService.updateTask(taskId, request);

        verify(emailSchedulerService).updateEmailScheduleTime(taskId, newTaskEntity.getReminder());
        verify(emailSchedulerService, never()).scheduleEmail(any(), any());
        verify(emailSchedulerService, never()).deleteEmailSchedule(any());
    }

    @Test
    void testHandleReminderUpdate_whenOldEqualsNew_shouldDoNothing() {
        UUID taskId = UUID.randomUUID();
        ZonedDateTime reminder = ZonedDateTime.now();

        TaskEntity oldTaskEntity = new TaskEntity();
        oldTaskEntity.setId(taskId);
        oldTaskEntity.setReminder(reminder);

        TaskEntity newTaskEntity = new TaskEntity();
        newTaskEntity.setId(taskId);
        newTaskEntity.setReminder(reminder);

        when(taskRepository.findById(taskId)).
                thenReturn(Optional.of(oldTaskEntity)).
                thenReturn(Optional.of(newTaskEntity));

        when(taskMapper.taskToTaskEntity(any())).
                thenReturn(newTaskEntity);
        when(taskRepository.save(any(TaskEntity.class))).
                thenReturn(newTaskEntity);

        TaskRequest request = new TaskRequest(
                null,
                "title",
                "description",
                reminder,
                null,
                "ON_HOLD"
        );

        taskService.updateTask(taskId, request);

        verify(emailSchedulerService, never()).scheduleEmail(any(), any());
        verify(emailSchedulerService, never()).deleteEmailSchedule(any());
        verify(emailSchedulerService, never()).updateEmailScheduleTime(any(), any());
    }
}