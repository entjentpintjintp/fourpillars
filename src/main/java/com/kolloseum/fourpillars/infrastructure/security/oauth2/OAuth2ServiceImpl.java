// 📁 infrastructure/security/oauth2/OAuth2ServiceImpl.java
package com.kolloseum.fourpillars.infrastructure.security.oauth2;

import com.kolloseum.fourpillars.domain.model.enums.Provider;
import com.kolloseum.fourpillars.domain.model.vo.OAuth;
import com.kolloseum.fourpillars.domain.model.vo.OAuth2Request;
import com.kolloseum.fourpillars.domain.service.OAuth2Service;
import com.kolloseum.fourpillars.infrastructure.security.oauth2.provider.AbstractOAuth2Provider;
import com.kolloseum.fourpillars.infrastructure.security.oauth2.provider.GoogleOAuth2Provider;
import com.kolloseum.fourpillars.infrastructure.security.oauth2.provider.KakaoOAuth2Provider;
import com.kolloseum.fourpillars.infrastructure.security.oauth2.provider.NaverOAuth2Provider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuth2ServiceImpl implements OAuth2Service {
    
    private final GoogleOAuth2Provider googleProvider;
    private final KakaoOAuth2Provider kakaoProvider;
    private final NaverOAuth2Provider naverProvider;
    
    @Override
    public OAuth authenticate(OAuth2Request request) {
        AbstractOAuth2Provider provider = getProvider(request.getProvider());
        return provider.authenticate(request);
    }
    
    private AbstractOAuth2Provider getProvider(Provider provider) {
        return switch (provider) {
            case GOOGLE -> googleProvider;
            case KAKAO -> kakaoProvider;
            case NAVER -> naverProvider;
        };
    }
}