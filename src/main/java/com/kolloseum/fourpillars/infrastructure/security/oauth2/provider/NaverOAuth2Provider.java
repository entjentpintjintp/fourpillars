// 📁 infrastructure/security/oauth2/NaverOAuth2Provider.java
package com.kolloseum.fourpillars.infrastructure.security.oauth2.provider;

import com.kolloseum.fourpillars.domain.model.enums.Provider;
import com.kolloseum.fourpillars.infrastructure.security.oauth2.OAuth2Properties;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class NaverOAuth2Provider extends AbstractOAuth2Provider {
    
    private final OAuth2Properties.Naver naverProps;
    
    public NaverOAuth2Provider(WebClient webClient, OAuth2Properties oAuth2Properties) {
        super(webClient);
        this.naverProps = oAuth2Properties.getNaver();
    }
    
    @Override
    protected Provider getProvider() {
        return Provider.NAVER;
    }
    
    @Override
    protected String getTokenEndpoint() {
        return "https://nid.naver.com/oauth2.0/token";
    }
    
    @Override
    protected String getUserInfoEndpoint() {
        return "https://openapi.naver.com/v1/nid/me";
    }
    
    @Override
    protected String extractSocialId(Map<String, Object> userInfo) {
        @SuppressWarnings("unchecked")
        Map<String, Object> response = (Map<String, Object>) userInfo.get("response");
        return response != null ? (String) response.get("id") : null;
    }

    @Override
    protected boolean supportsPKCE() {
        return false; // ❌ client_secret만
    }
    
    @Override
    protected void addClientCredentials(MultiValueMap<String, String> body) {
        body.add("client_id", naverProps.getClientId());
        body.add("client_secret", naverProps.getClientSecret());
    }
}