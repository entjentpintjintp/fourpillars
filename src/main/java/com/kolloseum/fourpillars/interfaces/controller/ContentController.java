package com.kolloseum.fourpillars.interfaces.controller;

import com.kolloseum.fourpillars.application.service.FortuneService;
import com.kolloseum.fourpillars.common.response.ApiResponse;
import com.kolloseum.fourpillars.common.utils.SecurityContextTokenExtractor;
import com.kolloseum.fourpillars.interfaces.dto.response.FortuneResponse;
import com.kolloseum.fourpillars.interfaces.dto.response.MonthlyFortuneResponse;
import com.kolloseum.fourpillars.interfaces.mapper.ResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contents/fortune")
@RequiredArgsConstructor
public class ContentController {

    private final FortuneService fortuneService;

    @GetMapping("/yearly")
    public ResponseEntity<ApiResponse<FortuneResponse>> getFortune() {
        var tokenPayload = SecurityContextTokenExtractor.getCurrentTokenPayload();
        var query = fortuneService.getUserFortune(tokenPayload);
        var response = ResponseMapper.toFortuneResponse(query);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/monthly")
    public ResponseEntity<ApiResponse<MonthlyFortuneResponse>> getMonthlyFortune() {
        var tokenPayload = SecurityContextTokenExtractor.getCurrentTokenPayload();
        var query = fortuneService.getUserMonthlyFortune(tokenPayload);
        var response = ResponseMapper.toMonthlyFortuneResponse(query);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

}
