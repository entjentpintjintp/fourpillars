package com.kolloseum.fourpillars.domain.repository;

import com.kolloseum.fourpillars.domain.model.entity.User;
import com.kolloseum.fourpillars.domain.model.enums.TermsType;
import com.kolloseum.fourpillars.domain.model.vo.OAuth;
import com.kolloseum.fourpillars.domain.model.vo.TermsAgreementStatus;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    User save(User user);
    Optional<User> findByOAuth(OAuth oAuth);
    Optional<User> findById(UUID id);
    void delete(UUID id);

    Optional<User> findByIdWithProfile(UUID id);
    Optional<User> findByIdWithDevice(UUID id);
    Optional<User> findByIdWithTermsAgreements(UUID id);
    Optional<User> findByOAuthWithTermsAgreements(OAuth oAuth);
    
    Optional<TermsAgreementStatus> getTermsAgreementStatus(UUID userId, TermsType termsType);

}