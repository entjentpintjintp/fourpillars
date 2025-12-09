package com.kolloseum.fourpillars.application.service.impl;

import com.kolloseum.fourpillars.application.dto.*;
import com.kolloseum.fourpillars.application.service.AuthorizationService;
import com.kolloseum.fourpillars.common.exception.BusinessException;
import com.kolloseum.fourpillars.common.exception.ErrorCode;
import com.kolloseum.fourpillars.common.exception.JwtException;
import com.kolloseum.fourpillars.common.utils.AuthValidator;
import com.kolloseum.fourpillars.domain.model.entity.Terms;
import com.kolloseum.fourpillars.domain.model.entity.User;
import com.kolloseum.fourpillars.domain.model.enums.Role;
import com.kolloseum.fourpillars.domain.model.enums.TermsType;
import com.kolloseum.fourpillars.domain.model.vo.OAuth;
import com.kolloseum.fourpillars.domain.model.vo.TokenPayload;
import com.kolloseum.fourpillars.domain.repository.TermsRepository;
import com.kolloseum.fourpillars.domain.repository.UserRepository;
import com.kolloseum.fourpillars.domain.service.TermsFileService;
import com.kolloseum.fourpillars.domain.service.TokenProvider;
import com.kolloseum.fourpillars.infrastructure.persistence.repository.TermsJpaRepository;
import com.kolloseum.fourpillars.infrastructure.persistence.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorizationServiceImpl implements AuthorizationService {

        private final TokenProvider tokenProvider;
        private final UserRepository userRepository;
        private final TermsRepository termsRepository;
        private final TermsFileService termsFileService;
        private final AuthValidator authValidator;
        private final TermsJpaRepository termsJpaRepository;

        @Override
        @Transactional
        public AuthTokenInput updateTermsAgreementAndDeviceInfo(TokenPayload temporaryTokenPayload,
                        String temporaryTokenString,
                        AuthTermsAgreementCommand termsAgreement,
                        DeviceUpdateCommand deviceUpdate) {

                // TEMPORARY 또는 ACCESS 토큰인지 확인
                if (!"TEMPORARY".equals(temporaryTokenPayload.getTokenType())
                                && !"ACCESS".equals(temporaryTokenPayload.getTokenType())) {
                        throw JwtException.invalidTokenState("Not a temporary or access token");
                }

                // OAuth 정보로 사용자 조회
                OAuth oAuth = OAuth.of(temporaryTokenPayload.getSocialId(), temporaryTokenPayload.getProvider());
                User user = userRepository.findByOAuth(oAuth)
                                .orElseGet(() -> User.create(oAuth, Role.USER));

                // 디바이스 정보 업데이트
                User updatedUser = user.createOrUpdateDevice(
                                deviceUpdate.getOs_version(),
                                deviceUpdate.getOs_name());

                // 약관 동의 처리
                if (termsAgreement.getServiceTermsAgreedVersion() != null) {
                        updatedUser = updatedUser.agreeToTerms(
                                        termsAgreement.getServiceTermsAgreedVersion(),
                                        TermsType.SERVICE);
                        log.info("User agreed to terms: userId={}, type={}, version={}", updatedUser.getId(),
                                        TermsType.SERVICE, termsAgreement.getServiceTermsAgreedVersion());
                }

                if (termsAgreement.getPrivacyTermsAgreedVersion() != null) {
                        updatedUser = updatedUser.agreeToTerms(
                                        termsAgreement.getPrivacyTermsAgreedVersion(),
                                        TermsType.PRIVACY);
                        log.info("User agreed to terms: userId={}, type={}, version={}", updatedUser.getId(),
                                        TermsType.PRIVACY, termsAgreement.getPrivacyTermsAgreedVersion());
                }

                updatedUser = updatedUser.updateLastLogin();

                // 정식 사용자로 저장
                User savedUser = userRepository.save(updatedUser);

                // 원본 토큰 문자열로 블랙리스트 처리
                if (temporaryTokenString != null) {
                        tokenProvider.blacklistToken(temporaryTokenString);
                }

                // 정식 토큰 발급
                TokenPayload accessPayload = TokenPayload.ofAccess(
                                savedUser.getId(),
                                oAuth.getSocialId(),
                                oAuth.getProvider(),
                                savedUser.getRole());
                TokenPayload refreshPayload = TokenPayload.ofRefresh(savedUser.getId());

                String accessToken = tokenProvider.generateToken(accessPayload);
                String refreshToken = tokenProvider.generateToken(refreshPayload);

                log.info("User registered and tokens issued: userId={}", savedUser.getId());
                log.info("Device info updated for userId={}: os_name={}, os_version={}", savedUser.getId(),
                                deviceUpdate.getOs_name(), deviceUpdate.getOs_version());

                return AuthTokenInput.of(accessToken, refreshToken);
        }

        @Override
        public AuthTermsCheckQuery checkTermsStatus(TokenPayload tokenPayload) {

                if ("TEMPORARY".equals(tokenPayload.getTokenType())) {
                        // DB 조회 불필요 : 신규
                        return AuthTermsCheckQuery.of(false, false); // 프로필 미완성, 약관 미동의
                }

                // ACCESS/REFRESH 토큰인 경우: 기존 로직
                if (tokenPayload.getUserId() == null) {
                        throw JwtException.invalidTokenState("User ID is required for this operation");
                }

                User user = userRepository.findById(tokenPayload.getUserId())
                                .orElseThrow(() -> BusinessException.invalidUser("User not found"));

                boolean completedProfile = user.isProfileCompleted();
                boolean latestTermsAgreed = authValidator.checkLatestTermsAgreement(user);

                return AuthTermsCheckQuery.of(completedProfile, latestTermsAgreed);

        }

        @Override
        public AuthTermsContentQuery getTermsContent(TokenPayload tokenPayload) {
                // 토큰 검증만 수행 (TokenPayload가 이미 검증된 상태)

                // 최신 약관 조회
                Optional<Terms> serviceTerms = termsRepository.findLatestByType(TermsType.SERVICE);
                Optional<Terms> privacyTerms = termsRepository.findLatestByType(TermsType.PRIVACY);

                // DB에서 내용 조회 (없으면 파일 조회 로직은 제거하거나 fallback으로 유지 가능하나, 여기선 DB 우선)
                String serviceContent = serviceTerms
                                .flatMap(terms -> termsJpaRepository.findByTermsTypeAndTermsVersion(
                                                terms.getTermsType(), terms.getTermsVersion()))
                                .map(entity -> entity.getTermsContent())
                                .orElse(null);

                String serviceVersion = serviceTerms.map(Terms::getTermsVersion).orElse(null);

                String privacyContent = privacyTerms
                                .flatMap(terms -> termsJpaRepository.findByTermsTypeAndTermsVersion(
                                                terms.getTermsType(), terms.getTermsVersion()))
                                .map(entity -> entity.getTermsContent())
                                .orElse(null);

                String privacyVersion = privacyTerms.map(Terms::getTermsVersion).orElse(null);

                return AuthTermsContentQuery.of(
                                serviceContent,
                                serviceVersion,
                                privacyContent,
                                privacyVersion);
        }

        @Override
        public String getPublicTermsContent(String typeStr) {
                TermsType type;
                try {
                        type = TermsType.valueOf(typeStr.toUpperCase());
                } catch (IllegalArgumentException e) {
                        throw BusinessException.invalidRequest("Invalid terms type: " + typeStr);
                }

                Optional<Terms> latestTerms = termsRepository.findLatestByType(type);
                String content = latestTerms
                                .flatMap(terms -> termsJpaRepository.findByTermsTypeAndTermsVersion(
                                                terms.getTermsType(), terms.getTermsVersion()))
                                .map(entity -> entity.getTermsContent())
                                .orElse("No content available.");

                // Simple HTML wrapper for better viewing in browser
                return "<!DOCTYPE html>" +
                                "<html>" +
                                "<head>" +
                                "<meta charset='UTF-8'>" +
                                "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                                "<title>" + type + "</title>" +
                                "<style>" +
                                "body { font-family: sans-serif; line-height: 1.6; padding: 20px; max-width: 800px; margin: 0 auto; }"
                                +
                                "pre { white-space: pre-wrap; word-wrap: break-word; }" +
                                "</style>" +
                                "</head>" +
                                "<body>" +
                                "<h1>" + type + "</h1>" +
                                "<pre>" + content + "</pre>" +
                                "</body>" +
                                "</html>";
        }
}