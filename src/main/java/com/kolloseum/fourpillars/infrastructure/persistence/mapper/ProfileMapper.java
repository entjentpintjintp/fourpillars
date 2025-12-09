package com.kolloseum.fourpillars.infrastructure.persistence.mapper;

import com.kolloseum.fourpillars.domain.model.entity.Profile;
import com.kolloseum.fourpillars.infrastructure.persistence.entity.ProfileEntity;

public class ProfileMapper {

    public static Profile toDomain(ProfileEntity profileEntity) {
        if (profileEntity == null) {
            return null;
        }
        return Profile.restore(
                profileEntity.getId(),
                profileEntity.getBirthdate(),
                profileEntity.getUpdatedAt());
    }

    public static ProfileEntity toEntity(Profile profile) {
        if (profile == null) {
            return null;
        }
        ProfileEntity entity = new ProfileEntity();
        entity.setId(profile.getId());
        entity.setBirthdate(profile.getBirthdate());

        return entity;
    }

    public static void updateEntity(Profile profile, ProfileEntity entity) {
        if (profile == null || entity == null) {
            return;
        }
        entity.setBirthdate(profile.getBirthdate());

    }
}