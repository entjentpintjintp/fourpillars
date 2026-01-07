package com.kolloseum.fourpillars.interfaces.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserProfileResponse {

    private final String birthdate;
    private final String birthtime;

    public static UserProfileResponse of(String birthdate, String birthtime) {
        return new UserProfileResponse(birthdate, birthtime);
    }
}
