package com.kolloseum.fourpillars.interfaces.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserProfileResponse {

    private final String birthdate;
    private final String birthTime;

    public static UserProfileResponse of(String birthdate, String birthTime) {
        return new UserProfileResponse(birthdate, birthTime);
    }
}
