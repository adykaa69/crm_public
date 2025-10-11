package hu.bhr.crm.validation.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NotPastValidatorTest {

    private NotPastValidator underTest;
    private ZonedDateTime fixedNow;

    @BeforeEach
    void setUp() {
        fixedNow = ZonedDateTime.parse("2025-09-01T12:00:00Z");
        Clock fixedClock = Clock.fixed(fixedNow.toInstant(), fixedNow.getZone());
        underTest = new NotPastValidator(fixedClock);
    }

    @Test
    void shouldReturnTrueWhenValueIsNull() {
        assertTrue(underTest.isValid(null,null));
    }

    @Test
    void shouldReturnFalseWhenDateIsInThePast() {
        ZonedDateTime past = fixedNow.minusSeconds(1);
        assertFalse(underTest.isValid(past, null));
    }

    @Test
    void shouldReturnFalseWhenDateIsNow() {
        ZonedDateTime now = fixedNow;
        assertFalse(underTest.isValid(now, null));
    }

    @Test
    void shouldReturnTrueWhenDateIsInTheFuture() {
        ZonedDateTime future = fixedNow.plusSeconds(1);
        assertTrue(underTest.isValid(future, null));
    }

    @Test
    void shouldReturnFalseWhenSetDateIsInThePast() {
        ZonedDateTime past = ZonedDateTime.parse("2025-08-31T18:00:00Z");
        assertFalse(underTest.isValid(past, null));
    }

    @Test
    void shouldReturnFalseWhenSetDateIsInThePastOnTheSameDay() {
        ZonedDateTime past = ZonedDateTime.parse("2025-09-01T11:59:59Z");
        assertFalse(underTest.isValid(past, null));
    }

    @Test
    void shouldReturnFalseWhenSetDateIsNow() {
        ZonedDateTime now = ZonedDateTime.parse("2025-09-01T12:00:00Z");
        assertFalse(underTest.isValid(now, null));
    }

    @Test
    void shouldReturnTrueWhenSetDateIsInTheFuture() {
        ZonedDateTime future = ZonedDateTime.parse("2025-10-01T12:00:00Z");
        assertTrue(underTest.isValid(future, null));
    }

    @Test
    void shouldReturnTrueWhenSetDateIsInTheFutureOnTheSameDay() {
        ZonedDateTime future = ZonedDateTime.parse("2025-09-01T12:00:01Z");
        assertTrue(underTest.isValid(future, null));
    }
}
