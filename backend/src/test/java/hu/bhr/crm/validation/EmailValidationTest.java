package hu.bhr.crm.validation;

import hu.bhr.crm.exception.InvalidEmailException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmailValidationTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "username@domain.com",
            "user69@domain69.com",
            "user.name@domain.com",
            "user.name.surname@domain.com",
            "user-name@domain.com",
            "username@domain.co.in",
            "username@domain.travel",
            "user_name@domain.com",
            "USERNAME@DOMAIN.COM"
    })
    void testValidEmail(String email) {
        assertDoesNotThrow(() -> EmailValidation.validate(email));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "username.@domain.com",
            "user..name@domain.com",
            ".user.name@domain.com",
            "user-name@domain.com.",
            "username@.com",
            "username@domain.c",
            "user name@domain.com",
            " username@domain.com",
            "username@domain.com "
    })
    void testInvalidEmailStructure(String email) {
        assertThrows(InvalidEmailException.class, () -> EmailValidation.validate(email));
    }

    @Test
    void testLocalPartLengthBoundaryValid() {
        String localPart64 = "a".repeat(64);
        String validEmail = localPart64 + "@domain.com";
        assertDoesNotThrow(() -> EmailValidation.validate(validEmail));
    }

    @Test
    void testLocalPartLengthBoundaryInvalid() {
        String localPart65 = "a".repeat(65);
        String invalidEmail = localPart65 + "@domain.com";
        assertThrows(InvalidEmailException.class, () -> EmailValidation.validate(invalidEmail));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "username+name@domain.com",
            "user!name@domain.com",
            "user#name@domain.com",
            "user$name@domain.com",
            "user%name@domain.com",
            "user^name@domain.com",
            "user&name@domain.com",
            "user*name@domain.com",
            "user=name@domain.com",
            "user?name@domain.com",
            "user/name@domain.com",
            "user~name@domain.com",
            "user'name@domain.com",
            "user,name@domain.com"
    })
    void testEmailWithDisallowedSpecialCharacters(String email) {
        assertThrows(InvalidEmailException.class, () -> EmailValidation.validate(email));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "\t", "\n"})
    void testNullEmail(String email) {
        assertDoesNotThrow(() -> EmailValidation.validate(email));
    }

}