package com.kolloseum.fourpillars.domain.service;

import com.kolloseum.fourpillars.domain.model.enums.TermsType;

import java.util.Optional;

public interface TermsFileService {
    

    Optional<String> getTermsContent(TermsType termsType, String version);
    String calculateFileHash(String content);
    boolean validateTermsIntegrity(TermsType termsType, String version, 
                                 String backupContent, String expectedHash);
}