package hu.bhr.crm.exception;

import java.util.UUID;

public class CustomerNotFoundException extends ResourceNotFoundException {
    public CustomerNotFoundException(UUID id) {
        super("Customer", id);
    }

    public CustomerNotFoundException(String message) {
        super(message);
    }
}
