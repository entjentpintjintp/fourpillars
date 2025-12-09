package com.kolloseum.fourpillars.domain.repository;

import com.kolloseum.fourpillars.domain.model.entity.Terms;
import com.kolloseum.fourpillars.domain.model.enums.TermsType;

import java.util.Optional;

public interface TermsRepository {
    Optional<Terms> findLatestByType(TermsType termsType);
    Terms save(Terms terms);
    Optional<Terms> findByTermsTypeAndVersion(TermsType termsType, String version); // ← 추가 필요
}