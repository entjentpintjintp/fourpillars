package com.kolloseum.fourpillars.interfaces.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthRefreshRequest {
    @NotBlank(message = "Refresh token is required")
    private final String refreshToken;
    
    public static AuthRefreshRequest of(String refreshToken) {
        return new AuthRefreshRequest(refreshToken);
    }
}