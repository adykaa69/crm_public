package hu.bhr.crm.validation.validator;

import hu.bhr.crm.validation.annotation.AtLeastOneFieldRequired;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

public class AtLeastOneFieldRequiredValidator implements ConstraintValidator<AtLeastOneFieldRequired, Object> {

    private static final Logger log = LoggerFactory.getLogger(AtLeastOneFieldRequiredValidator.class);
    private String message;
    private String[] fields;

    @Override
    public void initialize(AtLeastOneFieldRequired constraintAnnotation) {
        this.message = constraintAnnotation.message();
        this.fields = constraintAnnotation.fields();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        for (String fieldName : fields) {
            try {
                Field field = value.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                Object fieldValue = field.get(value);
                if (fieldValue != null && !fieldValue.toString().isBlank()) {
                    return true;
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                log.error("Validation failed: field '{}' not found or not accessible", fieldName, e);
            }
        }

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
        return false;
    }
}

