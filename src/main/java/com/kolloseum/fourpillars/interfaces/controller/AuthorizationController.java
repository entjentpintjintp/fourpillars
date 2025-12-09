package com.kolloseum.fourpillars.interfaces.controller;

import com.kolloseum.fourpillars.application.dto.AuthTokenInput;
import com.kolloseum.fourpillars.application.service.AuthorizationService;
import com.kolloseum.fourpillars.common.exception.JwtException;
import com.kolloseum.fourpillars.common.response.ApiResponse;
import com.kolloseum.fourpillars.common.utils.SecurityContextTokenExtractor;
import com.kolloseum.fourpillars.interfaces.dto.request.AuthTermsUpdateRequest;
import com.kolloseum.fourpillars.interfaces.dto.response.AuthTermsCheckResponse;
import com.kolloseum.fourpillars.interfaces.dto.response.AuthTermsContentResponse;
import com.kolloseum.fourpillars.interfaces.dto.response.AuthTokenResponse;
import com.kolloseum.fourpillars.interfaces.mapper.RequestMapper;
import com.kolloseum.fourpillars.interfaces.mapper.ResponseMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authz")
@RequiredArgsConstructor
public class AuthorizationController {

    private final AuthorizationService authorizationService;

    @PostMapping("/terms/update")
    public ResponseEntity<ApiResponse<AuthTokenResponse>> updateTermsAgreement(
            @Valid @RequestBody AuthTermsUpdateRequest request,
            HttpServletRequest httpRequest) {
        var tokenPayload = SecurityContextTokenExtractor.getCurrentTokenPayload();

        String bearerToken = httpRequest.getHeader("Authorization");
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            throw JwtException.invalidToken("Invalid Authorization header format");
        }
        String temporaryTokenString = bearerToken.substring("Bearer ".length());

        var termsCommand = RequestMapper.toAuthTermsAgreementCommand(request);
        var deviceCommand = RequestMapper.toDeviceUpdateCommand(request);

        AuthTokenInput tokenInput = authorizationService.updateTermsAgreementAndDeviceInfo(
                tokenPayload, temporaryTokenString, termsCommand, deviceCommand);

        AuthTokenResponse response = AuthTokenResponse.of(
                tokenInput.getAccessToken(),
                tokenInput.getRefreshToken());

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/terms/check")
    public ResponseEntity<ApiResponse<AuthTermsCheckResponse>> checkTermsStatus() {
        var tokenPayload = SecurityContextTokenExtractor.getCurrentTokenPayload();
        var query = authorizationService.checkTermsStatus(tokenPayload);
        var response = ResponseMapper.toAuthTermsCheckResponse(query);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/terms/request")
    public ResponseEntity<ApiResponse<AuthTermsContentResponse>> getTermsContent() {
        var tokenPayload = SecurityContextTokenExtractor.getCurrentTokenPayload();
        var query = authorizationService.getTermsContent(tokenPayload);
        var response = ResponseMapper.toAuthTermsContentResponse(query);

        return ResponseEntity.ok(ApiResponse.success(response));
    }
}