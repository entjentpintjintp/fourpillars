package com.kolloseum.fourpillars.application.service;

import com.kolloseum.fourpillars.application.dto.AuthTermsAgreementCommand;
import com.kolloseum.fourpillars.application.dto.AuthTermsCheckQuery;
import com.kolloseum.fourpillars.application.dto.AuthTermsContentQuery;
import com.kolloseum.fourpillars.application.dto.AuthTokenInput;
import com.kolloseum.fourpillars.application.dto.DeviceUpdateCommand;
import com.kolloseum.fourpillars.domain.model.vo.TokenPayload;

public interface AuthorizationService {

    // TokenPayload + 원본 토큰 문자열 모두 받기
    AuthTokenInput updateTermsAgreementAndDeviceInfo(TokenPayload temporaryTokenPayload,
            String temporaryTokenString,
            AuthTermsAgreementCommand termsAgreement,
            DeviceUpdateCommand deviceUpdate);

    AuthTermsCheckQuery checkTermsStatus(TokenPayload tokenPayload);

    AuthTermsContentQuery getTermsContent(TokenPayload tokenPayload);

    String getPublicTermsContent(String type);
}