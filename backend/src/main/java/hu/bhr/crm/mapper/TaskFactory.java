package hu.bhr.crm.mapper;

import hu.bhr.crm.controller.dto.TaskRequest;
import hu.bhr.crm.model.Customer;
import hu.bhr.crm.model.Task;
import hu.bhr.crm.model.TaskStatus;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.UUID;

public class TaskFactory {

    /**
     * Builds a Task from a TaskRequest.
     *
     * @param taskRequest the data transfer object containing the new task details
     * @return one built task in a {@link Task}
     */
    public static Task createTask(TaskRequest taskRequest, Customer customer) {
        return Task.builder()
                .id(UUID.randomUUID())
                .customer(customer)
                .title(taskRequest.title())
                .description(taskRequest.description())
                .reminder(toInstant(taskRequest.reminder()))
                .dueDate(toInstant(taskRequest.dueDate()))
                .status(StringUtils.isBlank(taskRequest.status())
                        ? TaskStatus.OPEN
                        : convertStatus(taskRequest.status()))
                .build();
    }

    public static Task createTaskWithId(UUID id, TaskRequest taskRequest, Customer customer) {
        return Task.builder()
                .id(id)
                .customer(customer)
                .title(taskRequest.title())
                .description(taskRequest.description())
                .reminder(toInstant(taskRequest.reminder()))
                .dueDate(toInstant(taskRequest.dueDate()))
                .status(StringUtils.isBlank(taskRequest.status())
                        ? TaskStatus.OPEN
                        : convertStatus(taskRequest.status()))
                .build();
    }

    private static TaskStatus convertStatus(String status) {
        return TaskStatus.fromString(status);
    }

    private static Instant toInstant(ZonedDateTime zonedDateTime) {
        return zonedDateTime != null ? zonedDateTime.toInstant() : null;
    }
}
