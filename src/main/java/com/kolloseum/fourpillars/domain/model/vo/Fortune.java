package com.kolloseum.fourpillars.domain.model.vo;

import lombok.*;
import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "fortuneCode")
@ToString
public class Fortune {

    private final String fortuneCode;
    private final String fortuneContent;

    // Monthly Fortunes (12 months)
    private final List<MonthlyFortune> monthlyFortunes;

    // Metadata
    private final String adviceBox;
    private final String symbol;
    private final String symbolChn;

    public static Fortune create(
            String fortuneCode,
            String fortuneContent,
            List<MonthlyFortune> monthlyFortunes,
            String adviceBox,
            String symbol,
            String symbolChn) {
        return new Fortune(
                fortuneCode,
                fortuneContent,
                monthlyFortunes != null ? List.copyOf(monthlyFortunes) : List.of(),
                adviceBox,
                symbol,
                symbolChn);
    }

    public boolean isValidCode() {
        return fortuneCode != null && fortuneCode.matches("\\d{3}");
    }
}
