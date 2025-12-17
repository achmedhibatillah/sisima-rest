package com.sisima.api.domain.auth;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sisima.api.config.JwtUtil;
import com.sisima.api.domain.akun.Akun;
import com.sisima.api.domain.akun.AkunRepository;
import com.sisima.api.domain.auth.model.AuthRequest;
import com.sisima.api.domain.auth.model.AuthResponse;
import com.sisima.api.domain.auth.model.LoginResult;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class AuthService {

    private final AkunRepository akunRepository;
    private final RefreshTokenRepository refreshTokenRepository;
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

    // public String authenticateAndGetToken(AuthRequest request) {
    //     Akun akun = akunRepository.findDetailByEmail(request.getEmail())
    //             .orElse(null);

    //     if (akun == null) return null;

    //     if (!passwordEncoder.matches(request.getPassword(), akun.getPassword()))
    //         return null;

    //     return jwtUtil.generateToken(akun);
    // }

    public LoginResult login(AuthRequest request) {
        Akun akun = akunRepository.findDetailByEmail(request.getEmail())
            .orElse(null);

        if (akun == null ||
            !passwordEncoder.matches(request.getPassword(), akun.getPassword()))
            return null;

        String accessToken = jwtUtil.generateToken(akun);

        String refreshToken = generateRefreshToken(akun);

        RefreshToken entity = new RefreshToken(
            null,
            DigestUtils.sha256Hex(refreshToken),
            akun.getPublicId(),
            Instant.now().plus(7, ChronoUnit.DAYS),
            false,
            null
        );

        refreshTokenRepository.save(entity);

        return new LoginResult(accessToken, refreshToken);
    }

    public String refreshAccessToken(String refreshTokenRaw) {
        String hash = DigestUtils.sha256Hex(refreshTokenRaw);

        RefreshToken token = refreshTokenRepository
            .findByTokenHashAndRevokedFalse(hash)
            .orElse(null);

        if (token == null || token.getExpiresAt().isBefore(Instant.now()))
            return null;

        Akun akun = akunRepository.findDetailByPublicId(token.getOwnerPublicId())
            .orElse(null);

        if (akun == null) return null;

        return jwtUtil.generateToken(akun);
    }

    public String generateRefreshToken(Akun akun) {
        return UUID.randomUUID().toString() + UUID.randomUUID();
    }
    
}
