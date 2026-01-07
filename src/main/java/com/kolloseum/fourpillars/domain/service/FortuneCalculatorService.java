package com.kolloseum.fourpillars.domain.service;

import com.kolloseum.fourpillars.domain.model.enums.TimeBranch;
import com.kolloseum.fourpillars.domain.model.vo.Gapja;
import java.time.LocalDate;

public interface FortuneCalculatorService {

    Gapja calculateYearGapja(LocalDate birthdate);

    Gapja calculateMonthGapja(LocalDate birthdate);

    Gapja calculateDayGapja(LocalDate birthdate);

    int calculateKoreanAge(LocalDate birthdate);

    int transformSolarBirthdayToLunar(LocalDate birthdate);

    boolean lengthOfMonth(LocalDate birthdate);

    Gapja calculateSiju(Gapja dayGapja, TimeBranch timeBranch);
}