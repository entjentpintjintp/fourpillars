package com.kolloseum.fourpillars.interfaces.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kolloseum.fourpillars.application.dto.NoticeResult;
import com.kolloseum.fourpillars.infrastructure.persistence.entity.NoticeEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NoticeDetailResponse {
    private Long id;
    private String title;
    private String content;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    public static NoticeDetailResponse from(NoticeEntity entity) {
        return new NoticeDetailResponse(
                entity.getId(),
                entity.getTitle(),
                entity.getContent(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    public static NoticeDetailResponse from(NoticeResult result) {
        return new NoticeDetailResponse(
                result.getId(),
                result.getTitle(),
                result.getContent(),
                result.getCreatedAt(),
                result.getUpdatedAt());
    }
}
