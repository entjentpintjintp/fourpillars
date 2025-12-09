package com.kolloseum.fourpillars.infrastructure.security.oauth2;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "oauth")
@Getter
@Setter
public class OAuth2Properties {
    
    private Google google = new Google();
    private Kakao kakao = new Kakao();
    private Naver naver = new Naver();
    
    @Getter
    @Setter
    public static class Google {
        private String clientId;
        private String clientSecret;
    }
    
    @Getter
    @Setter
    public static class Kakao {
        private String clientId;
        private String clientSecret;
    }
    
    @Getter
    @Setter
    public static class Naver {
        private String clientId;
        private String clientSecret;
    }
}