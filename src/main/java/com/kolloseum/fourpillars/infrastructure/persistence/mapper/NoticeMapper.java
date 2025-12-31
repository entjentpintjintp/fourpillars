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
        // If ID exists, it's an update or existing entity.
        // However, usually we create a new entity for insert, or update an existing
        // managed entity.
        // For simplicity in save(), we can reconstruct.
        // BUT, NoticeEntity constructor signature was changed to (String, String).
        // Reflection or a different factory might be needed if we want to set ID/dates
        // manually on Entity,
        // OR we just use it for Creation.

        // Actually, JPA Repository save(entity) handles ID.
        // If we want to support 'update' via toEntity, we might need a way to set ID on
        // NoticeEntity
        // which might be protected or not exposed.
        // Let's check NoticeEntity again.

        // Checking NoticeEntity again... it extends BaseTimeEntity.
        // It has @NoArgsConstructor(access = Protected). Use Reflection or just map
        // what we can.
        // For strict DDD, the RepositoryImpl finds the Entity and updates fields.
        // But for 'create', we convert Domain -> Entity.

        return NoticeEntity.create(notice.getTitle(), notice.getContent());
    }
}
