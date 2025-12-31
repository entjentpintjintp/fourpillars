package com.kolloseum.fourpillars.application.service.impl;

import com.kolloseum.fourpillars.application.dto.NoticeResult;
import com.kolloseum.fourpillars.application.service.AdminNoticeService;
import com.kolloseum.fourpillars.common.exception.BusinessException;
import com.kolloseum.fourpillars.domain.repository.NoticeRepository;
import com.kolloseum.fourpillars.domain.model.entity.Notice;
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
        Notice notice = Notice.create(
                request.getTitle(),
                request.getContent());

        Notice savedNotice = noticeRepository.save(notice);
        return NoticeResult.from(savedNotice);
    }

    @Override
    @Transactional
    public NoticeResult updateNotice(Long id, NoticeRequest request) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> BusinessException.notFound("Notice not found with id: " + id));

        notice.update(request.getTitle(), request.getContent());
        // In simple JPA, dirty checking works on Entities.
        // Here, we have a Domain object decoupled from JPA session (unless Mapper
        // returns attached entity wrapped in Domain? No).
        // Domain object is POJO. So we MUST call save() to persist changes via
        // Repository.
        Notice savedNotice = noticeRepository.save(notice);

        return NoticeResult.from(savedNotice);
    }

    @Override
    @Transactional
    public void deleteNotice(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> BusinessException.notFound("Notice not found with id: " + id));

        noticeRepository.delete(notice);
    }
}
