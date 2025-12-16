package com.sisima.api.domain.auth;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sisima.api.domain.auth.model.AuthRequest;
import com.sisima.api.domain.auth.model.AuthResponse;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import java.time.Duration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;


@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    // used - guest
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody @Valid AuthRequest request
    ) {
        String token = authService.authenticateAndGetToken(request);

        if (token == null) {
            return 
                ResponseEntity
                    .status(401)
                    .body(new AuthResponse("invalid"));
        }

        ResponseCookie cookie = 
            ResponseCookie
                .from("access_token", token)
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(Duration.ofHours(1))
                .build();

        return 
            ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new AuthResponse("success"));
    }

}
