package com.kolloseum.fourpillars.interfaces.controller;

import com.kolloseum.fourpillars.application.service.AuthenticationService;
import com.kolloseum.fourpillars.common.response.ApiResponse;
import com.kolloseum.fourpillars.interfaces.dto.request.AuthLoginRequest;
import com.kolloseum.fourpillars.interfaces.dto.request.AuthLogoutRequest;
import com.kolloseum.fourpillars.interfaces.dto.request.AuthRefreshRequest;
import com.kolloseum.fourpillars.interfaces.dto.response.AuthTokenResponse;
import com.kolloseum.fourpillars.interfaces.mapper.RequestMapper;
import com.kolloseum.fourpillars.interfaces.mapper.ResponseMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authn")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthTokenResponse>> login(@Valid @RequestBody AuthLoginRequest request) {
        var command = RequestMapper.toAuthLoginCommand(request);
        var tokenInput = authenticationService.login(command);
        var response = ResponseMapper.toAuthTokenResponse(tokenInput);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthTokenResponse>> refreshToken(@Valid @RequestBody AuthRefreshRequest request) {
        var tokenInput = authenticationService.refreshToken(request.getRefreshToken());
        var response = ResponseMapper.toAuthTokenResponse(tokenInput);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@Valid @RequestBody AuthLogoutRequest request) {
        authenticationService.logout(request.getAccessToken(), request.getRefreshToken());
        return ResponseEntity.ok(ApiResponse.success());
    }
}