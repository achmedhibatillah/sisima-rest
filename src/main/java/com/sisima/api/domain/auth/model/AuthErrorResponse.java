package com.sisima.api.domain.auth.model;

import com.sisima.api.enums.AuthDenyReasonEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthErrorResponse {

    private String status;   // unauthorization | forbidden
    private String code;     // MISSING_TOKEN | INVALID_TOKEN | NO_ACCESS | ...
    private String message;

    public static AuthErrorResponse of(AuthDenyReasonEnum reason) {
        return switch (reason) {
            case MISSING_TOKEN, INVALID_TOKEN, TOKEN_EXPIRED ->
                new AuthErrorResponse(
                    "unauthorization",
                    reason.name(),
                    reason.getMessage()
                );
            case NO_ACCESS ->
                new AuthErrorResponse(
                    "forbidden",
                    reason.name(),
                    reason.getMessage()
                );
        };
    }
}
