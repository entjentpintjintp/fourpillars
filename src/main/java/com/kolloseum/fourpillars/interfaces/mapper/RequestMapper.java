package com.kolloseum.fourpillars.interfaces.mapper;

import com.kolloseum.fourpillars.domain.model.enums.TimeBranch;
import com.kolloseum.fourpillars.interfaces.dto.request.*;
import com.kolloseum.fourpillars.application.dto.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RequestMapper {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    // AuthLoginRequest → AuthLoginCommand
    public static AuthLoginCommand toAuthLoginCommand(AuthLoginRequest request) {
        return AuthLoginCommand.of(
                request.getAuthorizationCode(),
                request.getCodeVerifier(),
                request.getRedirectUri(),
                request.getProvider());
    }

    // UserProfileRequest → UserCommand
    public static UserCommand toUserCommand(UserProfileRequest request) {
        LocalDate birthdate = LocalDate.parse(request.getBirthdate(), DATE_FORMATTER);
        TimeBranch birthTime = TimeBranch.fromCode(request.getBirthtime());

        return UserCommand.of(birthdate, birthTime);
    }

    // AuthTermsUpdateRequest → AuthTermsAgreementCommand
    public static AuthTermsAgreementCommand toAuthTermsAgreementCommand(AuthTermsUpdateRequest request) {
        String serviceVersion = request.getService() != null ? request.getService().getTermsAgreedVersion() : null;
        String serviceDate = request.getService() != null ? request.getService().getTermsAgreedDate().toString() : null;

        String privacyVersion = request.getPrivacy() != null ? request.getPrivacy().getTermsAgreedVersion() : null;
        String privacyDate = request.getPrivacy() != null ? request.getPrivacy().getTermsAgreedDate().toString() : null;

        return AuthTermsAgreementCommand.of(
                serviceVersion, serviceDate,
                privacyVersion, privacyDate);
    }

    // AuthTermsUpdateRequest → DeviceUpdateCommand
    public static DeviceUpdateCommand toDeviceUpdateCommand(AuthTermsUpdateRequest request) {
        return DeviceUpdateCommand.of(request.getOs_name(), request.getOs_version());
    }

}