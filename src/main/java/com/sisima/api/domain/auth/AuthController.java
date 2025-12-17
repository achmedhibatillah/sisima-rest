package com.sisima.api.domain.auth;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sisima.api.domain.auth.model.AuthRequest;
import com.sisima.api.domain.auth.model.AuthResponse;
import com.sisima.api.domain.auth.model.LoginResult;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;

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
    // @PostMapping("/login")
    // public ResponseEntity<AuthResponse> login(
    //         @RequestBody @Valid AuthRequest request
    // ) {
    //     String token = authService.authenticateAndGetToken(request);

    //     if (token == null) {
    //         return 
    //             ResponseEntity
    //                 .status(401)
    //                 .body(new AuthResponse("invalid"));
    //     }

    //     ResponseCookie cookie = 
    //         ResponseCookie
    //             .from("access_token", token)
    //             .httpOnly(true)
    //             .secure(false)
    //             .sameSite("Lax")
    //             .path("/")
    //             .maxAge(Duration.ofHours(1))
    //             .build();

    //     return 
    //         ResponseEntity
    //             .ok()
    //             .header(HttpHeaders.SET_COOKIE, cookie.toString())
    //             .body(new AuthResponse("success"));
    // }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody @Valid AuthRequest request
    ) {
        LoginResult result = authService.login(request);
        if (result == null)
            return ResponseEntity.status(401).body(Map.of("code", "INVALID_AUTHENTICATION"));

        ResponseCookie refreshCookie =
            ResponseCookie.from("refresh_token", result.refreshToken())
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/auth/refresh")
                .maxAge(Duration.ofDays(7))
                .build();

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
            .body(new AuthResponse(result.accessToken()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null)
            return ResponseEntity.status(401).build();

        Cookie cookie = Arrays.stream(cookies)
            .filter(c -> c.getName().equals("refresh_token"))
            .findFirst()
            .orElse(null);

        if (cookie == null)
            return ResponseEntity.status(401).build();

        String newAccessToken =
            authService.refreshAccessToken(cookie.getValue());

        if (newAccessToken == null)
            return ResponseEntity.status(401).build();

        return ResponseEntity.ok(new AuthResponse(newAccessToken));
    }

}
