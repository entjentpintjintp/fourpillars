package com.kolloseum.fourpillars.application.dto;

import com.kolloseum.fourpillars.domain.model.enums.TimeBranch;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserQuery {

    private final LocalDate birthdate;
    private final TimeBranch birthTime;

    public static UserQuery of(LocalDate birthdate, TimeBranch birthTime) {
        return new UserQuery(birthdate, birthTime);
    }

}
