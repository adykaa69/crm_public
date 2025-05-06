package hu.bhr.crm.validation;

import hu.bhr.crm.exception.MissingFieldException;

public class FieldValidation {

    private FieldValidation() {

    }

    public static void validateNotEmpty(String field, String fieldName) {
        if (field == null || field.isBlank()) {
            throw new MissingFieldException(fieldName + " is required.");
        }
    }

    public static void validateAtLeastOneIsNotEmpty(String firstField, String firstFieldName, String secondField, String secondFieldName) {
        if ((firstField == null || firstField.isBlank()) && (secondField == null || secondField.isBlank())) {
            throw new MissingFieldException("At least one of " + firstFieldName + " or " + secondFieldName + " is required.");
        }
    }
}
