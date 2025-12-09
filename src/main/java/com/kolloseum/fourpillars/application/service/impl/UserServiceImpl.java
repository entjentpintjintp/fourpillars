package com.kolloseum.fourpillars.application.service.impl;

import com.kolloseum.fourpillars.application.dto.UserCommand;
import com.kolloseum.fourpillars.application.dto.UserQuery;
import com.kolloseum.fourpillars.application.service.UserService;
import com.kolloseum.fourpillars.common.exception.BusinessException;
import com.kolloseum.fourpillars.common.exception.JwtException;
import com.kolloseum.fourpillars.common.utils.AuthValidator;
import com.kolloseum.fourpillars.domain.model.entity.User;
import com.kolloseum.fourpillars.domain.model.vo.TokenPayload;
import com.kolloseum.fourpillars.domain.repository.UserRepository;
import com.kolloseum.fourpillars.domain.service.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final AuthValidator authValidator;

    @Override
    @Transactional(readOnly = true)
    public UserQuery getUserInfo(TokenPayload tokenPayload) {
        User user = authValidator.validateAndGetAuthorizedUser(tokenPayload);

        if (user.getProfile() == null || user.getProfile().getBirthdate() == null) {
            throw BusinessException.invalidUser("User profile not found");
        }

        return UserQuery.of(user.getProfile().getBirthdate());
    }

    @Override
    @Transactional
    public void updateProfile(TokenPayload tokenPayload, UserCommand userCommand) {
        try {
            User user = authValidator.validateAndGetAuthorizedUser(tokenPayload);

            LocalDate birthdate = userCommand.getBirthdate();
            if (birthdate != null && birthdate.isAfter(LocalDate.now())) {
                throw BusinessException.invalidBirthDate("Birthdate cannot be in the future");
            }

            User updatedUser = user.createOrUpdateProfile(birthdate);
            userRepository.save(updatedUser);

            log.info("User profile updated successfully: userId={}, birthdate={}", tokenPayload.getUserId(), birthdate);

        } catch (Exception e) {
            if (e instanceof BusinessException || e instanceof JwtException) {
                throw e;
            }
            throw BusinessException.profileUpdateFailed("User profile update failed: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void withdraw(TokenPayload headerTokenPayload, String accessToken, String refreshToken) {
        authValidator.validateAndGetAuthorizedUser(headerTokenPayload);
        UUID headerUserId = headerTokenPayload.getUserId();

        TokenPayload bodyAccessTokenPayload = tokenProvider.parseToken(accessToken);
        if (!"ACCESS".equals(bodyAccessTokenPayload.getTokenType())) {
            throw JwtException.invalidTokenState("Body accessToken must be ACCESS type");
        }

        TokenPayload bodyRefreshTokenPayload = tokenProvider.parseToken(refreshToken);
        if (!"REFRESH".equals(bodyRefreshTokenPayload.getTokenType())) {
            throw JwtException.invalidTokenState("Body refreshToken must be REFRESH type");
        }

        if (!headerUserId.equals(bodyAccessTokenPayload.getUserId()) ||
                !headerUserId.equals(bodyRefreshTokenPayload.getUserId())) {
            throw JwtException.invalidToken("Token mismatch: All tokens must belong to the same user");
        }

        try {
            log.info("Starting user deletion for userId: {}", headerUserId);
            userRepository.delete(headerUserId);
            log.info("User deletion completed for userId: {}", headerUserId);
        } catch (Exception e) {
            throw BusinessException.withdrawalFailed("User deletion failed: " + e.getMessage());
        }

        try {
            if (accessToken != null) {
                tokenProvider.blacklistToken(accessToken);
                log.debug("Access token blacklisted successfully");
            }
            if (refreshToken != null) {
                tokenProvider.blacklistToken(refreshToken);
                log.debug("Refresh token blacklisted successfully");
            }
        } catch (Exception e) {
            log.warn("Token blacklist failed for userId: {}, but user deletion succeeded. Error: {}",
                    headerUserId, e.getMessage());
        }

        log.info("User withdrawal successful: userId={}", headerUserId);
    }
}