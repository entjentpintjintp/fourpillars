package com.kolloseum.fourpillars.application.dto;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserCommand {

    private final LocalDate birthdate;

    public static UserCommand of(LocalDate birthdate) {
        return new UserCommand(birthdate);
    }
}
