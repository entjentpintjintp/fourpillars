package com.kolloseum.fourpillars.infrastructure.persistence.repository;

import com.kolloseum.fourpillars.domain.model.enums.Provider;
import com.kolloseum.fourpillars.domain.model.enums.TermsType;
import com.kolloseum.fourpillars.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {

    @Query("SELECT u FROM UserEntity u WHERE u.provider = :provider AND u.socialId = :socialId")
    Optional<UserEntity> findByProviderAndSocialId(
        @Param("provider") Provider provider, 
        @Param("socialId") String socialId
    );

    @Query("SELECT u FROM UserEntity u LEFT JOIN FETCH u.profile WHERE u.id = :id")
    Optional<UserEntity> findByIdWithProfile(@Param("id") UUID id);

    @Query("SELECT u FROM UserEntity u LEFT JOIN FETCH u.device WHERE u.id = :id")
    Optional<UserEntity> findByIdWithDevice(@Param("id") UUID id);

    @Query("SELECT u FROM UserEntity u LEFT JOIN FETCH u.termsAgreements WHERE u.id = :id")
    Optional<UserEntity> findByIdWithTermsAgreements(@Param("id") UUID id);

    @Query("SELECT u FROM UserEntity u LEFT JOIN FETCH u.termsAgreements WHERE u.provider = :provider AND u.socialId = :socialId")
    Optional<UserEntity> findByProviderAndSocialIdWithTermsAgreements(
        @Param("provider") Provider provider, 
        @Param("socialId") String socialId
    );
    
    @Query("SELECT ta.termsAgreedVersion FROM UserEntity u " +
           "JOIN u.termsAgreements ta WHERE u.id = :userId AND ta.termsType = :termsType " +
           "ORDER BY ta.termsAgreedDate DESC")
    List<String> findAgreedVersionsByUserIdAndTermsType(@Param("userId") UUID userId, @Param("termsType") TermsType termsType, Pageable pageable);
    
    default Optional<String> findLatestAgreedVersionByUserIdAndTermsType(UUID userId, TermsType termsType) {
        List<String> results = findAgreedVersionsByUserIdAndTermsType(userId, termsType, Pageable.ofSize(1));
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
}