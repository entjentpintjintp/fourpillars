package com.kolloseum.fourpillars.interfaces.controller;

import com.kolloseum.fourpillars.application.service.UserService;
import com.kolloseum.fourpillars.common.response.ApiResponse;

import com.kolloseum.fourpillars.interfaces.dto.request.AuthWithdrawRequest;
import com.kolloseum.fourpillars.interfaces.dto.request.UserProfileRequest;
import com.kolloseum.fourpillars.interfaces.dto.response.UserProfileResponse;
import com.kolloseum.fourpillars.interfaces.mapper.RequestMapper;
import com.kolloseum.fourpillars.common.utils.SecurityContextTokenExtractor;
import com.kolloseum.fourpillars.interfaces.mapper.ResponseMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/profile")
    public ResponseEntity<ApiResponse<Void>> createProfile(@Valid @RequestBody UserProfileRequest request) {
        var tokenPayload = SecurityContextTokenExtractor.getCurrentTokenPayload();
        var command = RequestMapper.toUserCommand(request);

        userService.updateProfile(tokenPayload, command);

        return ResponseEntity.ok(ApiResponse.success());
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getUserInfo() {
        var tokenPayload = SecurityContextTokenExtractor.getCurrentTokenPayload();
        var query = userService.getUserInfo(tokenPayload);
        var response = ResponseMapper.toUserProfileResponse(query);

        return ResponseEntity.ok(ApiResponse.success(response));

    }

    @DeleteMapping("/withdraw")
    public ResponseEntity<ApiResponse<Void>> withdraw(@Valid @RequestBody AuthWithdrawRequest request) {

        var tokenPayload = SecurityContextTokenExtractor.getCurrentTokenPayload();

        userService.withdraw(tokenPayload, request.getAccessToken(), request.getRefreshToken());

        return ResponseEntity.ok(ApiResponse.success());

    }

    @PatchMapping("/update")
    public ResponseEntity<ApiResponse<Void>> updateProfile(@Valid @RequestBody UserProfileRequest request) {

        var tokenPayload = SecurityContextTokenExtractor.getCurrentTokenPayload();
        var command = RequestMapper.toUserCommand(request);

        userService.updateProfile(tokenPayload, command);

        return ResponseEntity.ok(ApiResponse.success());
    }
}