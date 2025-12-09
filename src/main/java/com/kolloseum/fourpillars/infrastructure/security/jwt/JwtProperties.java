package com.kolloseum.fourpillars.infrastructure.security.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
public class JwtProperties {
    
    private String secret;
    private long temporaryTokenValidityInSeconds = 420; // 7분
    private long accessTokenValidityInSeconds = 3600;   // 1시간
    private long refreshTokenValidityInSeconds = 604800; // 7일
    private String issuer = "auth-service";
}