package com.kolloseum.fourpillars.domain.service;

import com.kolloseum.fourpillars.domain.model.vo.OAuth;
import com.kolloseum.fourpillars.domain.model.vo.OAuth2Request;

public interface OAuth2Service {

    OAuth authenticate(OAuth2Request request);

}
