package com.kolloseum.fourpillars.domain.model.vo;

import com.kolloseum.fourpillars.domain.model.enums.Provider;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@ToString
public class OAuth {
    String socialId;
    Provider provider;

    public static OAuth of(String socialId, Provider provider) {
        return new OAuth(socialId, provider);
    }
}
