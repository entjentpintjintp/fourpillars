package com.kolloseum.fourpillars.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "devices")
@Getter
@Setter
@NoArgsConstructor
public class DeviceEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)

    private UUID id;

    @Column(name = "os_version")
    private String os_version;

    @Column(name = "os_name")
    private String os_name;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
