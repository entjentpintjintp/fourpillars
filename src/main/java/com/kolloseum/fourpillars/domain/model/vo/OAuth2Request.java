package com.kolloseum.fourpillars.domain.model.vo;

import com.kolloseum.fourpillars.domain.model.enums.Provider;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@ToString
public class OAuth2Request {
    
    private final String authorizationCode;
    private final String codeVerifier;
    private final String redirectUri;
    private final Provider provider;

    public static OAuth2Request of(String authorizationCode, String codeVerifier, String redirectUri, Provider provider) {
        return new OAuth2Request(authorizationCode, codeVerifier, redirectUri, provider);
    }

}