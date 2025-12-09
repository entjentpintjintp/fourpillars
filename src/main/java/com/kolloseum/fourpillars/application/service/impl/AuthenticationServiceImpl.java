package com.kolloseum.fourpillars.application.service.impl;

import com.kolloseum.fourpillars.application.dto.AuthLoginCommand;
import com.kolloseum.fourpillars.application.dto.AuthTokenInput;
import com.kolloseum.fourpillars.application.service.AuthenticationService;
import com.kolloseum.fourpillars.application.service.TotpService;
import com.kolloseum.fourpillars.common.exception.BusinessException;
import com.kolloseum.fourpillars.common.exception.JwtException;
import com.kolloseum.fourpillars.domain.model.entity.User;
import com.kolloseum.fourpillars.domain.model.enums.Role;
import com.kolloseum.fourpillars.domain.model.vo.OAuth;
import com.kolloseum.fourpillars.domain.model.vo.OAuth2Request;
import com.kolloseum.fourpillars.domain.model.vo.TokenPayload;
import com.kolloseum.fourpillars.domain.repository.UserRepository;
import com.kolloseum.fourpillars.domain.service.OAuth2Service;
import com.kolloseum.fourpillars.domain.service.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final OAuth2Service oAuth2Service;
    private final TotpService totpService;

    @Override
    @Transactional
    public AuthTokenInput login(AuthLoginCommand command) {
        // AuthLoginCommand → OAuth2Request 변환
        OAuth2Request request = OAuth2Request.of(
                command.getAuthorizationCode(),
                command.getCodeVerifier(),
                command.getRedirectUri(),
                command.getProvider());

        // OAuth2 인증 처리
        OAuth oAuth = oAuth2Service.authenticate(request);

        // 기존 사용자 확인
        Optional<User> existingUser = userRepository.findByOAuth(oAuth);

        if (existingUser.isPresent()) {
            // 기존 회원 - 정식 토큰 발급
            User user = existingUser.get().updateLastLogin();
            User savedUser = userRepository.save(user);

            TokenPayload accessPayload = TokenPayload.ofAccess(
                    savedUser.getId(),
                    oAuth.getSocialId(),
                    oAuth.getProvider(),
                    savedUser.getRole());
            TokenPayload refreshPayload = TokenPayload.ofRefresh(savedUser.getId());

            String accessToken = tokenProvider.generateToken(accessPayload);
            String refreshToken = tokenProvider.generateToken(refreshPayload);

            log.info("User login success: userId={}, provider={}", savedUser.getId(), oAuth.getProvider());
            return AuthTokenInput.of(accessToken, refreshToken);

        } else {
            // 신규 사용자 - 임시 토큰 발급 (리프레시 토큰은 null)
            TokenPayload temporaryPayload = TokenPayload.ofTemporary(
                    oAuth.getSocialId(),
                    oAuth.getProvider());

            String temporaryToken = tokenProvider.generateToken(temporaryPayload);

            log.info("New user login (temporary): provider={}, socialId={}", oAuth.getProvider(), oAuth.getSocialId());
            return AuthTokenInput.of(temporaryToken, null);
        }
    }

    @Override
    public AuthTokenInput refreshToken(String refreshToken) {

        // 토큰 파싱하여 사용자 ID 추출
        TokenPayload payload = tokenProvider.parseToken(refreshToken);

        // REFRESH 토큰인지 확인
        if (!"REFRESH".equals(payload.getTokenType())) {
            throw JwtException.invalidTokenState("Not a refresh token");
        }

        // 사용자 존재 확인
        User user = userRepository.findById(payload.getUserId())
                .orElseThrow(() -> BusinessException.invalidUser("User not found"));

        // 새로운 토큰 생성
        TokenPayload newAccessPayload = TokenPayload.ofAccess(
                user.getId(),
                user.getOAuth().getSocialId(),
                user.getOAuth().getProvider(),
                user.getRole());
        TokenPayload newRefreshPayload = TokenPayload.ofRefresh(user.getId());

        String newAccessToken = tokenProvider.generateToken(newAccessPayload);
        String newRefreshToken = tokenProvider.generateToken(newRefreshPayload);

        log.info("Token refreshed: userId={}", user.getId());
        return AuthTokenInput.of(newAccessToken, newRefreshToken);
    }

    @Override
    public void logout(String accessToken, String refreshToken) {
        // 액세스 토큰 블랙리스트 추가
        if (accessToken != null) {
            tokenProvider.blacklistToken(accessToken);
        }

        // 리프레시 토큰 블랙리스트 추가
        if (refreshToken != null) {
            tokenProvider.blacklistToken(refreshToken);
        }
        log.info("User logout processed");
    }
}