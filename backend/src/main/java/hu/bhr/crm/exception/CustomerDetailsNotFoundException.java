package hu.bhr.crm.exception;

import java.util.UUID;

public class CustomerDetailsNotFoundException extends ResourceNotFoundException {
    public CustomerDetailsNotFoundException(UUID id) {
        super("Customer details", id);
    }

    public CustomerDetailsNotFoundException(String message) {
        super(message);
    }
}