package com.kolloseum.fourpillars.application.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DeviceUpdateCommand {

    private final String os_name;
    private final String os_version;

    public static DeviceUpdateCommand of(String os_name, String os_version) {
        return new DeviceUpdateCommand(os_name, os_version);
    }
}
