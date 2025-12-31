package com.kolloseum.fourpillars.domain.model.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
public class Notice {
    private final Long id;
    private String title;
    private String content;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Notice create(String title, String content) {
        return new Notice(null, title, content, null, null);
    }

    public static Notice restore(Long id, String title, String content, LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        return new Notice(id, title, content, createdAt, updatedAt);
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
        this.updatedAt = LocalDateTime.now();
    }
}
