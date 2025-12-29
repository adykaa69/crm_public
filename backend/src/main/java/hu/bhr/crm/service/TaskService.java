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

import java.time.Instant;
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
        Customer customer = resolveCustomer(taskRequest.customerId());

        Task task = TaskFactory.createTask(taskRequest, customer);
        TaskEntity taskEntity = taskMapper.taskToTaskEntity(task);

        Instant completedAt = determineCompletedAt(null, taskEntity.getStatus());
        taskEntity.setCompletedAt(completedAt);

        TaskEntity savedTaskEntity = taskRepository.save(taskEntity);
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

        Customer customer = resolveCustomer(taskRequest.customerId());

        Task task = TaskFactory.createTaskWithId(id, taskRequest, customer);
        TaskEntity newTaskEntity = taskMapper.taskToTaskEntity(task);

        Instant completedAt = determineCompletedAt(oldTaskEntity, newTaskEntity.getStatus());
        newTaskEntity.setCompletedAt(completedAt);

        TaskEntity updatedTaskEntity = taskRepository.save(newTaskEntity);
        handleReminderUpdate(oldTaskEntity.getReminder(), updatedTaskEntity.getReminder(), id);

        return taskMapper.taskEntityToTask(updatedTaskEntity);
    }

    /**
     * Determines the completion timestamp based on the previous state and new status.
     * <p>
     * The logic ensures idempotency and correctness:
     * <ul>
     * <li> If the new status is NOT completed, the timestamp is reset to null.</li>
     * <li> If the task was ALREADY completed (in existingEntity), the original timestamp is preserved.</li>
     * <li> If the task is newly transitioning to completed, a new System time is generated.</li>
     * </ul>
     * </p>
     *
     * @param existingTaskEntity the current state of the task in the DB (null for new tasks)
     * @param newTaskStatus      the target status of the task
     * @return the calculated {@link Instant} for completion, or null
     */
    private Instant determineCompletedAt(TaskEntity existingTaskEntity, TaskStatus newTaskStatus) {
        if (newTaskStatus != TaskStatus.COMPLETED) {
            return null;
        }

        if (existingTaskEntity != null
                && existingTaskEntity.getCompletedAt() != null
                && existingTaskEntity.getStatus() == TaskStatus.COMPLETED) {

            return existingTaskEntity.getCompletedAt();
        }

        return Instant.now();
    }

    /**
     * Resolves the Customer entity based on the provided ID.
     * <p>
     * Retrieves the full Customer object from the customer service.
     * If the ID is null, it returns null.
     * </p>
     *
     * @param customerId the UUID of the customer to resolve
     * @return the {@link Customer} domain object, or null if customerId is null
     * @throws hu.bhr.crm.exception.CustomerNotFoundException if the ID is provided but the customer does not exist
     */
    private Customer resolveCustomer(UUID customerId) {
        if (customerId == null) {
            return null;
        }
        return customerService.getCustomerById(customerId);
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
                    taskRequest.reminder().toInstant()
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
    private void handleReminderUpdate(Instant oldReminder, Instant newReminder, UUID taskId) {
        if (oldReminder == null && newReminder != null) {
            emailSchedulerService.scheduleEmail(taskId, newReminder);

        } else if (oldReminder != null && newReminder == null) {
            emailSchedulerService.deleteEmailSchedule(taskId);

        } else if (!Objects.equals(oldReminder, newReminder)) {
            emailSchedulerService.updateEmailScheduleTime(taskId, newReminder);
        }
    }
}

