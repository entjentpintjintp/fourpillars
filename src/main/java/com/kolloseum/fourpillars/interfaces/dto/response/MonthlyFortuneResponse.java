package com.kolloseum.fourpillars.interfaces.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MonthlyFortuneResponse {

    private final String affection;
    private final String wealth;
    private final String misc;

    public static MonthlyFortuneResponse of(String affection, String wealth, String misc) {
        return new MonthlyFortuneResponse(affection, wealth, misc);
    }
}
