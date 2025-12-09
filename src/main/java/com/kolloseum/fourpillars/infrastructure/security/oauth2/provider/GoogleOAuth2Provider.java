// 📁 infrastructure/security/oauth2/GoogleOAuth2Provider.java
package com.kolloseum.fourpillars.infrastructure.security.oauth2.provider;

import com.kolloseum.fourpillars.domain.model.enums.Provider;
import com.kolloseum.fourpillars.infrastructure.security.oauth2.OAuth2Properties;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class GoogleOAuth2Provider extends AbstractOAuth2Provider {
    
    private final OAuth2Properties.Google googleProps;
    
    public GoogleOAuth2Provider(WebClient webClient, OAuth2Properties oAuth2Properties) {
        super(webClient);
        this.googleProps = oAuth2Properties.getGoogle();
    }
    
    @Override
    protected Provider getProvider() {
        return Provider.GOOGLE;
    }
    
    @Override
    protected String getTokenEndpoint() {
        return "https://oauth2.googleapis.com/token";
    }
    
    @Override
    protected String getUserInfoEndpoint() {
        return "https://www.googleapis.com/oauth2/v2/userinfo";
    }
    
    @Override
    protected String extractSocialId(Map<String, Object> userInfo) {
        return (String) userInfo.get("id");
    }

    @Override
    protected boolean supportsPKCE() {
        return true; // ✅ PKCE + client_secret
    }

    @Override
    protected void addClientCredentials(MultiValueMap<String, String> body) {
        body.add("client_id", googleProps.getClientId());
        body.add("client_secret", googleProps.getClientSecret());
    }
}