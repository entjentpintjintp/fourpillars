package com.kolloseum.fourpillars.application.service;

import com.kolloseum.fourpillars.application.dto.NoticeResult;
import com.kolloseum.fourpillars.interfaces.dto.request.NoticeRequest;

public interface AdminNoticeService {
    NoticeResult createNotice(NoticeRequest request);

    NoticeResult updateNotice(Long id, NoticeRequest request);

    void deleteNotice(Long id);
}
