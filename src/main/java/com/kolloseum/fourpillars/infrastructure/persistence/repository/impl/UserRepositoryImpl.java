package com.kolloseum.fourpillars.infrastructure.persistence.repository.impl;

import com.kolloseum.fourpillars.domain.model.entity.User;
import com.kolloseum.fourpillars.domain.model.enums.TermsType;
import com.kolloseum.fourpillars.domain.model.vo.OAuth;
import com.kolloseum.fourpillars.domain.model.vo.TermsAgreementStatus;
import com.kolloseum.fourpillars.domain.repository.UserRepository;
import com.kolloseum.fourpillars.infrastructure.persistence.entity.UserEntity;
import com.kolloseum.fourpillars.infrastructure.persistence.mapper.UserMapper;
import com.kolloseum.fourpillars.infrastructure.persistence.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    @Transactional
    public User save(User user) {
        UserEntity entity = UserMapper.toEntity(user);
        UserEntity savedEntity = userJpaRepository.save(entity);
        return UserMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<User> findByOAuth(OAuth oAuth) {
        return userJpaRepository.findByProviderAndSocialId(oAuth.getProvider(), oAuth.getSocialId())
                .map(UserMapper::toDomain);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userJpaRepository.findById(id)
                .map(UserMapper::toDomain);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        userJpaRepository.deleteById(id);
    }

    @Override
    public Optional<User> findByIdWithProfile(UUID id) {
        return userJpaRepository.findByIdWithProfile(id)
                .map(UserMapper::toDomain);
    }

    @Override
    public Optional<User> findByIdWithDevice(UUID id) {
        return userJpaRepository.findByIdWithDevice(id)
                .map(UserMapper::toDomain);
    }

    @Override
    public Optional<User> findByIdWithTermsAgreements(UUID id) {
        return userJpaRepository.findByIdWithTermsAgreements(id)
                .map(UserMapper::toDomain);
    }

    @Override
    public Optional<User> findByOAuthWithTermsAgreements(OAuth oAuth) {
        return userJpaRepository.findByProviderAndSocialIdWithTermsAgreements(oAuth.getProvider(), oAuth.getSocialId())
                .map(UserMapper::toDomain);
    }

    @Override
    public Optional<TermsAgreementStatus> getTermsAgreementStatus(UUID userId, TermsType termsType) {
        Optional<String> agreedVersion = userJpaRepository.findLatestAgreedVersionByUserIdAndTermsType(userId, termsType);

        return agreedVersion.map(s -> TermsAgreementStatus.agreed(termsType, s))
                .or(() -> Optional.of(TermsAgreementStatus.notAgreed(termsType)));
    }

}