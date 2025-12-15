package com.sisima.api.domain.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sisima.api.config.JwtUtil;
import com.sisima.api.domain.akun.Akun;
import com.sisima.api.domain.akun.AkunRepository;
import com.sisima.api.domain.auth.model.AuthRequest;
import com.sisima.api.domain.auth.model.AuthResponse;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class AuthService {

    private final AkunRepository akunRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthResponse authentication(AuthRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();

        Akun akun = akunRepository.findDetailByEmail(email).orElse(null);
        if (akun == null) {
            return new AuthResponse("invalid");
        }

        boolean isPasswordMatch = passwordEncoder.matches(password, akun.getPassword());
        if (!isPasswordMatch) {
            return new AuthResponse("invalid");
        }

        return new AuthResponse("success");
    }

    public String authenticateAndGetToken(AuthRequest request) {
        Akun akun = akunRepository.findDetailByEmail(request.getEmail())
                .orElse(null);

        if (akun == null) return null;

        if (!passwordEncoder.matches(request.getPassword(), akun.getPassword()))
            return null;

        return jwtUtil.generateToken(akun);
    }
    
}
