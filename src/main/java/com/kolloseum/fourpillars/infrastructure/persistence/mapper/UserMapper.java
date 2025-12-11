package com.kolloseum.fourpillars.infrastructure.persistence.mapper;

import com.kolloseum.fourpillars.domain.model.entity.TermsAgreement;
import com.kolloseum.fourpillars.domain.model.entity.User;
import com.kolloseum.fourpillars.domain.model.vo.OAuth;
import com.kolloseum.fourpillars.infrastructure.persistence.entity.TermsAgreementEntity;
import com.kolloseum.fourpillars.infrastructure.persistence.entity.UserEntity;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    public static User toDomain(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }

        OAuth oAuth = OAuth.of(userEntity.getSocialId(), userEntity.getProvider());

        return User.restore(
                userEntity.getId(),
                oAuth,
                userEntity.getRole(),
                userEntity.getLastLogin(),
                userEntity.getCreatedAt(),
                userEntity.getTotpSecret(),
                ProfileMapper.toDomain(userEntity.getProfile()),
                DeviceMapper.toDomain(userEntity.getDevice()),
                TermsAgreementMapper.toDomainList(userEntity.getTermsAgreements()));
    }

    public static UserEntity toEntity(User user) {
        if (user == null) {
            return null;
        }

        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setRole(user.getRole());
        entity.setLastLogin(user.getLastLogin());
        entity.setTotpSecret(user.getTotpSecret());

        if (user.getOAuth() != null) {
            entity.setSocialId(user.getOAuth().getSocialId());
            entity.setProvider(user.getOAuth().getProvider());
        }

        if (user.getProfile() != null) {
            entity.setProfile(ProfileMapper.toEntity(user.getProfile()));
        }

        if (user.getDevice() != null) {
            entity.setDevice(DeviceMapper.toEntity(user.getDevice()));
        }

        if (user.getTermsAgreements() != null && !user.getTermsAgreements().isEmpty()) {
            List<TermsAgreementEntity> entities = TermsAgreementMapper.toEntityList(user.getTermsAgreements());
            if (entities != null) {
                entities.forEach(agreement -> {
                    agreement.setUser(entity);
                    entity.getTermsAgreements().add(agreement);
                });
            }
        }

        return entity;
    }

    public static void updateLastLogin(User user, UserEntity entity) {
        if (user == null || entity == null) {
            return;
        }
        entity.setLastLogin(user.getLastLogin());
    }

    public static void updateRole(User user, UserEntity entity) {
        if (user == null || entity == null) {
            return;
        }
        entity.setRole(user.getRole());
    }

    public static void updateProfile(User user, UserEntity entity) {
        if (user == null || entity == null) {
            return;
        }

        if (user.getProfile() != null) {
            if (entity.getProfile() == null) {
                entity.setProfile(ProfileMapper.toEntity(user.getProfile()));
            } else {
                ProfileMapper.updateEntity(user.getProfile(), entity.getProfile());
            }
        } else {
            entity.setProfile(null);
        }
    }

    public static void updateDevice(User user, UserEntity entity) {
        if (user == null || entity == null) {
            return;
        }

        if (user.getDevice() != null) {
            if (entity.getDevice() == null) {
                entity.setDevice(DeviceMapper.toEntity(user.getDevice()));
            } else {
                DeviceMapper.updateEntity(user.getDevice(), entity.getDevice());
            }
        } else {
            entity.setDevice(null);
        }
    }

    public static void addTermsAgreement(TermsAgreement newAgreement, UserEntity entity) {
        if (newAgreement == null || entity == null) {
            return;
        }

        TermsAgreementEntity agreementEntity = TermsAgreementMapper.toEntity(newAgreement);
        if (agreementEntity != null) {
            agreementEntity.setUser(entity);
            entity.getTermsAgreements().add(agreementEntity);
        }
    }
}