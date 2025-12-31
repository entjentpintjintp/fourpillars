package com.kolloseum.fourpillars.application.service;

import com.kolloseum.fourpillars.interfaces.dto.request.NoticeRequest;
import com.kolloseum.fourpillars.interfaces.dto.response.NoticeDetailResponse;

public interface AdminNoticeService {
    NoticeDetailResponse createNotice(NoticeRequest request);

    NoticeDetailResponse updateNotice(Long id, NoticeRequest request);

    void deleteNotice(Long id);
}
