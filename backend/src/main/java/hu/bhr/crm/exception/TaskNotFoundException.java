package hu.bhr.crm.exception;

import java.util.UUID;

public class TaskNotFoundException extends ResourceNotFoundException {

    public TaskNotFoundException(UUID id) {
        super("Task", id);
    }

    public TaskNotFoundException(String message) {
        super(message);
    }
}