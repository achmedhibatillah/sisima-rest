package com.sisima.api.domain.auth.model;

public record LoginResult(
    String accessToken,
    String refreshToken
) {}
