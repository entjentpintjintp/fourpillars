package com.kolloseum.fourpillars.application.dto;

import com.kolloseum.fourpillars.domain.model.entity.Terms;
import com.kolloseum.fourpillars.domain.model.enums.TermsType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class TermsResult {
    private final UUID id;
    private final TermsType termsType;
    private final String termsVersion;
    private final String termsContent;
    private final String fileHash;
    private final LocalDateTime createdAt;

    public static TermsResult from(Terms terms) {
        return TermsResult.builder()
                .id(terms.getId())
                .termsType(terms.getTermsType())
                .termsVersion(terms.getTermsVersion())
                .termsContent(terms.getTermsContent())
                .fileHash(terms.getFileHash())
                .createdAt(terms.getCreatedAt())
                .build();
    }
}
