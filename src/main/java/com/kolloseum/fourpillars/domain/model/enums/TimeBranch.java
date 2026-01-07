package com.kolloseum.fourpillars.domain.model.enums;

import com.kolloseum.fourpillars.common.exception.BusinessException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum TimeBranch {
    JA("ja", 0),
    CHUK("chuk", 1),
    IN("in", 2),
    MYO("myo", 3),
    JIN("jin", 4),
    SA("sa", 5),
    O("o", 6),
    MI("mi", 7),
    SIN("sin", 8),
    YU("yu", 9),
    SUL("sul", 10),
    HAE("hae", 11);

    private final String code;
    private final int index;

    private static final Map<String, TimeBranch> CODE_MAP = Arrays.stream(values())
            .collect(Collectors.toMap(TimeBranch::getCode, Function.identity()));

    public static TimeBranch fromCode(String code) {
        if (code == null) {
            return null;
        }
        TimeBranch branch = CODE_MAP.get(code.toLowerCase());
        if (branch == null) {
            throw BusinessException.invalidRequest("Invalid birth time code: " + code);
        }
        return branch;
    }
}
