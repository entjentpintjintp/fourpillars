package com.kolloseum.fourpillars.interfaces.controller;

import com.kolloseum.fourpillars.application.service.AdminNoticeService;
import com.kolloseum.fourpillars.common.response.ApiResponse;
import com.kolloseum.fourpillars.interfaces.dto.request.NoticeRequest;
import com.kolloseum.fourpillars.interfaces.dto.response.NoticeDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/api/notices")
@RequiredArgsConstructor
public class AdminNoticeController {

    private final AdminNoticeService adminNoticeService;

    @PostMapping
    public ResponseEntity<ApiResponse<NoticeDetailResponse>> createNotice(@RequestBody NoticeRequest request) {
        var response = adminNoticeService.createNotice(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<NoticeDetailResponse>> updateNotice(@PathVariable Long id,
            @RequestBody NoticeRequest request) {
        var response = adminNoticeService.updateNotice(id, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteNotice(@PathVariable Long id) {
        adminNoticeService.deleteNotice(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
