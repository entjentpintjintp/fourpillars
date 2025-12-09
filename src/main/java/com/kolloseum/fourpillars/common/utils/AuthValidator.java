package com.kolloseum.fourpillars.common.utils;

import com.kolloseum.fourpillars.common.exception.BusinessException;
import com.kolloseum.fourpillars.common.exception.JwtException;
import com.kolloseum.fourpillars.domain.model.entity.Terms;
import com.kolloseum.fourpillars.domain.model.entity.User;
import com.kolloseum.fourpillars.domain.model.enums.Role;
import com.kolloseum.fourpillars.domain.model.enums.TermsType;
import com.kolloseum.fourpillars.domain.model.vo.TokenPayload;
import com.kolloseum.fourpillars.domain.repository.TermsRepository;
import com.kolloseum.fourpillars.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthValidator {

    private final UserRepository userRepository;
    private final TermsRepository termsRepository;

    public User validateAndGetAuthorizedUser(TokenPayload tokenPayload) {
        validateAccessToken(tokenPayload);

        User user = userRepository.findByIdWithTermsAgreements(tokenPayload.getUserId())
                .orElseThrow(() -> BusinessException.invalidUser("User not found"));

        // Admin Bypass Logic
        if (user.getRole() == Role.ADMIN) {
            return user;
        }

        if (!checkLatestTermsAgreement(user)) {
            throw BusinessException.termsAgreementRequired("Terms agreement required for this operation");
        }

        return user;
    }

    public void validateAccessToken(TokenPayload tokenPayload) {
        if (!"ACCESS".equals(tokenPayload.getTokenType())) {
            throw JwtException.invalidTokenState("Only ACCESS token can access this resource");
        }

        if (tokenPayload.getUserId() == null) {
            throw JwtException.invalidTokenState("User ID is required for this operation");
        }
    }

    public boolean checkLatestTermsAgreement(User user) {
        Optional<Terms> latestServiceTerms = termsRepository.findLatestByType(TermsType.SERVICE);
        boolean serviceAgreed = latestServiceTerms
                .map(terms -> user.isLatestTermsAgreed(terms.getTermsVersion(), TermsType.SERVICE))
                .orElse(false);

        Optional<Terms> latestPrivacyTerms = termsRepository.findLatestByType(TermsType.PRIVACY);
        boolean privacyAgreed = latestPrivacyTerms
                .map(terms -> user.isLatestTermsAgreed(terms.getTermsVersion(), TermsType.PRIVACY))
                .orElse(false);

        return serviceAgreed && privacyAgreed;
    }
}
