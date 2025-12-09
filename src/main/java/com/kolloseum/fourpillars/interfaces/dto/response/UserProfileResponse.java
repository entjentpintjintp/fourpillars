package com.kolloseum.fourpillars.interfaces.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserProfileResponse {

    private final String birthdate;

    public static UserProfileResponse of(String birthdate) {
        return new UserProfileResponse(birthdate);
    }
}
