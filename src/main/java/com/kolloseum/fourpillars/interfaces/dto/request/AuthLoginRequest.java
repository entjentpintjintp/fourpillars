package com.kolloseum.fourpillars.interfaces.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kolloseum.fourpillars.domain.model.enums.Provider;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthLoginRequest {
    @NotBlank(message = "Authorization code is required")
    private final String authorizationCode;

    @JsonProperty(defaultValue = "")
    private final String codeVerifier;

    @JsonProperty(defaultValue = "")
    private final String redirectUri;

    @NotNull(message = "Provider is required")
    private final Provider provider;

    public static AuthLoginRequest of(String authorizationCode, String codeVerifier,
            String redirectUri, Provider provider) {
        return new AuthLoginRequest(authorizationCode, codeVerifier, redirectUri, provider);
    }
}