package com.kolloseum.fourpillars.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "notices")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoticeEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    public NoticeEntity(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public static NoticeEntity create(String title, String content) {
        return new NoticeEntity(title, content);
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
