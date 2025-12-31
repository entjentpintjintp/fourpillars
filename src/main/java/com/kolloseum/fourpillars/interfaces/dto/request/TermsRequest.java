package com.kolloseum.fourpillars.interfaces.dto.request;

import com.kolloseum.fourpillars.domain.model.enums.TermsType;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TermsRequest {
    private TermsType type;
    private String version;
    private String content;
}
