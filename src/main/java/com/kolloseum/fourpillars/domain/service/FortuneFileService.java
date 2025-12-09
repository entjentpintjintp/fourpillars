package com.kolloseum.fourpillars.domain.service;

import com.kolloseum.fourpillars.domain.model.vo.Fortune;

import java.util.List;
import java.util.Optional;

public interface FortuneFileService {

    List<Fortune> getAllFortunes();

    Optional<Fortune> getFortuneByCode(String fortuneCode);

    List<Fortune> getCachedFortunes();

}
