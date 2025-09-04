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

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

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
     * Gets one task by its unique ID.
     *
     * @param id the unique ID of the requested task
     * @return a {@link Task} object corresponding to the given ID
     * @throws TaskNotFoundException if the task with the given ID does not exist (returns HTTP 404 Not Found)
     */
    public Task getTaskById(UUID id) {
        TaskEntity taskEntity = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));
        return taskMapper.taskEntityToTask(taskEntity);
    }

    /**
     * Gets all tasks.
     *
     * @return a list of {@link Task} objects
     */
    public List<Task> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(taskMapper::taskEntityToTask)
                .toList();
    }

    /**
     * Gets all tasks associated with a specific customer by their unique ID.
     *
     * @param customerId the unique ID of the customer whose tasks are requested
     * @return a list of {@link Task} objects corresponding to the given customer ID
     */
    public List<Task> getAllTasksByCustomerId(UUID customerId) {
        return taskRepository.findAllByCustomerId(customerId).stream()
                .map(taskMapper::taskEntityToTask)
                .toList();
    }

    /**
     * Saves a new task.
     * Validates the task's title and if a customer ID is provided, check if the customer exists.
     *
     * @param taskRequest the built task to be saved
     * @return the saved {@link Task} object
     * @throws hu.bhr.crm.exception.MissingFieldException if title is missing
     * @throws TaskNotFoundException if the task could not be retrieved
     */
    public Task saveTask(TaskRequest taskRequest) {
        Customer customer = null;
        if (taskRequest.customerId() != null) {
            customer = customerService.getCustomerById(taskRequest.customerId());
        }

        Task task = TaskFactory.createTask(taskRequest, customer);
        TaskEntity taskEntity = taskMapper.taskToTaskEntity(task);
        TaskEntity savedTaskEntity = taskRepository.save(taskEntity);

        setCompletedAtIfCompleted(savedTaskEntity);

        savedTaskEntity = taskRepository.findById(savedTaskEntity.getId())
                .orElseThrow(() -> new TaskNotFoundException("Failed to retrieve saved task"));

        scheduleEmailIfReminderExists(savedTaskEntity, taskRequest);

        return taskMapper.taskEntityToTask(savedTaskEntity);
    }

    public Task deleteTask(UUID id) {
        TaskEntity taskEntity = findTaskEntity(id);

        emailSchedulerService.deleteEmailSchedule(id);

        Task deletedTask = taskMapper.taskEntityToTask(taskEntity);
        taskRepository.deleteById(id);
        return deletedTask;
    }

    public Task updateTask(UUID id, TaskRequest taskRequest) {
        TaskEntity oldTaskEntity = findTaskEntity(id);

        Customer customer = null;
        if (taskRequest.customerId() != null) {
            customer = customerService.getCustomerById(taskRequest.customerId());
        }

        Task updatedTask = TaskFactory.createTaskWithId(id, taskRequest, customer);

        TaskEntity updatedTaskEntity = taskMapper.taskToTaskEntity(updatedTask);
        updatedTaskEntity = taskRepository.save(updatedTaskEntity);

        handleReminderUpdate(oldTaskEntity.getReminder(), updatedTaskEntity.getReminder(), id);

        setCompletedAtIfCompleted(updatedTaskEntity);
        updatedTaskEntity = taskRepository.findById(updatedTaskEntity.getId())
                .orElseThrow(() -> new TaskNotFoundException("Failed to retrieve saved task"));
        return taskMapper.taskEntityToTask(updatedTaskEntity);
    }

    private void setCompletedAtIfCompleted(TaskEntity taskEntity) {
        if (taskEntity.getStatus() == TaskStatus.COMPLETED) {
            taskRepository.setCompletedAtIfCompleted(taskEntity.getId(), TaskStatus.COMPLETED);
        }
    }

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

    private TaskEntity findTaskEntity(UUID taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));
    }

    private void scheduleEmailIfReminderExists(TaskEntity taskEntity, TaskRequest taskRequest) {
        if (taskRequest.reminder() != null) {
            emailSchedulerService.scheduleEmail(
                    taskEntity.getId(),
                    taskRequest.reminder()
            );
        }
    }

    private void handleReminderUpdate(Timestamp oldReminder, Timestamp newReminder, UUID taskId) {
        if (oldReminder == null) {
            emailSchedulerService.scheduleEmail(taskId, newReminder);

        } else if (newReminder == null) {
            emailSchedulerService.deleteEmailSchedule(taskId);

        } else if (!Objects.equals(oldReminder, newReminder)) {
            emailSchedulerService.updateEmailScheduleTime(taskId, newReminder);
        }
    }
}

