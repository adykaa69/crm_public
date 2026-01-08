package hu.bhr.crm.exception;

import java.util.UUID;

public class ResourceNotFoundException extends DomainException {
    protected ResourceNotFoundException(String message) {
        super(message);
    }

    protected ResourceNotFoundException(String resourceName, UUID id) {
        super(String.format("%s not found with id %s", resourceName, id));
    }
}
