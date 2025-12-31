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
public class NoticeListResponse {
    private Long id;
    private String title;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    public static NoticeListResponse from(NoticeEntity entity) {
        return new NoticeListResponse(
                entity.getId(),
                entity.getTitle(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    public static NoticeListResponse from(NoticeResult result) {
        return new NoticeListResponse(
                result.getId(),
                result.getTitle(),
                result.getCreatedAt(),
                result.getUpdatedAt());
    }
}
