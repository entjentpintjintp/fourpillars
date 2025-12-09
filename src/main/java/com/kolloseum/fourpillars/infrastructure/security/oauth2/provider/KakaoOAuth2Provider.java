// 📁 infrastructure/security/oauth2/KakaoOAuth2Provider.java
package com.kolloseum.fourpillars.infrastructure.security.oauth2.provider;

import com.kolloseum.fourpillars.domain.model.enums.Provider;
import com.kolloseum.fourpillars.infrastructure.security.oauth2.OAuth2Properties;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class KakaoOAuth2Provider extends AbstractOAuth2Provider {
    
    private final OAuth2Properties.Kakao kakaoProps;
    
    public KakaoOAuth2Provider(WebClient webClient, OAuth2Properties oAuth2Properties) {
        super(webClient);
        this.kakaoProps = oAuth2Properties.getKakao();
    }
    
    @Override
    protected Provider getProvider() {
        return Provider.KAKAO;
    }
    
    @Override
    protected String getTokenEndpoint() {
        return "https://kauth.kakao.com/oauth/token";
    }
    
    @Override
    protected String getUserInfoEndpoint() {
        return "https://kapi.kakao.com/v2/user/me";
    }
    
    @Override
    protected String extractSocialId(Map<String, Object> userInfo) {
        return String.valueOf(userInfo.get("id"));
    }

    @Override
    protected boolean supportsPKCE() {
        return true; // ✅ PKCE + client_secret
    }
    
    @Override
    protected void addClientCredentials(MultiValueMap<String, String> body) {
        body.add("client_id", kakaoProps.getClientId());
        body.add("client_secret", kakaoProps.getClientSecret());
    }
}