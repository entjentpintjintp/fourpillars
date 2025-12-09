package com.kolloseum.fourpillars.infrastructure.persistence.mapper;

import com.kolloseum.fourpillars.domain.model.entity.Device;
import com.kolloseum.fourpillars.infrastructure.persistence.entity.DeviceEntity;

public class DeviceMapper {

    public static Device toDomain(DeviceEntity deviceEntity) {
        if (deviceEntity == null) {
            return null;
        }
        return Device.restore(
                deviceEntity.getId(),
                deviceEntity.getOs_version(),
                deviceEntity.getOs_name(),
                deviceEntity.getUpdatedAt());
    }

    public static DeviceEntity toEntity(Device device) {
        if (device == null) {
            return null;
        }
        DeviceEntity entity = new DeviceEntity();
        entity.setId(device.getId());
        entity.setOs_version(device.getOs_version());
        entity.setOs_name(device.getOs_name());

        return entity;
    }

    public static void updateEntity(Device device, DeviceEntity entity) {
        if (device == null || entity == null) {
            return;
        }
        entity.setOs_version(device.getOs_version());
        entity.setOs_name(device.getOs_name());

    }
}