package com.kolloseum.fourpillars.application.dto;

import com.kolloseum.fourpillars.domain.model.entity.Notice;
import com.kolloseum.fourpillars.infrastructure.persistence.entity.NoticeEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NoticeResult {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static NoticeResult from(NoticeEntity entity) {
        return new NoticeResult(
                entity.getId(),
                entity.getTitle(),
                entity.getContent(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    public static NoticeResult from(Notice notice) {
        return new NoticeResult(
                notice.getId(),
                notice.getTitle(),
                notice.getContent(),
                notice.getCreatedAt(),
                notice.getUpdatedAt());
    }
}
