package com.kolloseum.fourpillars.application.service;

import com.kolloseum.fourpillars.domain.model.enums.TermsType;
import com.kolloseum.fourpillars.domain.model.entity.Terms;
import java.util.List;

public interface AdminTermsService {
    void createTerms(TermsType type, String version, String content);

    List<Terms> getAllTerms();
}
