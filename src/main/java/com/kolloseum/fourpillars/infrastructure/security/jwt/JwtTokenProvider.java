package com.kolloseum.fourpillars.infrastructure.security.jwt;

import com.kolloseum.fourpillars.common.exception.JwtException;
import com.kolloseum.fourpillars.domain.model.enums.Provider;
import com.kolloseum.fourpillars.domain.model.enums.Role;
import com.kolloseum.fourpillars.domain.model.vo.TokenPayload;
import com.kolloseum.fourpillars.domain.service.BlacklistService;
import com.kolloseum.fourpillars.domain.service.TokenProvider;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider implements TokenProvider {
    
    private final JwtProperties jwtProperties;
    private final BlacklistService blacklistService; // ✅ 인터페이스로 변경
    
    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    @Override
    public String generateToken(TokenPayload payload) {
        try {
            Date now = new Date();
            Date expiration = new Date(now.getTime() + getExpirationTime(payload));
            
            JwtBuilder builder = Jwts.builder()
                    .claim("tokenType", payload.getTokenType())
                    .issuer(jwtProperties.getIssuer())
                    .issuedAt(now)
                    .expiration(expiration)
                    .signWith(getSecretKey());
            
            if ("ACCESS".equals(payload.getTokenType())) {
                builder.subject(payload.getSocialId())
                       .claim("userId", payload.getUserId().toString())
                       .claim("provider", payload.getProvider().name())
                       .claim("role", payload.getRole().name());
            } else if ("REFRESH".equals(payload.getTokenType())) {
                builder.claim("userId", payload.getUserId().toString());
            } else if ("TEMPORARY".equals(payload.getTokenType())) {
                builder.subject(payload.getSocialId())
                       .claim("provider", payload.getProvider().name());
            }
            
            return builder.compact();
            
        } catch (Exception e) {
            log.error("Failed to generate JWT token: {}", e.getMessage());
            throw JwtException.generationError("Token generation failed: " + e.getMessage());
        }
    }


    @Override
    public TokenPayload parseToken(String token) {
        try {
            if (blacklistService.isBlacklisted(token)) {
                log.warn("Attempt to parse blacklisted token");
                throw JwtException.blacklistedToken("Token has been revoked");
            }
            
            Claims claims = Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            
            String tokenType = claims.get("tokenType", String.class);
            
            if ("ACCESS".equals(tokenType)) {
                return parseAccessToken(claims);
            } else if ("REFRESH".equals(tokenType)) {
                return parseRefreshToken(claims);
            } else if ("TEMPORARY".equals(tokenType)) {
                return parseTemporaryToken(claims);
            }
            
            throw JwtException.parsingError("Unknown token type: " + tokenType);
            
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token: {}", e.getMessage());
            throw JwtException.expiredToken("Token has expired");
        } catch (UnsupportedJwtException | MalformedJwtException | SecurityException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            throw JwtException.invalidToken("Invalid token format");
        } catch (io.jsonwebtoken.JwtException e) {
            log.error("JWT parsing failed: {}", e.getMessage());
            throw JwtException.parsingError("Token parsing failed: " + e.getMessage());
        } catch (JwtException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during token parsing: {}", e.getMessage());
            throw JwtException.parsingError("Unexpected error occurred during token processing");
        }
    }

    @Override
    public void blacklistToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            
            // 토큰의 남은 유효시간 계산
            long expirationTime = claims.getExpiration().getTime();
            long currentTime = System.currentTimeMillis();
            long remainingTime = (expirationTime - currentTime) / 1000;
            
            if (remainingTime > 0) {
                blacklistService.addToBlacklist(token, remainingTime);
                log.info("Token added to blacklist with {} seconds remaining", remainingTime);
            }
        } catch (Exception e) {
            log.warn("Failed to blacklist token: {}", e.getMessage());
        }
    }
    
    private TokenPayload parseAccessToken(Claims claims) {
        try {
            String socialId = claims.getSubject();
            String userIdStr = claims.get("userId", String.class);
            String providerStr = claims.get("provider", String.class);
            String roleStr = claims.get("role", String.class);
            
            if (userIdStr == null || providerStr == null || roleStr == null) {
                throw JwtException.parsingError("Missing required token claims");
            }
            
            UUID userId = UUID.fromString(userIdStr);
            Provider provider = Provider.valueOf(providerStr);
            Role role = Role.valueOf(roleStr);
            
            return TokenPayload.ofAccess(userId, socialId, provider, role);
            
        } catch (IllegalArgumentException e) {
            log.error("Invalid token data: {}", e.getMessage());
            throw JwtException.parsingError("Invalid token data: " + e.getMessage());
        }
    }
    
    private TokenPayload parseRefreshToken(Claims claims) {
        try {
            String userIdStr = claims.get("userId", String.class);
            
            if (userIdStr == null) {
                throw JwtException.parsingError("Missing userId in refresh token");
            }
            
            UUID userId = UUID.fromString(userIdStr);
            return TokenPayload.ofRefresh(userId);
            
        } catch (IllegalArgumentException e) {
            log.error("Invalid refresh token data: {}", e.getMessage());
            throw JwtException.parsingError("Invalid refresh token data: " + e.getMessage());
        }
    }
    
    private TokenPayload parseTemporaryToken(Claims claims) {
        try {
            String socialId = claims.getSubject();
            String providerStr = claims.get("provider", String.class);
            
            if (socialId == null || providerStr == null) {
                throw JwtException.parsingError("Missing required temporary token claims");
            }
            
            Provider provider = Provider.valueOf(providerStr);
            return TokenPayload.ofTemporary(socialId, provider);
            
        } catch (IllegalArgumentException e) {
            log.error("Invalid temporary token data: {}", e.getMessage());
            throw JwtException.parsingError("Invalid temporary token data: " + e.getMessage());
        }
    }

    private long getExpirationTime(TokenPayload payload) {
        return switch (payload.getTokenType()) {
            case "TEMPORARY" -> jwtProperties.getTemporaryTokenValidityInSeconds() * 1000;
            case "REFRESH" -> jwtProperties.getRefreshTokenValidityInSeconds() * 1000;
            default -> jwtProperties.getAccessTokenValidityInSeconds() * 1000;
        };
    }
}