package com.kolloseum.fourpillars.application.service.impl;

import com.kolloseum.fourpillars.domain.model.entity.Terms;
import com.kolloseum.fourpillars.domain.model.enums.TermsType;
import com.kolloseum.fourpillars.domain.repository.TermsRepository;
import com.kolloseum.fourpillars.domain.service.TermsService;
import com.kolloseum.fourpillars.domain.service.TermsFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TermsServiceImpl implements TermsService {

    private final TermsRepository termsRepository;
    private final TermsFileService termsFileService;

    @Override
    public Optional<Terms> getLatestTerms(TermsType termsType) {
        return termsRepository.findLatestByType(termsType);
    }

    @Override
    public Map<TermsType, Terms> getAllLatestTerms() {
        Map<TermsType, Terms> result = new EnumMap<>(TermsType.class);

        for (TermsType type : TermsType.values()) {
            getLatestTerms(type).ifPresent(terms -> result.put(type, terms));
        }

        return result;
    }

    @Override
    @Transactional
    public Terms registerNewTermsVersion(TermsType termsType, String version, String content) {
        // 해시 계산
        String contentHash = termsFileService.calculateFileHash(content);

        // 새 약관 엔티티 생성 (restore 메서드 사용)
        Terms newTerms = Terms.restore(
                null,
                termsType,
                version,
                content,
                contentHash,
                LocalDateTime.now());

        Terms savedTerms = termsRepository.save(newTerms);
        log.info("Successfully registered new terms version: {} v{}", termsType, version);

        return savedTerms;
    }

    @Override
    public boolean validateTermsIntegrity(Terms terms) {
        return termsFileService.validateTermsIntegrity(
                terms.getTermsType(),
                terms.getTermsVersion(),
                terms.getTermsContent(),
                terms.getFileHash());
    }

    @Override
    public Optional<Terms> getTermsByVersion(TermsType termsType, String version) {
        return termsRepository.findByTermsTypeAndVersion(termsType, version);
    }
}