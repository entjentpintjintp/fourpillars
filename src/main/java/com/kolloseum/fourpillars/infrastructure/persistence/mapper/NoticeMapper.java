package com.kolloseum.fourpillars.infrastructure.persistence.mapper;

import com.kolloseum.fourpillars.domain.model.entity.Notice;
import com.kolloseum.fourpillars.infrastructure.persistence.entity.NoticeEntity;

public class NoticeMapper {

    public static Notice toDomain(NoticeEntity entity) {
        if (entity == null) {
            return null;
        }
        return Notice.restore(
                entity.getId(),
                entity.getTitle(),
                entity.getContent(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    public static NoticeEntity toEntity(Notice notice) {
        if (notice == null) {
            return null;
        }
        NoticeEntity entity = NoticeEntity.create(notice.getTitle(), notice.getContent());
        if (notice.getId() != null) {
            entity.setId(notice.getId());
        }
        return entity;
    }
}
