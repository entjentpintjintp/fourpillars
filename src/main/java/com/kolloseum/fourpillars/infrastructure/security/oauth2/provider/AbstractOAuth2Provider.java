package com.kolloseum.fourpillars.infrastructure.security.oauth2.provider;

import com.kolloseum.fourpillars.domain.model.enums.Provider;
import com.kolloseum.fourpillars.domain.model.vo.OAuth;
import com.kolloseum.fourpillars.domain.model.vo.OAuth2Request;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractOAuth2Provider {

    protected final WebClient webClient;

    public OAuth authenticate(OAuth2Request request) {
        try {
            String accessToken = exchangeCodeForToken(request);
            String socialId = getUserInfo(accessToken);

            return OAuth.of(socialId, getProvider());

        } catch (Exception e) {
            log.error("{} OAuth2 authentication failed: {}", getProvider(), e.getMessage());
            throw new RuntimeException("OAuth2 authentication failed: " + e.getMessage());
        }
    }

    private String exchangeCodeForToken(OAuth2Request request) {
        MultiValueMap<String, String> body = createTokenRequestBody(request);

        log.debug("  [TOKEN REQUEST]");
        String grantType = body.getFirst("grant_type");
        log.debug("  grant_type: {}", grantType != null ? grantType : "N/A");

        String code = body.getFirst("code");
        log.debug("  code: {}...",
                code != null && code.length() > 20
                        ? code.substring(0, 20)
                        : "N/A");

        String redirectUri = body.getFirst("redirect_uri");
        if (redirectUri != null) {
            log.debug("  redirect_uri: {}", redirectUri);
        } else {
            log.debug("  redirect_uri: (not used - SDK mode)");
        }

        String clientId = body.getFirst("client_id");
        log.debug("  client_id: {}", clientId != null ? clientId : "N/A");

        String codeVerifier = body.getFirst("code_verifier");
        if (codeVerifier != null) {
            log.debug("  code_verifier: {}...",
                    codeVerifier.length() > 20 ? codeVerifier.substring(0, 20)
                            : "N/A");
        } else {
            log.debug("  code_verifier: (not used - SDK mode)");
        }

        try {

            @SuppressWarnings("unchecked")
            Map<String, Object> response = webClient.post()
                    .uri(getTokenEndpoint())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response == null || !response.containsKey("access_token")) {
                throw new RuntimeException("Failed to get access token");
            }

            return (String) response.get("access_token");
        } catch (Exception e) {
            log.error("Token Exchange Failed: {}", e.getMessage());
            throw e;
        }
    }

    private String getUserInfo(String accessToken) {
        @SuppressWarnings("unchecked")
        Map<String, Object> response = webClient.get()
                .uri(getUserInfoEndpoint())
                .headers(headers -> headers.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        if (response == null) {
            throw new RuntimeException("Failed to get user info");
        }

        return extractSocialId(response);
    }

    private MultiValueMap<String, String> createTokenRequestBody(OAuth2Request request) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("code", request.getAuthorizationCode());

        if (request.getRedirectUri() != null && !request.getRedirectUri().trim().isEmpty()) {
            body.add("redirect_uri", request.getRedirectUri());
        }

        if (supportsPKCE() &&
                request.getCodeVerifier() != null &&
                !request.getCodeVerifier().trim().isEmpty()) {
            body.add("code_verifier", request.getCodeVerifier());
            log.debug("{} using PKCE authentication", getProvider());
        }

        addClientCredentials(body);
        return body;
    }

    protected abstract Provider getProvider();

    protected abstract String getTokenEndpoint();

    protected abstract String getUserInfoEndpoint();

    protected abstract String extractSocialId(Map<String, Object> userInfo);

    protected abstract void addClientCredentials(MultiValueMap<String, String> body);

    protected abstract boolean supportsPKCE();
}