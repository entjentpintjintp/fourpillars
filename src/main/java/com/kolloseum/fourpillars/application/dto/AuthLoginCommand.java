package com.kolloseum.fourpillars.application.dto;

import com.kolloseum.fourpillars.domain.model.enums.Provider;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthLoginCommand {

    private final String authorizationCode;
    private final String codeVerifier;
    private final String redirectUri;
    private final Provider provider;

    public static AuthLoginCommand of(String authorizationCode, String codeVerifier,
                                     String redirectUri, Provider provider) {
        return new AuthLoginCommand(authorizationCode, codeVerifier, redirectUri, provider);
    }

}
