package com.kolloseum.fourpillars.application.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FourPillarsResult {

    private final String yearly;
    private final String monthly;
    private final String daily;
    private final String timely;

    public static FourPillarsResult of(String yearly, String monthly, String daily, String timely) {
        return new FourPillarsResult(yearly, monthly, daily, timely);
    }
}
