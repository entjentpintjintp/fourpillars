package com.kolloseum.fourpillars.domain.model.vo;

import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@ToString
public class MonthlyFortune {

    private final String affection;
    private final String wealth;
    private final String misc;

    public static MonthlyFortune create(String affection, String wealth, String misc) {
        return new MonthlyFortune(
                affection != null ? affection : "",
                wealth != null ? wealth : "",
                misc != null ? misc : "");
    }
}
