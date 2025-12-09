package com.kolloseum.fourpillars.application.mapper;

import com.kolloseum.fourpillars.application.dto.FortuneQuery;
import com.kolloseum.fourpillars.application.dto.MonthlyFortuneQuery;
import com.kolloseum.fourpillars.domain.model.vo.Fortune;
import com.kolloseum.fourpillars.domain.model.vo.MonthlyFortune;



public class FortuneMapper {

    public static FortuneQuery toFortuneQuery(Fortune fortune) {
        return FortuneQuery.of(
                fortune.getFortuneContent(),
                fortune.getAdviceBox(),
                fortune.getSymbol(),
                fortune.getSymbolChn());
    }

    public static MonthlyFortuneQuery toMonthlyFortuneQuery(
            MonthlyFortune monthlyFortune) {
        return MonthlyFortuneQuery.of(
                monthlyFortune.getAffection(),
                monthlyFortune.getWealth(),
                monthlyFortune.getMisc());
    }
}
