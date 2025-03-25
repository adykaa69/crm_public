package hu.bhr.crm.validation;

import hu.bhr.crm.exception.InvalidEmailException;

public class EmailValidation {

    private static final String EMAIL_REGEX = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    private EmailValidation() {
    }

    public static void validate(String email) {
        if (email != null && !email.isBlank() && !email.matches(EMAIL_REGEX)) {
            throw new InvalidEmailException("Invalid email format");
        }
    }
}
