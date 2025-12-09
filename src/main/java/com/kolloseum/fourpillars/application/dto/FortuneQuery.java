package com.kolloseum.fourpillars.application.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FortuneQuery {

    private final String fortuneContent;
    private final String adviceBox;
    private final String symbol;
    private final String symbolChn;

    public static FortuneQuery of(String fortuneContent, String adviceBox, String symbol, String symbolChn) {
        return new FortuneQuery(fortuneContent, adviceBox, symbol, symbolChn);
    }
}
