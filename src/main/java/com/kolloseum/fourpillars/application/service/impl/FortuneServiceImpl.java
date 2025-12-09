package com.kolloseum.fourpillars.application.service.impl;

import com.kolloseum.fourpillars.application.dto.FortuneQuery;
import com.kolloseum.fourpillars.application.dto.MonthlyFortuneQuery;
import com.kolloseum.fourpillars.application.mapper.FortuneMapper;
import com.kolloseum.fourpillars.application.service.FortuneService;
import com.kolloseum.fourpillars.common.exception.BusinessException;
import com.kolloseum.fourpillars.common.utils.AuthValidator;
import com.kolloseum.fourpillars.domain.model.entity.User;
import com.kolloseum.fourpillars.domain.model.vo.Fortune;
import com.kolloseum.fourpillars.domain.model.vo.Gapja;
import com.kolloseum.fourpillars.domain.model.vo.TokenPayload;
import com.kolloseum.fourpillars.domain.service.FortuneCalculatorService;
import com.kolloseum.fourpillars.domain.service.FortuneFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FortuneServiceImpl implements FortuneService {

    private final FortuneFileService fortuneFileService;
    private final FortuneCalculatorService fortuneCalculatorService;
    private final AuthValidator authValidator;

    @Override
    public FortuneQuery getUserFortune(TokenPayload tokenPayload) {
        User user = authValidator.validateAndGetAuthorizedUser(tokenPayload);

        if (user.getProfile() == null || user.getProfile().getBirthdate() == null) {
            throw BusinessException.profileIncompleted("User profile not found or birthdate is missing");
        }

        LocalDate birthdate = user.getProfile().getBirthdate();
        String fortuneCode = calculateTotalScore(birthdate);

        Fortune fortune = fortuneFileService.getFortuneByCode(fortuneCode)
                .orElseThrow(
                        () -> BusinessException.fileReadError("Fortune content not found for code: " + fortuneCode));

        log.info("Fortune calculation requested for userId: {}", tokenPayload.getUserId());
        return FortuneMapper.toFortuneQuery(fortune);
    }

    @Override
    public MonthlyFortuneQuery getUserMonthlyFortune(TokenPayload tokenPayload) {
        User user = authValidator.validateAndGetAuthorizedUser(tokenPayload);

        if (user.getProfile() == null || user.getProfile().getBirthdate() == null) {
            throw BusinessException.profileIncompleted("User profile not found or birthdate is missing");
        }

        LocalDate birthdate = user.getProfile().getBirthdate();
        String fortuneCode = calculateTotalScore(birthdate);

        Fortune fortune = fortuneFileService.getFortuneByCode(fortuneCode)
                .orElseThrow(
                        () -> BusinessException.fileReadError("Fortune content not found for code: " + fortuneCode));

        log.info("Monthly fortune calculation requested for userId: {}", tokenPayload.getUserId());

        // 현재 월(Month) 식별 (1~12)
        int currentMonth = LocalDate.now().getMonthValue();

        // 해당 월의 데이터 추출 (인덱스는 0부터 시작하므로 -1)
        if (fortune.getMonthlyFortunes().size() < currentMonth) {
            throw BusinessException.fileReadError("Monthly fortune data missing for month: " + currentMonth);
        }

        var monthlyFortune = fortune.getMonthlyFortunes().get(currentMonth - 1);

        return FortuneMapper.toMonthlyFortuneQuery(monthlyFortune);
    }

    private String calculateTotalScore(LocalDate birthdate) {
        try {
            Gapja yearGapja = fortuneCalculatorService.calculateYearGapja(birthdate);
            Gapja monthGapja = fortuneCalculatorService.calculateMonthGapja(birthdate);
            Gapja dayGapja = fortuneCalculatorService.calculateDayGapja(birthdate);
            int koreanAge = fortuneCalculatorService.calculateKoreanAge(birthdate);
            int lunarBirthday = fortuneCalculatorService.transformSolarBirthdayToLunar(birthdate);
            int lengthOfMonth;
            if (fortuneCalculatorService.lengthOfMonth(birthdate)) {
                lengthOfMonth = 30;
            } else {
                lengthOfMonth = 29;
            }

            int yearScore = (yearGapja.getYearScore() + koreanAge) % 8;
            if (yearScore == 0)
                yearScore = 8;

            int monthScore = (monthGapja.getMonthScore() + lengthOfMonth) % 6;
            if (monthScore == 0)
                monthScore = 6;

            int dayScore = (dayGapja.getDayScore() + lunarBirthday) % 3;
            if (dayScore == 0)
                dayScore = 3;

            return String.valueOf(yearScore * 100 + monthScore * 10 + dayScore);

        } catch (Exception e) {
            log.error("Score calculation failed for birthdate: {}", birthdate, e);
            throw BusinessException.birthTransformFailed("Failed to calculate fortune score: " + e.getMessage());
        }
    }
}