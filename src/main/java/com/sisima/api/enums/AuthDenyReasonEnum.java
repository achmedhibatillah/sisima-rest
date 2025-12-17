package com.sisima.api.enums;

public enum AuthDenyReasonEnum {
    MISSING_TOKEN("Authentication token is required"),
    INVALID_TOKEN("Invalid authentication token"),
    TOKEN_EXPIRED("Authentication token expired"),
    NO_ACCESS("You are not allowed to access this resource");

    private final String message;

    AuthDenyReasonEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
