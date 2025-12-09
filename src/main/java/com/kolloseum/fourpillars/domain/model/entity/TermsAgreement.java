package com.kolloseum.fourpillars.domain.model.entity;

import com.kolloseum.fourpillars.domain.model.enums.TermsType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
public class TermsAgreement {

    private final UUID id;
    private final String termsAgreedVersion;
    private final LocalDateTime termsAgreedDate;
    private final TermsType termsType;
    private final LocalDateTime createdAt;

    public static TermsAgreement create(String termsAgreedVersion, TermsType termsType) {
        return new TermsAgreement(null, termsAgreedVersion, LocalDateTime.now(), termsType, LocalDateTime.now());
    }
    
    public static TermsAgreement restore(UUID id, String termsAgreedVersion, LocalDateTime termsAgreedDate, TermsType termsType, LocalDateTime createdAt) {
        return new TermsAgreement(id, termsAgreedVersion, termsAgreedDate, termsType, createdAt);
    }
}