package com.kolloseum.fourpillars.application.dto;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthTermsAgreementCommand {

    private final String serviceTermsAgreedVersion;
    private final String serviceTermsAgreedDate;
    private final String privacyTermsAgreedVersion;
    private final String privacyTermsAgreedDate;

    public static AuthTermsAgreementCommand of(String serviceTermsAgreedVersion, String serviceTermsAgreedDate,
                                               String privacyTermsAgreedVersion, String privacyTermsAgreedDate) {
        return new AuthTermsAgreementCommand(serviceTermsAgreedVersion, serviceTermsAgreedDate, privacyTermsAgreedVersion, privacyTermsAgreedDate);
    }

}
