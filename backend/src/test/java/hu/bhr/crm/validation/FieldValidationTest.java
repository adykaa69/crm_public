package hu.bhr.crm.validation;

import hu.bhr.crm.exception.MissingFieldException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FieldValidationTest {

    @Test
    void shouldSuccessWhenFieldPresent() {
        assertDoesNotThrow(() -> FieldValidation.validateNotEmpty("John Doe", "name"));
    }

    @Test
    void shouldFailWhenFieldEmpty() {
        assertThrows(MissingFieldException.class, () -> FieldValidation.validateNotEmpty("", "name"));
    }

    @Test
    void shouldFailWhenFieldNull() {
        assertThrows(MissingFieldException.class, () -> FieldValidation.validateNotEmpty(null, "name"));
    }

    @Test
    void shouldFailWhenFieldWhitespace() {
        assertThrows(MissingFieldException.class, () -> FieldValidation.validateNotEmpty("   ", "name"));
    }


    @Test
    void shouldSuccessWhenFirstFieldPresentSecondEmpty() {
        assertDoesNotThrow(() -> FieldValidation.validateAtLeastOneIsNotEmpty("John", "First Name", "", "Nickname"));
    }

    @Test
    void shouldSuccessWhenFirstFieldNullSecondPresent() {
        assertDoesNotThrow(() -> FieldValidation.validateAtLeastOneIsNotEmpty(null, "First Name", "Johnny", "Nickname"));
    }

    @Test
    void shouldSuccessWhenBothFieldsPresent() {
        assertDoesNotThrow(() -> FieldValidation.validateAtLeastOneIsNotEmpty("John", "First Name", "Johnny", "Nickname"));
    }

    @Test
    void shouldFailWhenBothFieldsEmpty() {
        assertThrows(MissingFieldException.class, () -> FieldValidation.validateAtLeastOneIsNotEmpty("", "First Name", "", "Nickname"));
    }

    @Test
    void shouldFailWhenBothFieldsNull() {
        assertThrows(MissingFieldException.class, () -> FieldValidation.validateAtLeastOneIsNotEmpty(null, "First Name", null, "Nickname"));
    }
}