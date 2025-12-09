package com.kolloseum.fourpillars.application.service;

import com.kolloseum.fourpillars.application.dto.FortuneQuery;
import com.kolloseum.fourpillars.application.dto.MonthlyFortuneQuery;
import com.kolloseum.fourpillars.domain.model.vo.TokenPayload;

public interface FortuneService {

    FortuneQuery getUserFortune(TokenPayload tokenPayload);
    MonthlyFortuneQuery getUserMonthlyFortune(TokenPayload tokenPayload);

}
