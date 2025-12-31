package com.kolloseum.fourpillars.interfaces.dto.request;

import com.kolloseum.fourpillars.domain.model.enums.TermsType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TermsRequest {
    private TermsType type;
    private String version;
    private String content;
}
