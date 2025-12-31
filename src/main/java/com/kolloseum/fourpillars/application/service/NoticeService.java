package com.kolloseum.fourpillars.application.service;

import com.kolloseum.fourpillars.interfaces.dto.response.NoticeDetailResponse;
import com.kolloseum.fourpillars.interfaces.dto.response.NoticeWrapper;
import org.springframework.data.domain.Pageable;

public interface NoticeService {
    NoticeWrapper getNoticeList(Pageable pageable);

    NoticeDetailResponse getNoticeDetail(Long id);
}
