package com.kolloseum.fourpillars.application.service;

import com.kolloseum.fourpillars.application.dto.AuthLoginCommand;
import com.kolloseum.fourpillars.application.dto.AuthTokenInput;
import com.kolloseum.fourpillars.domain.model.vo.OAuth2Request;

public interface AuthenticationService {

    AuthTokenInput login(AuthLoginCommand command);

    AuthTokenInput refreshToken(String refreshToken);

    void logout(String accessToken, String refreshToken);
}
