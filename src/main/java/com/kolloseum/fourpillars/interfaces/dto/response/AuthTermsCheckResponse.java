package com.kolloseum.fourpillars.interfaces.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthTermsCheckResponse {
    private final boolean completedProfile;
    private final boolean latestTermsAgreed;

    public static AuthTermsCheckResponse of(boolean completedProfile, boolean latestTermsAgreed) {
        return new AuthTermsCheckResponse(completedProfile, latestTermsAgreed);
    }
}