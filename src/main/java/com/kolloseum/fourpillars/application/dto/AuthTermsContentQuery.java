package com.kolloseum.fourpillars.application.dto;

import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.AccessLevel;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthTermsContentQuery {
    
    private final String serviceTermsContent;
    private final String serviceTermsVersion;
    private final String privacyTermsContent;
    private final String privacyTermsVersion;
    
    public static AuthTermsContentQuery of(String serviceTermsContent, 
                                         String serviceTermsVersion,
                                         String privacyTermsContent, 
                                         String privacyTermsVersion) {
        return new AuthTermsContentQuery(
            serviceTermsContent, 
            serviceTermsVersion,
            privacyTermsContent, 
            privacyTermsVersion
        );
    }
}