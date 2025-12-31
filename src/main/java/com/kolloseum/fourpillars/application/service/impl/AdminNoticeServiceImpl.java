package com.kolloseum.fourpillars.application.service.impl;

import com.kolloseum.fourpillars.application.dto.NoticeResult;
import com.kolloseum.fourpillars.application.service.AdminNoticeService;
import com.kolloseum.fourpillars.common.exception.BusinessException;
import com.kolloseum.fourpillars.domain.repository.NoticeRepository;
import com.kolloseum.fourpillars.infrastructure.persistence.entity.NoticeEntity;
import com.kolloseum.fourpillars.interfaces.dto.request.NoticeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminNoticeServiceImpl implements AdminNoticeService {

    private final NoticeRepository noticeRepository;

    @Override
    @Transactional
    public NoticeResult createNotice(NoticeRequest request) {
        NoticeEntity notice = NoticeEntity.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .build();

        NoticeEntity savedNotice = noticeRepository.save(notice);
        return NoticeResult.from(savedNotice);
    }

    @Override
    @Transactional
    public NoticeResult updateNotice(Long id, NoticeRequest request) {
        NoticeEntity notice = noticeRepository.findById(id)
                .orElseThrow(() -> BusinessException.notFound("Notice not found with id: " + id));

        notice.update(request.getTitle(), request.getContent());
        // JPA Dirty Checking will save the changes

        return NoticeResult.from(notice);
    }

    @Override
    @Transactional
    public void deleteNotice(Long id) {
        NoticeEntity notice = noticeRepository.findById(id)
                .orElseThrow(() -> BusinessException.notFound("Notice not found with id: " + id));

        noticeRepository.delete(notice);
    }
}
