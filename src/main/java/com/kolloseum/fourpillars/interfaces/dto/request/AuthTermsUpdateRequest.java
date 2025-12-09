package com.kolloseum.fourpillars.interfaces.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthTermsUpdateRequest {

    @Valid
    private final TermsAgreementInfo service;

    @Valid
    private final TermsAgreementInfo privacy;

    @NotBlank(message = "OS name is required")
    private final String os_name;

    @NotBlank(message = "OS version is required")
    private final String os_version;

    public static AuthTermsUpdateRequest of(TermsAgreementInfo service, TermsAgreementInfo privacy,
            String osName, String osVersion) {
        return new AuthTermsUpdateRequest(service, privacy, osName, osVersion);
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class TermsAgreementInfo {
        @NotBlank(message = "Terms agreed version is required")
        private final String termsAgreedVersion;

        @NotNull(message = "Terms agreed date is required")
        private final LocalDateTime termsAgreedDate;

        public static TermsAgreementInfo of(String termsAgreedVersion, LocalDateTime termsAgreedDate) {
            return new TermsAgreementInfo(termsAgreedVersion, termsAgreedDate);
        }
    }
}