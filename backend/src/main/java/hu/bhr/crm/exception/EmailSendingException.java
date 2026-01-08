package hu.bhr.crm.exception;

public class EmailSendingException extends InfrastructureException {
    public EmailSendingException(String message, Throwable cause) {
        super(message, cause);
    }
}
