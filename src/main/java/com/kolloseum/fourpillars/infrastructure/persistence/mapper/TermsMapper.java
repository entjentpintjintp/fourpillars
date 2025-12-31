package com.kolloseum.fourpillars.infrastructure.persistence.mapper;

import com.kolloseum.fourpillars.domain.model.entity.Terms;
import com.kolloseum.fourpillars.infrastructure.persistence.entity.TermsEntity;

public class TermsMapper {

    public static Terms toDomain(TermsEntity termsEntity) {
        if (termsEntity == null) {
            return null;
        }
        return Terms.restore(
                termsEntity.getId(),
                termsEntity.getTermsType(),
                termsEntity.getTermsVersion(),
                termsEntity.getTermsContent(),
                termsEntity.getFileHash(),
                termsEntity.getCreatedAt());
    }

    public static TermsEntity toEntity(Terms terms) {
        if (terms == null) {
            return null;
        }
        TermsEntity entity = new TermsEntity();
        if (terms.getId() != null) {
            entity.setId(terms.getId());
        }

        entity.setTermsType(terms.getTermsType());
        entity.setTermsVersion(terms.getTermsVersion());
        entity.setTermsContent(terms.getTermsContent());
        entity.setFileHash(terms.getFileHash());
        entity.setCreatedAt(terms.getCreatedAt());
        return entity;
    }
}