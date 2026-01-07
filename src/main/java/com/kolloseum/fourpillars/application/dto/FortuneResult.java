package com.kolloseum.fourpillars.application.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FortuneResult {

    private final String yearly;
    private final String monthly;
    private final String daily;
    private final String timely;

    public static FortuneResult of(String yearly, String monthly, String daily, String timely) {
        return new FortuneResult(yearly, monthly, daily, timely);
    }
}
