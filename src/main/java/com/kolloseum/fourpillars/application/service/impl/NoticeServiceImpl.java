package com.kolloseum.fourpillars.application.service.impl;

import com.kolloseum.fourpillars.application.dto.NoticeResult;
import com.kolloseum.fourpillars.application.service.NoticeService;
import com.kolloseum.fourpillars.common.exception.BusinessException;
import com.kolloseum.fourpillars.domain.repository.NoticeRepository;
import com.kolloseum.fourpillars.domain.model.entity.Notice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<NoticeResult> getNoticeList(Pageable pageable) {
        log.debug("[NoticeService] Fetching notice list: page={}, size={}", pageable.getPageNumber(),
                pageable.getPageSize());
        return noticeRepository.findAll(pageable)
                .map(NoticeResult::from);
    }

    @Override
    @Transactional(readOnly = true)
    public NoticeResult getNoticeDetail(Long id) {
        log.info("[NoticeService] Fetching notice detail: ID={}", id);
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> BusinessException.notFound("Notice not found with id: " + id));

        return NoticeResult.from(notice);
    }
}
