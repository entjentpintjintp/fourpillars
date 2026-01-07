package com.kolloseum.fourpillars.application.service.impl;

import com.kolloseum.fourpillars.application.dto.NoticeResult;
import com.kolloseum.fourpillars.application.service.AdminNoticeService;
import com.kolloseum.fourpillars.common.exception.BusinessException;
import com.kolloseum.fourpillars.domain.repository.NoticeRepository;
import com.kolloseum.fourpillars.domain.model.entity.Notice;
import com.kolloseum.fourpillars.interfaces.dto.request.NoticeRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminNoticeServiceImpl implements AdminNoticeService {

    private final NoticeRepository noticeRepository;

    @Override
    @Transactional
    public NoticeResult createNotice(NoticeRequest request) {
        log.info("[AdminNoticeService] Creating notice: title='{}'", request.getTitle());
        Notice notice = Notice.create(
                request.getTitle(),
                request.getContent());

        Notice savedNotice = noticeRepository.save(notice);
        log.info("[AdminNoticeService] Notice created successfully. ID={}", savedNotice.getId());
        return NoticeResult.from(savedNotice);
    }

    @Override
    @Transactional
    public NoticeResult updateNotice(Long id, NoticeRequest request) {
        log.info("[AdminNoticeService] Updating notice ID={}", id);
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> BusinessException.notFound("Notice not found with id: " + id));

        notice.update(request.getTitle(), request.getContent());
        Notice savedNotice = noticeRepository.save(notice);

        log.info("[AdminNoticeService] Notice updated successfully. ID={}", savedNotice.getId());
        return NoticeResult.from(savedNotice);
    }

    @Override
    @Transactional
    public void deleteNotice(Long id) {
        log.info("[AdminNoticeService] Deleting notice ID={}", id);
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> BusinessException.notFound("Notice not found with id: " + id));

        noticeRepository.delete(notice);
        log.info("[AdminNoticeService] Notice deleted successfully. ID={}", id);
    }
}
