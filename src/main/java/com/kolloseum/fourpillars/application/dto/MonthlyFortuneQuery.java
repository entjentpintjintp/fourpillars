package com.kolloseum.fourpillars.application.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MonthlyFortuneQuery {

    private final String affection;
    private final String wealth;
    private final String misc;

    public static MonthlyFortuneQuery of(String affection, String wealth, String misc) {
        return new MonthlyFortuneQuery(affection, wealth, misc);
    }
}
