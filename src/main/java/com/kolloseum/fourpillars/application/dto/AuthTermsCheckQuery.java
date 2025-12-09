package com.kolloseum.fourpillars.application.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthTermsCheckQuery {

    private final boolean completedProfile;
    private final boolean latestTermsAgreed;

    public static AuthTermsCheckQuery of(boolean completedProfile, boolean latestTermsAgreed) {
        return new AuthTermsCheckQuery(completedProfile, latestTermsAgreed);
    }

}
