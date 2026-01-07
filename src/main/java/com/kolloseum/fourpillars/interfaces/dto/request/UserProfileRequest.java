package com.kolloseum.fourpillars.interfaces.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserProfileRequest {

    @Pattern(regexp = "^(19[0-9]{2}|[2-9][0-9]{3})(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])$", message = "Lunar birthdate must be in YYYYMMDD format and after 1900-01-01")
    private final String birthdate;

    @Pattern(regexp = "^(ja|chuk|in|myo|jin|sa|o|mi|sin|yu|sul|hae)$", message = "Invalid birth time code")
    private final String birthtime;

    public static UserProfileRequest of(String birthdate, String birthtime) {
        return new UserProfileRequest(birthdate, birthtime);
    }
}