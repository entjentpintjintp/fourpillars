package com.kolloseum.fourpillars.interfaces.dto.response;

import com.kolloseum.fourpillars.application.dto.NoticeResult;
import com.kolloseum.fourpillars.infrastructure.persistence.entity.NoticeEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NoticeListResponse {
    private Long id;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static NoticeListResponse from(NoticeEntity entity) {
        return NoticeListResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static NoticeListResponse from(NoticeResult result) {
        return NoticeListResponse.builder()
                .id(result.getId())
                .title(result.getTitle())
                .createdAt(result.getCreatedAt())
                .updatedAt(result.getUpdatedAt())
                .build();
    }
}
