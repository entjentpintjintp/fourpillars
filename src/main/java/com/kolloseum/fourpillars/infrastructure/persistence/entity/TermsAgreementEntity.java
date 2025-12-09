package com.kolloseum.fourpillars.infrastructure.persistence.entity;

import com.kolloseum.fourpillars.domain.model.enums.TermsType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "terms_agreements")
@Getter
@Setter
@NoArgsConstructor
public class TermsAgreementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)

    private UUID id;

    private String termsAgreedVersion;
    private LocalDateTime termsAgreedDate;

    @Enumerated(EnumType.STRING)
    private TermsType termsType;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;


}
