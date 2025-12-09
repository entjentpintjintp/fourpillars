package com.kolloseum.fourpillars.application.service;

import com.kolloseum.fourpillars.application.dto.UserCommand;
import com.kolloseum.fourpillars.application.dto.UserQuery;
import com.kolloseum.fourpillars.domain.model.vo.TokenPayload;

public interface UserService {

    UserQuery getUserInfo(TokenPayload tokenPayload);
    void updateProfile(TokenPayload tokenPayload, UserCommand userCommand);
    void withdraw(TokenPayload headerTokenPayload, String accessToken, String refreshToken);
}