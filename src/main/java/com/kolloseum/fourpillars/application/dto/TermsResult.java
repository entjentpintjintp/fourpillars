package com.kolloseum.fourpillars.application.dto;

import com.kolloseum.fourpillars.domain.model.entity.Terms;
import com.kolloseum.fourpillars.domain.model.enums.TermsType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TermsResult {
    private final UUID id;
    private final TermsType termsType;
    private final String termsVersion;
    private final String termsContent;
    private final String fileHash;
    private final LocalDateTime createdAt;

    public static TermsResult from(Terms terms) {
        return new TermsResult(
                terms.getId(),
                terms.getTermsType(),
                terms.getTermsVersion(),
                terms.getTermsContent(),
                terms.getFileHash(),
                terms.getCreatedAt());
    }
}
