package com.kolloseum.fourpillars.infrastructure.security.jwt;

import com.kolloseum.fourpillars.common.exception.JwtException;
import com.kolloseum.fourpillars.domain.model.vo.TokenPayload;
import com.kolloseum.fourpillars.domain.service.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        try {
            String token = extractTokenFromRequest(request);

            if (StringUtils.hasText(token)) {
                TokenPayload tokenPayload = tokenProvider.parseToken(token);
                Authentication authentication = createAuthentication(tokenPayload);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                log.debug("Authentication success: userId={}, role={}",
                        tokenPayload.getUserId(), tokenPayload.getRole());
            }
        } catch (Exception e) {
            log.error("Authentication failed: {}", e.getMessage());
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }

        return null;
    }

    private Authentication createAuthentication(TokenPayload tokenPayload) {
        List<SimpleGrantedAuthority> authorities;

        if ("TEMPORARY".equals(tokenPayload.getTokenType())) {
            authorities = List.of(new SimpleGrantedAuthority("SIGNUP_PROCESS"));
        } else if (tokenPayload.getRole() != null) {
            authorities = List.of(new SimpleGrantedAuthority("ROLE_" + tokenPayload.getRole().name()));
        } else {
            throw JwtException.invalidTokenState("The token is invalid");
        }

        return new UsernamePasswordAuthenticationToken(tokenPayload, null, authorities);
    }
}