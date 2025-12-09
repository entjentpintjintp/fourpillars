package com.kolloseum.fourpillars.interfaces.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthWithdrawRequest {

    @NotBlank(message = "Access token is required")
    private final String accessToken;

    @NotBlank(message = "Refresh token is required")
    private final String refreshToken;

    public static AuthWithdrawRequest of(String accessToken, String refreshToken) {
        return new AuthWithdrawRequest(accessToken, refreshToken);
    }

}
