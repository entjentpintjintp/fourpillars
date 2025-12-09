package com.kolloseum.fourpillars.domain.service;

import com.kolloseum.fourpillars.domain.model.entity.Terms;  // ← Terms (도메인)
import com.kolloseum.fourpillars.domain.model.enums.TermsType;
import java.util.Map;
import java.util.Optional;

public interface TermsService {

    Optional<Terms> getLatestTerms(TermsType termsType);
    Map<TermsType, Terms> getAllLatestTerms();

    Terms registerNewTermsVersion(TermsType termsType, String version, String content);
    //무결성 검증
    boolean validateTermsIntegrity(Terms terms);
    //특정 버전 약관 조회
    Optional<Terms> getTermsByVersion(TermsType termsType, String version);
}