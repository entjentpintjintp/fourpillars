package com.kolloseum.fourpillars.application.service;

import com.kolloseum.fourpillars.application.dto.TermsResult;
import com.kolloseum.fourpillars.interfaces.dto.request.TermsRequest;
import java.util.List;

public interface AdminTermsService {
    void createTerms(TermsRequest request);

    List<TermsResult> getAllTerms();
}
