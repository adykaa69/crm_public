package hu.bhr.crm.exception;

public enum ErrorCode {
    CUSTOMER_NOT_FOUND("CUSTOMER.NOT_FOUND"),
    EMAIL_INVALID("EMAIL.INVALID"),
    MISSING_FIELD("FIELD.MISSING"),
    INTERNAL_SERVER_ERROR("INTERNAL.SERVER_ERROR");

    private final String code;

    ErrorCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
