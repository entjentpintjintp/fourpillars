package com.kolloseum.fourpillars.common.utils;

import com.kolloseum.fourpillars.common.exception.JwtException;
import com.kolloseum.fourpillars.domain.model.enums.Provider;
import com.kolloseum.fourpillars.domain.model.enums.Role;
import com.kolloseum.fourpillars.domain.model.vo.TokenPayload;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public class SecurityContextTokenExtractor {

    public static TokenPayload getCurrentTokenPayload() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        if (auth != null && auth.getPrincipal() instanceof TokenPayload) {
            return (TokenPayload) auth.getPrincipal();
        }
        
        throw JwtException.invalidTokenState("No valid token found in security context");
    }

    public static UUID getCurrentUserId() {
        TokenPayload payload = getCurrentTokenPayload();
        
        if (payload.getUserId() == null) {
            throw JwtException.invalidTokenState("User ID is not available for this token type");
        }
        
        return payload.getUserId();
    }

    public static String getCurrentTokenType() {
        return getCurrentTokenPayload().getTokenType();
    }
    

    public static Role getCurrentUserRole() {
        TokenPayload payload = getCurrentTokenPayload();
        
        if (payload.getRole() == null) {
            throw JwtException.invalidTokenState("User role is not available for this token type");
        }
        
        return payload.getRole();
    }

    public static String getCurrentSocialId() {
        TokenPayload payload = getCurrentTokenPayload();
        
        if (payload.getSocialId() == null) {
            throw JwtException.invalidTokenState("Social ID is not available for this token type");
        }
        
        return payload.getSocialId();
    }

    public static Provider getCurrentProvider() {
        TokenPayload payload = getCurrentTokenPayload();
        
        if (payload.getProvider() == null) {
            throw JwtException.invalidTokenState("Provider is not available for this token type");
        }
        
        return payload.getProvider();
    }

    public static boolean isAccessToken() {
        return "ACCESS".equals(getCurrentTokenType());
    }

    public static boolean isTemporaryToken() {
        return "TEMPORARY".equals(getCurrentTokenType());
    }

    public static boolean isRefreshToken() {
        return "REFRESH".equals(getCurrentTokenType());
    }
}