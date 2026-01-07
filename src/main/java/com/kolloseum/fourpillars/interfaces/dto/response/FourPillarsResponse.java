package com.kolloseum.fourpillars.interfaces.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FourPillarsResponse {

    private final String yearly;
    private final String monthly;
    private final String daily;
    private final String timely;

    public static FourPillarsResponse of(String yearly, String monthly, String daily, String timely) {
        return new FourPillarsResponse(yearly, monthly, daily, timely);
    }
}
