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
                AuthTermsContentResponse.TermsInfo.of(stripHtml(query.getServiceTermsContent()),
                        query.getServiceTermsVersion()),
                AuthTermsContentResponse.TermsInfo.of(stripHtml(query.getPrivacyTermsContent()),
                        query.getPrivacyTermsVersion()));
    }

    // UserQuery → UserProfileResponse
    public static UserProfileResponse toUserProfileResponse(UserQuery query) {
        String birthtimeCode = query.getBirthTime() != null ? query.getBirthTime().getCode() : null;
        return UserProfileResponse.of(query.getBirthdate().toString(), birthtimeCode);
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
        // Convert <br> back to \n for mobile app display
        String cleanContent = stripHtml(result.getContent());
        return NoticeDetailResponse.of(
                result.getId(),
                result.getTitle(),
                cleanContent,
                result.getCreatedAt(),
                result.getUpdatedAt());
    }

    // NoticeResult -> NoticeListResponse
    public static NoticeListResponse toNoticeListResponse(NoticeResult result) {
        return NoticeListResponse.from(result);
    }

    private static String stripHtml(String content) {
        if (content == null)
            return null;
        // Replace <br> variations with newline
        return content.replaceAll("(?i)<br\\s*/?>", "\n");
    }
}