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
public class Terms {
    private final UUID id;
    private final TermsType termsType;
    private final String termsVersion;
    private final String termsContent;
    private final String fileHash;
    private final LocalDateTime createdAt;

    public static Terms restore(UUID id, TermsType termsType, String termsVersion, String termsContent, String fileHash,
            LocalDateTime createdAt) {
        return new Terms(id, termsType, termsVersion, termsContent, fileHash, createdAt);
    }
}