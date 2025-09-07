package hu.bhr.crm.validation.validator;

import hu.bhr.crm.validation.annotation.NotPast;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Clock;
import java.time.ZonedDateTime;

public class NotPastValidator implements ConstraintValidator<NotPast, ZonedDateTime> {

    private final Clock clock;

    public NotPastValidator() {
        this.clock = Clock.systemDefaultZone();
    }

    public NotPastValidator(Clock clock) {
        this.clock = clock;
    }

    @Override
    public boolean isValid(ZonedDateTime value, ConstraintValidatorContext context) {
        return value == null || value.isAfter(ZonedDateTime.now(clock));
    }
}
