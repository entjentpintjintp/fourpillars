package com.kolloseum.fourpillars.infrastructure.persistence.entity;

import com.kolloseum.fourpillars.domain.model.enums.Provider;
import com.kolloseum.fourpillars.domain.model.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users", uniqueConstraints = {
                @UniqueConstraint(name = "uk_users_provider_social_id", columnNames = { "provider", "socialId" })
})
@Getter
@Setter
@NoArgsConstructor
public class UserEntity extends BaseTimeEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private UUID id;

        @Enumerated(EnumType.STRING)
        private Provider provider;

        private String socialId;

        @Enumerated(EnumType.STRING)
        private Role role;

        private LocalDateTime lastLogin;

        private String totpSecret;

        private boolean isTotpEnabled;

        @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
        private ProfileEntity profile;

        @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
        private DeviceEntity device;

        @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<TermsAgreementEntity> termsAgreements = new ArrayList<>();

        public void setProfile(ProfileEntity profile) {
                this.profile = profile;
                if (profile != null) {
                        profile.setUser(this);
                }
        }

        public void setDevice(DeviceEntity device) {
                this.device = device;
                if (device != null) {
                        device.setUser(this);
                }
        }

        public void addTermsAgreement(TermsAgreementEntity termsAgreement) {
                this.termsAgreements.add(termsAgreement);
                termsAgreement.setUser(this);
        }
}
