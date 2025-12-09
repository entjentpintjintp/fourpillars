package com.kolloseum.fourpillars.domain.model.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
public class Device {

    private final UUID id;
    private final String os_version;
    private final String os_name;
    private final LocalDateTime updatedAt;

    public static Device create(String os_version, String os_name) {
        return new Device(null, os_version, os_name, LocalDateTime.now());
    }
    
    public static Device restore(UUID id, String os_version, String os_name, LocalDateTime updatedAt) {
        return new Device(id, os_version, os_name, updatedAt);
    }

    public Device update(String os_version, String os_name) {
        return new Device(this.id, os_version, os_name, LocalDateTime.now());
    }
}