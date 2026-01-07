package com.kolloseum.fourpillars.infrastructure.persistence.repository.impl;

import com.kolloseum.fourpillars.domain.model.entity.Terms;
import com.kolloseum.fourpillars.domain.model.enums.TermsType;
import com.kolloseum.fourpillars.domain.repository.TermsRepository;

import com.kolloseum.fourpillars.infrastructure.persistence.mapper.TermsMapper;
import com.kolloseum.fourpillars.infrastructure.persistence.repository.TermsJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TermsRepositoryImpl implements TermsRepository {

    private final TermsJpaRepository termsJpaRepository;

    @Override
    public Optional<Terms> findLatestByType(TermsType termsType) {
        return termsJpaRepository.findLatestByTermsType(termsType)
                .map(TermsMapper::toDomain);
    }

    @Override
    public Terms save(Terms terms) {
        return TermsMapper.toDomain(
                termsJpaRepository.save(TermsMapper.toEntity(terms)));
    }

    @Override
    public Optional<Terms> findByTermsTypeAndVersion(TermsType termsType, String version) {
        return termsJpaRepository.findByTermsTypeAndTermsVersion(termsType, version)
                .map(TermsMapper::toDomain);
    }

    @Override
    public java.util.List<Terms> findAllByOrderByCreatedAtDesc() {
        return termsJpaRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(TermsMapper::toDomain)
                .collect(java.util.stream.Collectors.toList());
    }
}