package hu.bhr.crm.service;

import hu.bhr.crm.controller.dto.TaskRequest;
import hu.bhr.crm.exception.CustomerNotFoundException;
import hu.bhr.crm.exception.TaskNotFoundException;
import hu.bhr.crm.mapper.TaskFactory;
import hu.bhr.crm.mapper.TaskMapper;
import hu.bhr.crm.model.Customer;
import hu.bhr.crm.model.Task;
import hu.bhr.crm.model.TaskStatus;
import hu.bhr.crm.repository.TaskRepository;
import hu.bhr.crm.repository.entity.CustomerEntity;
import hu.bhr.crm.repository.entity.TaskEntity;
import hu.bhr.crm.validation.FieldValidation;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final CustomerService customerService;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper, CustomerService customerService) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.customerService = customerService;
    }

    /**
     * Saves a new task for a customer.
     * Validates the task's title and checks if the customer exists.
     *
     * @param taskRequest the built task to be saved
     * @return the saved {@link Task} object
     * @throws hu.bhr.crm.exception.MissingFieldException if title is missing
     * @throws TaskNotFoundException if the task could not be retrieved
     */
    public Task saveTask(TaskRequest taskRequest) {
        FieldValidation.validateNotEmpty(taskRequest.title(), "Title");

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
        return taskMapper.taskEntityToTask(savedTaskEntity);
    }

    private void setCompletedAtIfCompleted(TaskEntity taskEntity) {
        if (taskEntity.getStatus() == TaskStatus.COMPLETED) {
            taskRepository.setCompletedAtIfCompleted(taskEntity.getId(), TaskStatus.COMPLETED);
        }
    }
}

