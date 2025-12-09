package com.kolloseum.fourpillars.interfaces.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FortuneResponse {

    private final String fortuneContent;
    private final String adviceBox;
    private final String symbol;
    private final String symbolChn;

    public static FortuneResponse of(String fortuneContent, String adviceBox, String symbol, String symbolChn) {
        return new FortuneResponse(fortuneContent, adviceBox, symbol, symbolChn);
    }

}
