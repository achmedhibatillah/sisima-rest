package com.sisima.api.security;

import org.springframework.http.HttpStatus;

import com.sisima.api.enums.AuthDenyReasonEnum;

public record AuthDecision(
    boolean allowed,
    AuthDenyReasonEnum reason
) {
    public HttpStatus httpStatus() {
        return switch (reason) {
            case MISSING_TOKEN,
                 INVALID_TOKEN,
                 TOKEN_EXPIRED -> HttpStatus.UNAUTHORIZED;
            case NO_ACCESS -> HttpStatus.CREATED;
        };
    }
}
