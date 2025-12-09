package com.kolloseum.fourpillars.common.utils;

import com.kolloseum.fourpillars.common.exception.BusinessException;
import com.kolloseum.fourpillars.common.exception.JwtException;
import com.kolloseum.fourpillars.domain.model.entity.Terms;
import com.kolloseum.fourpillars.domain.model.entity.User;
import com.kolloseum.fourpillars.domain.model.enums.Provider;
import com.kolloseum.fourpillars.domain.model.enums.Role;
import com.kolloseum.fourpillars.domain.model.enums.TermsType;
import com.kolloseum.fourpillars.domain.model.vo.TokenPayload;
import com.kolloseum.fourpillars.domain.repository.TermsRepository;
import com.kolloseum.fourpillars.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthValidatorTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TermsRepository termsRepository;

    @InjectMocks
    private AuthValidator authValidator;

    private TokenPayload validTokenPayload;
    private User mockUser;
    private User mockAdmin;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        UUID userId = UUID.randomUUID();
        // Use factory method
        validTokenPayload = TokenPayload.ofAccess(userId, "socialId", Provider.KAKAO, Role.USER);

        mockUser = mock(User.class);
        when(mockUser.getRole()).thenReturn(Role.USER);

        mockAdmin = mock(User.class);
        when(mockAdmin.getRole()).thenReturn(Role.ADMIN);
    }

    @Test
    void validateAndGetAuthorizedUser_Success() {
        when(userRepository.findByIdWithTermsAgreements(any())).thenReturn(Optional.of(mockUser));

        // Mock Terms
        Terms serviceTerms = mock(Terms.class);
        when(serviceTerms.getTermsVersion()).thenReturn("v1");
        when(termsRepository.findLatestByType(TermsType.SERVICE)).thenReturn(Optional.of(serviceTerms));

        Terms privacyTerms = mock(Terms.class);
        when(privacyTerms.getTermsVersion()).thenReturn("v1");
        when(termsRepository.findLatestByType(TermsType.PRIVACY)).thenReturn(Optional.of(privacyTerms));

        // Mock User Agreement
        when(mockUser.isLatestTermsAgreed("v1", TermsType.SERVICE)).thenReturn(true);
        when(mockUser.isLatestTermsAgreed("v1", TermsType.PRIVACY)).thenReturn(true);

        User result = authValidator.validateAndGetAuthorizedUser(validTokenPayload);
        assertNotNull(result);
        assertEquals(mockUser, result);
    }

    @Test
    void validateAndGetAuthorizedUser_AdminBypass() {
        when(userRepository.findByIdWithTermsAgreements(any())).thenReturn(Optional.of(mockAdmin));

        // Even if terms are not agreed (or repositories return empty), Admin should
        // pass
        when(termsRepository.findLatestByType(any())).thenReturn(Optional.empty());

        User result = authValidator.validateAndGetAuthorizedUser(validTokenPayload);
        assertNotNull(result);
        assertEquals(mockAdmin, result);
    }

    @Test
    void validateAndGetAuthorizedUser_TermsNotAgreed() {
        when(userRepository.findByIdWithTermsAgreements(any())).thenReturn(Optional.of(mockUser));

        // Mock Terms
        Terms serviceTerms = mock(Terms.class);
        when(serviceTerms.getTermsVersion()).thenReturn("v1");
        when(termsRepository.findLatestByType(TermsType.SERVICE)).thenReturn(Optional.of(serviceTerms));

        // User not agreed
        when(mockUser.isLatestTermsAgreed("v1", TermsType.SERVICE)).thenReturn(false);

        assertThrows(BusinessException.class, () -> authValidator.validateAndGetAuthorizedUser(validTokenPayload));
    }

    @Test
    void validateAccessToken_InvalidType() {
        TokenPayload invalidPayload = TokenPayload.ofRefresh(UUID.randomUUID());
        assertThrows(JwtException.class, () -> authValidator.validateAccessToken(invalidPayload));
    }
}
