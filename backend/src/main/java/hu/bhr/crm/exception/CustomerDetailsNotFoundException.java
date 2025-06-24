package hu.bhr.crm.exception;

public class CustomerDetailsNotFoundException extends RuntimeException {
    public CustomerDetailsNotFoundException(String message) {
        super(message);
    }
}
