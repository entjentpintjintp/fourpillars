package com.kolloseum.fourpillars.infrastructure.persistence.mapper;

import com.kolloseum.fourpillars.domain.model.entity.TermsAgreement;
import com.kolloseum.fourpillars.infrastructure.persistence.entity.TermsAgreementEntity;

import java.util.List;
import java.util.stream.Collectors;

public class TermsAgreementMapper {
    
    public static TermsAgreement toDomain(TermsAgreementEntity entity) {
        if (entity == null) {
            return null;
        }
        return TermsAgreement.restore(
            entity.getId(),
            entity.getTermsAgreedVersion(),
            entity.getTermsAgreedDate(),
            entity.getTermsType(),
            entity.getCreatedAt()
        );
    }
    
    public static List<TermsAgreement> toDomainList(List<TermsAgreementEntity> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(TermsAgreementMapper::toDomain)
                .collect(Collectors.toList());
    }
    
    public static TermsAgreementEntity toEntity(TermsAgreement termsAgreement) {
        if (termsAgreement == null) {
            return null;
        }
        TermsAgreementEntity entity = new TermsAgreementEntity();
        entity.setId(termsAgreement.getId());
        entity.setTermsAgreedVersion(termsAgreement.getTermsAgreedVersion());
        entity.setTermsAgreedDate(termsAgreement.getTermsAgreedDate());
        entity.setTermsType(termsAgreement.getTermsType());
        entity.setCreatedAt(termsAgreement.getCreatedAt());
        return entity;
    }
    
    public static List<TermsAgreementEntity> toEntityList(List<TermsAgreement> termsAgreements) {
        if (termsAgreements == null) {
            return null;
        }
        return termsAgreements.stream()
                .map(TermsAgreementMapper::toEntity)
                .collect(Collectors.toList());
    }
}