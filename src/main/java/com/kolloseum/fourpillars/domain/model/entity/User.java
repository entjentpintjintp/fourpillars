package com.kolloseum.fourpillars.domain.model.entity;

import com.kolloseum.fourpillars.common.exception.BusinessException;
import com.kolloseum.fourpillars.domain.model.enums.Role;
import com.kolloseum.fourpillars.domain.model.enums.TermsType;
import com.kolloseum.fourpillars.domain.model.vo.OAuth;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
public class User {

        private final UUID id;
        private final OAuth oAuth;
        private final Role role;
        private final LocalDateTime lastLogin;
        private final LocalDateTime createdAt;
        private final String totpSecret;

        // 집계 내부 엔티티들
        private final Profile profile;
        private final Device device;
        private final List<TermsAgreement> termsAgreements;

        // 팩토리 메서드
        public static User create(OAuth oAuth, Role role) {
                return new User(
                                null,
                                oAuth,
                                role,
                                null,
                                LocalDateTime.now(),
                                null,
                                null,
                                null,
                                new ArrayList<>());
        }

        public static User restore(UUID id, OAuth oAuth, Role role, LocalDateTime lastLogin,
                        LocalDateTime createdAt, String totpSecret,
                        Profile profile, Device device, List<TermsAgreement> termsAgreements) {
                // TOTP enabled 여부는 secret 존재 여부로 판단
                return new User(id, oAuth, role, lastLogin, createdAt, totpSecret,
                                profile, device,
                                termsAgreements != null ? new ArrayList<>(termsAgreements) : new ArrayList<>());
        }

        // 집계루트 메서드
        public User createOrUpdateProfile(LocalDate lunarBirthdate,
                        com.kolloseum.fourpillars.domain.model.enums.TimeBranch birthTime) {
                Profile newProfile = this.profile == null
                                ? Profile.create(lunarBirthdate, birthTime)
                                : this.profile.update(lunarBirthdate, birthTime);

                return new User(this.id, this.oAuth, this.role, this.lastLogin, this.createdAt,
                                this.totpSecret, newProfile, this.device, this.termsAgreements);
        }

        public User createOrUpdateDevice(String os_version, String os_name) {
                Device newDevice = this.device == null
                                ? Device.create(os_version, os_name)
                                : this.device.update(os_version, os_name);

                return new User(this.id, this.oAuth, this.role, this.lastLogin, this.createdAt,
                                this.totpSecret, this.profile, newDevice, this.termsAgreements);
        }

        public User agreeToTerms(String termsVersion, TermsType termsType) {
                // 이미 해당 버전에 동의했는지만 확인
                boolean alreadyAgreedToThisVersion = termsAgreements.stream()
                                .anyMatch(agreement -> agreement.getTermsType() == termsType &&
                                                agreement.getTermsAgreedVersion().equals(termsVersion));

                if (alreadyAgreedToThisVersion) {
                        return this;
                }

                // 새로운 약관 동의 추가 (기존 기록은 유지됨)
                TermsAgreement newAgreement = TermsAgreement.create(termsVersion, termsType);
                List<TermsAgreement> newAgreements = new ArrayList<>(this.termsAgreements);
                newAgreements.add(newAgreement);

                return new User(this.id, this.oAuth, this.role, this.lastLogin, this.createdAt,
                                this.totpSecret, this.profile, this.device, newAgreements);
        }

        public User updateLastLogin() {
                return new User(this.id, this.oAuth, this.role, LocalDateTime.now(), this.createdAt,
                                this.totpSecret, this.profile, this.device, this.termsAgreements);
        }

        public User updateRole(Role newRole) {
                return new User(this.id, this.oAuth, newRole, this.lastLogin, this.createdAt,
                                this.totpSecret, this.profile, this.device, this.termsAgreements);
        }

        public boolean isProfileCompleted() {
                return profile != null && profile.isComplete();
        }

        public boolean isLatestTermsAgreed(String currentTermsVersion, TermsType termsType) {
                return termsAgreements.stream()
                                .filter(agreement -> agreement.getTermsType() == termsType)
                                .anyMatch(agreement -> agreement.getTermsAgreedVersion().equals(currentTermsVersion));
        }

        // TOTP 관련 로직 간소화: Secret 설정 시 활성화된 것으로 간주
        public User enableTotp(String secret) {
                return new User(this.id, this.oAuth, this.role, this.lastLogin, this.createdAt,
                                secret, this.profile, this.device, this.termsAgreements);
        }

        public User initiateTotpSetup(String secret) {
                // 셋업 시작 시, 아직 영구 저장 전의 임시 상태라면 DB에는 NULL로 유지하거나,
                // 비즈니스 로직에 따라 Secret을 변경.
                // 보통 initiate 단계에선 DB user를 업데이트하지 않고 세션에서만 처리하므로 이 메서드는 사용처가 줄어들 수 있음.
                // 여기서는 Secret을 업데이트하는 형태로 유지.
                return new User(this.id, this.oAuth, this.role, this.lastLogin, this.createdAt,
                                secret, this.profile, this.device, this.termsAgreements);
        }

        public User disableTotp() {
                return new User(this.id, this.oAuth, this.role, this.lastLogin, this.createdAt,
                                null, this.profile, this.device, this.termsAgreements);
        }
}