package com.sisima.api.domain.auth;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sisima.api.domain.auth.model.AuthRequest;
import com.sisima.api.domain.auth.model.AuthResponse;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@AllArgsConstructor
@RestController
@RequestMapping("/login")
public class AuthController {

    private final AuthService authService;

    @GetMapping
    public ResponseEntity<?> login(@RequestParam @Valid AuthRequest request) {
        AuthResponse response = authService.authentication(request);
        return ResponseEntity.ok(response);
    }

}
