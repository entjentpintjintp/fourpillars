package com.kolloseum.fourpillars.infrastructure.adapter;

import com.kolloseum.fourpillars.domain.model.vo.Gapja;
import com.kolloseum.fourpillars.domain.service.FortuneCalculatorService;
import net.time4j.PlainDate;
import net.time4j.calendar.EastAsianYear;
import net.time4j.calendar.KoreanCalendar;
import net.time4j.calendar.SexagesimalName;
import net.time4j.calendar.SexagesimalName.Branch;
import net.time4j.calendar.SexagesimalName.Stem;
import net.time4j.calendar.SolarTerm;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class Time4JFortuneCalculator implements FortuneCalculatorService {

    @Override
    public Gapja calculateYearGapja(LocalDate birthdate) {
        PlainDate solarDate = PlainDate.from(birthdate);
        int sajuYear = getSajuYear(solarDate);

        return Gapja.fromOrder(EastAsianYear.forGregorian(sajuYear).getYearOfCycle().getNumber());
    }

    @Override
    public Gapja calculateMonthGapja(LocalDate birthdate) {
        PlainDate solarDate = PlainDate.from(birthdate);
        int sajuYear = getSajuYear(solarDate);

        // 연간
        Stem yearStem = EastAsianYear.forGregorian(sajuYear).getYearOfCycle().getStem();

        // 월지
        SolarTerm currentTerm = solarDate.transform(KoreanCalendar.class).get(KoreanCalendar.SOLAR_TERM);
        int monthBranchIndex = (currentTerm.ordinal() / 2 + 2) % 12;
        Branch monthBranch = Branch.values()[monthBranchIndex];

        // 둔월법 적용
        int yearStemIndex = yearStem.ordinal();
        int offsetFromInMonth = (monthBranch.ordinal() - Branch.YIN_3_TIGER.ordinal() + 12) % 12;
        int monthStemIndex = (yearStemIndex * 2 + 2 + offsetFromInMonth) % 10;
        Stem monthStem = Stem.values()[monthStemIndex];

        return Gapja.fromOrder(SexagesimalName.of(monthStem, monthBranch).getNumber());
    }

    private int getSajuYear(PlainDate solarDate) {
        int birthYear = solarDate.getYear();

        // 해당 년도의 입춘 날짜를 계산
        PlainDate lichunDate = SolarTerm.MINOR_01_LICHUN_315
                .onOrAfter(PlainDate.of(birthYear, 1, 1).transform(KoreanCalendar.class))
                .transform(PlainDate.axis());

        // 생일이 입춘 이전이면 작년, 이후면 올해를 기준으로 함
        return solarDate.isBefore(lichunDate) ? birthYear - 1 : birthYear;
    }

    @Override
    public Gapja calculateDayGapja(LocalDate birthdate) {

        PlainDate solarDate = PlainDate.from(birthdate);
        SexagesimalName dayGapja = solarDate.transform(KoreanCalendar.class).getSexagesimalDay();

        return Gapja.fromOrder(dayGapja.getNumber());
    }

    @Override
    public int calculateKoreanAge(LocalDate birthdate) {
        return LocalDate.now().getYear() - birthdate.getYear() + 1;
    }

    @Override
    public int transformSolarBirthdayToLunar(LocalDate birthdate) {
        KoreanCalendar lunarDate = PlainDate.from(birthdate).transform(KoreanCalendar.class);

        return lunarDate.getDayOfMonth();
    }

    // 소월, 대월 판단 / 대월이면 true
    @Override
    public boolean lengthOfMonth(LocalDate birthdate) {
        // 양력 날짜를 음력으로 변환 후, 해당 음력 월의 길이를 확인
        KoreanCalendar lunarDate = PlainDate.from(birthdate).transform(KoreanCalendar.class);

        return lunarDate.lengthOfMonth() == 30;
    }

}
