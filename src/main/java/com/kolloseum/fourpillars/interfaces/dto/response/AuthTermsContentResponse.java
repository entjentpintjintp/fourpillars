package com.kolloseum.fourpillars.interfaces.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthTermsContentResponse {
    private final TermsInfo service;
    private final TermsInfo privacy;

    public static AuthTermsContentResponse of(TermsInfo service, TermsInfo privacy) {
        return new AuthTermsContentResponse(service, privacy);
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class TermsInfo {
        private final String termsContent;
        private final String termsVersion;

        public static TermsInfo of(String termsContent, String termsVersion) {
            return new TermsInfo(termsContent, termsVersion);
        }
    }

}