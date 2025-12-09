package com.kolloseum.fourpillars.infrastructure.persistence.entity;

import com.kolloseum.fourpillars.domain.model.enums.TermsType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "terms", uniqueConstraints = {
                @UniqueConstraint(name = "uk_terms_type_version", columnNames = { "termsType", "termsVersion" })
})
@Getter
@Setter
@NoArgsConstructor
public class TermsEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private UUID id;

        @Enumerated(EnumType.STRING)
        private TermsType termsType;

        private String termsVersion;

        @Lob
        @Column(name = "terms_content", columnDefinition = "TEXT")
        private String termsContent;

        @Column(name = "file_hash")
        private String fileHash; // 파일 무결성 검증용

        @Column(nullable = false)
        private LocalDateTime createdAt;
}