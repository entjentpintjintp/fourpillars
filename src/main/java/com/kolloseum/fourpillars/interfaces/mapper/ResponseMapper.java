package com.kolloseum.fourpillars.interfaces.mapper;

import com.kolloseum.fourpillars.interfaces.dto.response.*;
import com.kolloseum.fourpillars.application.dto.*;

public class ResponseMapper {

    // AuthTokenInput → AuthTokenResponse
    public static AuthTokenResponse toAuthTokenResponse(AuthTokenInput input) {
        return AuthTokenResponse.of(input.getAccessToken(), input.getRefreshToken());
    }

    // AuthTermsCheckQuery → AuthTermsCheckResponse
    public static AuthTermsCheckResponse toAuthTermsCheckResponse(AuthTermsCheckQuery query) {
        return AuthTermsCheckResponse.of(query.isCompletedProfile(), query.isLatestTermsAgreed());
    }

    // AuthTermsContentQuery → AuthTermsContentResponse
    public static AuthTermsContentResponse toAuthTermsContentResponse(AuthTermsContentQuery query) {
        return AuthTermsContentResponse.of(
                AuthTermsContentResponse.TermsInfo.of(query.getServiceTermsContent(), query.getServiceTermsVersion()),
                AuthTermsContentResponse.TermsInfo.of(query.getPrivacyTermsContent(), query.getPrivacyTermsVersion()));
    }

    // UserQuery → UserProfileResponse
    public static UserProfileResponse toUserProfileResponse(UserQuery query) {
        String birthTimeCode = query.getBirthTime() != null ? query.getBirthTime().getCode() : null;
        return UserProfileResponse.of(query.getBirthdate().toString(), birthTimeCode);
    }

    // FortuneQuery → FortuneResponse
    public static FortuneResponse toFortuneResponse(FortuneQuery query) {
        return FortuneResponse.of(query.getFortuneContent(), query.getAdviceBox(), query.getSymbol(),
                query.getSymbolChn());
    }

    // MonthlyFortuneQuery → MonthlyFortuneResponse
    public static MonthlyFortuneResponse toMonthlyFortuneResponse(MonthlyFortuneQuery query) {
        return MonthlyFortuneResponse.of(
                query.getAffection(),
                query.getWealth(),
                query.getMisc());
    }

    // FourPillarsResult → FourPillarsResponse
    public static FourPillarsResponse toFourPillarsResponse(FourPillarsResult result) {
        return FourPillarsResponse.of(result.getYearly(), result.getMonthly(), result.getDaily(), result.getTimely());
    }

    // NoticeResult → NoticeDetailResponse
    public static NoticeDetailResponse toNoticeDetailResponse(NoticeResult result) {
        return NoticeDetailResponse.from(result);
    }

    // NoticeResult -> NoticeListResponse
    public static NoticeListResponse toNoticeListResponse(NoticeResult result) {
        return NoticeListResponse.from(result);
    }
}