package hu.bhr.crm.validation.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class EmailValidatorTest {

    private EmailValidator underTest;

    @BeforeEach
    void setUp() {
        underTest = new EmailValidator();
    }

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
    void shouldReturnTrueWhenEmailIsValid(String email) {
        assertTrue(underTest.isValid(email, null));
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
    void shouldReturnFalseWhenEmailIsInvalid(String email) {
        assertFalse(underTest.isValid(email, null));
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
            "user,name@domain.com",
            "user\"name@domain.com",
            "user\\name@domain.com",
            "user`name@domain.com"
    })
    void shouldReturnFalseWhenEmailContainsDisallowedSpecialCharacters(String email) {
        assertFalse(underTest.isValid(email, null));
    }

    @Test
    void shouldReturnTrueWhenLocalPartLengthIs64() {
        String localPart = "a".repeat(64);
        String email = localPart + "@domain.com";
        assertTrue(underTest.isValid(email, null));
    }

    @Test
    void shouldReturnFalseWhenLocalPartLengthExceeds64() {
        String localPart = "a".repeat(65);
        String email = localPart + "@domain.com";
        assertFalse(underTest.isValid(email, null));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {
            "",
            "   ",
            "\t",
            "\n"
    })
    void shouldReturnTrueWhenEmailIsNullOrBlank(String email) {
        assertTrue(underTest.isValid(email, null));
    }
}