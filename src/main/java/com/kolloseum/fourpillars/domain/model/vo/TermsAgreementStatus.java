package com.kolloseum.fourpillars.domain.model.vo;

import com.kolloseum.fourpillars.domain.model.enums.TermsType;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@ToString
public class TermsAgreementStatus {
    
    private final TermsType termsType;
    private final boolean agreed;
    private final String agreedVersion;
    
    public static TermsAgreementStatus notAgreed(TermsType termsType) {
        return new TermsAgreementStatus(termsType, false, null);
    }
    
    public static TermsAgreementStatus agreed(TermsType termsType, String agreedVersion) {
        return new TermsAgreementStatus(termsType, true, agreedVersion);
    }
}