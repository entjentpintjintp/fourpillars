package com.kolloseum.fourpillars.application.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthTokenInput {

    private final String accessToken;
    private final String refreshToken;

    public static AuthTokenInput of(String accessToken, String refreshToken) {
        return new AuthTokenInput(accessToken, refreshToken);
    }
}
