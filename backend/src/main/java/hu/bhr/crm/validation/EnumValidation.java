package hu.bhr.crm.validation;

import hu.bhr.crm.exception.InvalidStatusException;

import java.util.Arrays;

public class EnumValidation {

    public static <E extends Enum<E>> void validateEnum(Class<E> enumClass, String value) {

        boolean isValid = Arrays.stream(enumClass.getEnumConstants())
                .anyMatch(enumValue -> enumValue.name().equals(value));

        if (!isValid) {
            throw new InvalidStatusException("Invalid value for enum " + enumClass.getSimpleName() + ": " + value);
        }
    }
}
