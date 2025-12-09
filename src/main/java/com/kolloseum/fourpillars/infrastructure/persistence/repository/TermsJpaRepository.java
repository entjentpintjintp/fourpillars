package com.kolloseum.fourpillars.infrastructure.persistence.repository;

import com.kolloseum.fourpillars.domain.model.enums.TermsType;
import com.kolloseum.fourpillars.infrastructure.persistence.entity.TermsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TermsJpaRepository extends JpaRepository<TermsEntity, UUID> {

    Optional<TermsEntity> findByTermsTypeAndTermsVersion(TermsType termsType, String version);

    List<TermsEntity> findAllByOrderByCreatedAtDesc();

    Optional<TermsEntity> findFirstByTermsTypeOrderByCreatedAtDesc(TermsType termsType);

    default Optional<TermsEntity> findLatestByTermsType(TermsType termsType) {
        return findFirstByTermsTypeOrderByCreatedAtDesc(termsType);
    }
}