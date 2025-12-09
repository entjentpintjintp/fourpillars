package com.kolloseum.fourpillars.domain.model.vo;

import com.kolloseum.fourpillars.domain.model.enums.Provider;
import com.kolloseum.fourpillars.domain.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TokenPayload {
    private UUID userId;
    private String socialId;
    private Provider provider;
    private Role role;
    private String tokenType; // ACCESS, REFRESH, TEMPORARY, PRE_AUTH_VERIFY, PRE_AUTH_SETUP

    public static TokenPayload ofAccess(UUID userId, String socialId, Provider provider, Role role) {
        return new TokenPayload(userId, socialId, provider, role, "ACCESS");
    }

    public static TokenPayload ofRefresh(UUID userId) {
        return new TokenPayload(userId, null, null, null, "REFRESH");
    }

    public static TokenPayload ofTemporary(String socialId, Provider provider) {
        return new TokenPayload(null, socialId, provider, Role.USER, "TEMPORARY");
    }

    public static TokenPayload ofPreAuthVerify(UUID userId, Role role) {
        return new TokenPayload(userId, null, null, role, "PRE_AUTH_VERIFY");
    }

    public static TokenPayload ofPreAuthSetup(UUID userId, Role role) {
        return new TokenPayload(userId, null, null, role, "PRE_AUTH_SETUP");
    }
}