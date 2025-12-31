package com.kolloseum.fourpillars.application.service;

import com.kolloseum.fourpillars.application.dto.NoticeResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeService {
    Page<NoticeResult> getNoticeList(Pageable pageable);

    NoticeResult getNoticeDetail(Long id);
}
