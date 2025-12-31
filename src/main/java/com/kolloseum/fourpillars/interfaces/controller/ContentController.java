package com.kolloseum.fourpillars.interfaces.controller;

import com.kolloseum.fourpillars.application.service.FortuneService;
import com.kolloseum.fourpillars.application.service.NoticeService;
import com.kolloseum.fourpillars.common.response.ApiResponse;
import com.kolloseum.fourpillars.common.utils.SecurityContextTokenExtractor;
import com.kolloseum.fourpillars.interfaces.dto.response.FortuneResponse;
import com.kolloseum.fourpillars.interfaces.dto.response.MonthlyFortuneResponse;
import com.kolloseum.fourpillars.interfaces.dto.response.NoticeDetailResponse;
import com.kolloseum.fourpillars.interfaces.dto.response.NoticeWrapper;
import com.kolloseum.fourpillars.interfaces.mapper.ResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contents")
@RequiredArgsConstructor
public class ContentController {

    private final FortuneService fortuneService;
    private final NoticeService noticeService;

    @GetMapping("/fortune/yearly")
    public ResponseEntity<ApiResponse<FortuneResponse>> getFortune() {
        var tokenPayload = SecurityContextTokenExtractor.getCurrentTokenPayload();
        var query = fortuneService.getUserFortune(tokenPayload);
        var response = ResponseMapper.toFortuneResponse(query);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/fortune/monthly")
    public ResponseEntity<ApiResponse<MonthlyFortuneResponse>> getMonthlyFortune() {
        var tokenPayload = SecurityContextTokenExtractor.getCurrentTokenPayload();
        var query = fortuneService.getUserMonthlyFortune(tokenPayload);
        var response = ResponseMapper.toMonthlyFortuneResponse(query);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/notices/title")
    public ResponseEntity<ApiResponse<NoticeWrapper>> getNoticeList(Pageable pageable) {
        var resultPage = noticeService.getNoticeList(pageable);
        var responseList = resultPage.getContent().stream()
                .map(ResponseMapper::toNoticeListResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.success(NoticeWrapper.of(responseList)));
    }

    @GetMapping("/notices/content/{id}")
    public ResponseEntity<ApiResponse<NoticeDetailResponse>> getNoticeDetail(@PathVariable Long id) {
        var result = noticeService.getNoticeDetail(id);
        var response = ResponseMapper.toNoticeDetailResponse(result);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}
