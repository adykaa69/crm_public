package hu.bhr.crm.service;

import hu.bhr.crm.controller.dto.TaskRequest;
import hu.bhr.crm.exception.TaskNotFoundException;
import hu.bhr.crm.mapper.TaskFactory;
import hu.bhr.crm.mapper.TaskMapper;
import hu.bhr.crm.model.Customer;
import hu.bhr.crm.model.Task;
import hu.bhr.crm.model.TaskStatus;
import hu.bhr.crm.repository.TaskRepository;
import hu.bhr.crm.repository.entity.TaskEntity;
import hu.bhr.crm.scheduler.EmailSchedulerService;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Service class for managing Task entities.
 * <p>
 * This service handles the lifecycle of tasks, including creation, updates, deletion,
 * and retrieval. It integrates with {@link EmailSchedulerService} to manage email reminders
 * associated with tasks and handles the synchronization of task status with completion timestamps.
 * </p>
 */
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final CustomerService customerService;
    private final EmailSchedulerService emailSchedulerService;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper, CustomerService customerService,
                       EmailSchedulerService emailSchedulerService)  {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.customerService = customerService;
        this.emailSchedulerService = emailSchedulerService;
    }

    /**
     * Retrieves a single task by its unique identifier.
     *
     * @param id the unique UUID of the task to retrieve
     * @return the {@link Task} domain object corresponding to the found entity
     * @throws TaskNotFoundException if no task exists with the given ID
     */
    public Task getTaskById(UUID id) {
        TaskEntity taskEntity = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));
        return taskMapper.taskEntityToTask(taskEntity);
    }

    /**
     * Retrieves all tasks currently stored in the database.
     *
     * @return a {@link List} of all {@link Task} domain objects
     */
    public List<Task> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(taskMapper::taskEntityToTask)
                .toList();
    }

    /**
     * Retrieves all tasks associated with a specific customer.
     *
     * @param customerId the unique UUID of the customer
     * @return a {@link List} of {@link Task} objects linked to the specified customer
     */
    public List<Task> getAllTasksByCustomerId(UUID customerId) {
        return taskRepository.findAllByCustomerId(customerId).stream()
                .map(taskMapper::taskEntityToTask)
                .toList();
    }

    /**
     * Creates and persists a new task in the database.
     * <p>
     * This method performs several side effects:
     * <ul>
     * <li>Validates the customer existence if a customer ID is provided.</li>
     * <li>Schedules an email reminder via {@link EmailSchedulerService} if a reminder date is set.</li>
     * <li>Automatically sets the 'completedAt' timestamp if the initial status is {@link TaskStatus#COMPLETED}.</li>
     * </ul>
     * </p>
     *
     * @param taskRequest the DTO containing the details for the new task
     * @return the persisted {@link Task} domain object with its generated ID
     * @throws hu.bhr.crm.exception.CustomerNotFoundException if the referenced customer ID does not exist
     * @throws hu.bhr.crm.exception.EmailScheduleException if scheduling the email reminder fails
     */
    public Task saveTask(TaskRequest taskRequest) {
        TaskEntity savedTaskEntity = buildAndSaveTaskEntity(null, taskRequest);
        scheduleEmailIfReminderExists(savedTaskEntity, taskRequest);

        return taskMapper.taskEntityToTask(savedTaskEntity);
    }

    /**
     * Deletes a task by its unique identifier.
     * <p>
     * This method also cleans up any associated scheduled email reminders,
     * before deleting the task entity from the database.
     * </p>
     *
     * @param id the unique UUID of the task to delete
     * @return the {@link Task} domain object that was deleted
     * @throws TaskNotFoundException if the task with the given ID does not exist
     */
    public Task deleteTask(UUID id) {
        TaskEntity taskEntity = findTaskEntity(id);

        emailSchedulerService.deleteEmailSchedule(id);

        Task deletedTask = taskMapper.taskEntityToTask(taskEntity);
        taskRepository.deleteById(id);
        return deletedTask;
    }

    /**
     * Updates an existing task identified by its ID.
     * <p>
     * This method synchronizes the task state with external systems:
     * <ul>
     * <li>It updates, creates, or deletes the email reminder schedule based on changes to the reminder date.</li>
     * <li>It manages the 'completedAt' timestamp if the status transitions to or is maintained as {@link TaskStatus#COMPLETED}.</li>
     * </ul>
     * </p>
     *
     * @param id the unique UUID of the task to update
     * @param taskRequest the DTO containing the updated fields
     * @return the updated {@link Task} domain object
     * @throws TaskNotFoundException if the task with the given ID does not exist
     * @throws hu.bhr.crm.exception.CustomerNotFoundException if the referenced customer ID in the update does not exist
     */
    public Task updateTask(UUID id, TaskRequest taskRequest) {
        TaskEntity oldTaskEntity = findTaskEntity(id);
        TaskEntity updatedTaskEntity = buildAndSaveTaskEntity(id, taskRequest);
        handleReminderUpdate(oldTaskEntity.getReminder(), updatedTaskEntity.getReminder(), id);

        return taskMapper.taskEntityToTask(updatedTaskEntity);
    }

    /**
     * Builds and saves the TaskEntity, handling customer association and completion timestamps.
     *
     * @param id the ID of the task (null for new tasks)
     * @param taskRequest the request data
     * @return the saved TaskEntity
     */
    private TaskEntity buildAndSaveTaskEntity(UUID id, TaskRequest taskRequest) {
        Customer customer = null;
        if (taskRequest.customerId() != null) {
            customer = customerService.getCustomerById(taskRequest.customerId());
        }

        Task task = (id == null)
                ? TaskFactory.createTask(taskRequest, customer)
                : TaskFactory.createTaskWithId(id, taskRequest, customer);

        TaskEntity taskEntityToSave = taskMapper.taskToTaskEntity(task);
        TaskEntity savedTaskEntity = taskRepository.save(taskEntityToSave);
        setCompletedAtIfCompleted(savedTaskEntity);

        return savedTaskEntity;
    }

    /**
     * Sets the completedAt timestamp in the database if the task status is COMPLETED.
     * This uses a native query to ensure the timestamp is generated by the database.
     *
     * @param taskEntity the entity to check and update
     */
    private void setCompletedAtIfCompleted(TaskEntity taskEntity) {
        if (taskEntity.getStatus() == TaskStatus.COMPLETED) {
            ZonedDateTime completedAt = taskRepository.setCompletedAtIfCompleted(taskEntity.getId(), TaskStatus.COMPLETED.name())
                    .atZone(ZoneId.systemDefault());
            taskEntity.setCompletedAt(completedAt);
        }
    }

    /**
     * Detaches a customer from all their associated tasks.
     * <p>
     * This method is typically called when a customer is deleted. It effectively "orphans" the tasks
     * by setting their customer reference to null. To preserve context, a note is appended to the
     * task description indicating that the related customer has been deleted.
     * </p>
     *
     * @param customerId the unique UUID of the customer being removed
     */
    public void detachCustomerFromTasks(UUID customerId) {
        List<TaskEntity> relatedTasks = taskRepository.findAllByCustomerId(customerId);
        for (TaskEntity task : relatedTasks) {
            task.setCustomer(null);
            String originalDescription = task.getDescription();
            String note = "The related customer has been deleted.";
            task.setDescription((originalDescription == null || originalDescription.isBlank())
                    ? note
                    : originalDescription + " " + note);
        }

        taskRepository.saveAll(relatedTasks);
    }

    /**
     * Helper method to find a task entity by ID or throw an exception if not found.
     *
     * @param taskId the UUID of the task
     * @return the found {@link TaskEntity}
     * @throws TaskNotFoundException if the task does not exist
     */
    private TaskEntity findTaskEntity(UUID taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));
    }

    /**
     * Schedules an email reminder if the task request contains a reminder date.
     *
     * @param taskEntity the persisted task entity (needed for the ID)
     * @param taskRequest the request object containing the potential reminder date
     */
    private void scheduleEmailIfReminderExists(TaskEntity taskEntity, TaskRequest taskRequest) {
        if (taskRequest.reminder() != null) {
            emailSchedulerService.scheduleEmail(
                    taskEntity.getId(),
                    taskRequest.reminder()
            );
        }
    }

    /**
     * Determines the appropriate action for the email scheduler based on the change in reminder dates.
     * <p>
     * Logic:
     * <ul>
     * <li>If a reminder is added (was null, now set), schedule a new job.</li>
     * <li>If a reminder is removed (was set, now null), delete the existing job.</li>
     * <li>If the reminder time has changed, update the existing job.</li>
     * <li>If both are null or equal, do nothing.</li>
     * </ul>
     * </p>
     *
     * @param oldReminder the previous reminder time (from the DB)
     * @param newReminder the new reminder time (from the request)
     * @param taskId the ID of the task
     */
    private void handleReminderUpdate(ZonedDateTime oldReminder, ZonedDateTime newReminder, UUID taskId) {
        if (oldReminder == null && newReminder != null) {
            emailSchedulerService.scheduleEmail(taskId, newReminder);

        } else if (oldReminder != null && newReminder == null) {
            emailSchedulerService.deleteEmailSchedule(taskId);

        } else if (!Objects.equals(oldReminder, newReminder)) {
            emailSchedulerService.updateEmailScheduleTime(taskId, newReminder);
        }
    }
}

