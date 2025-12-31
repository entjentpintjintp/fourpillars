package com.kolloseum.fourpillars.application.service.impl;

import com.kolloseum.fourpillars.application.service.NoticeService;
import com.kolloseum.fourpillars.common.exception.BusinessException;
import com.kolloseum.fourpillars.domain.repository.NoticeRepository;
import com.kolloseum.fourpillars.infrastructure.persistence.entity.NoticeEntity;
import com.kolloseum.fourpillars.interfaces.dto.response.NoticeDetailResponse;
import com.kolloseum.fourpillars.interfaces.dto.response.NoticeListResponse;
import com.kolloseum.fourpillars.interfaces.dto.response.NoticeWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;

    @Override
    @Transactional(readOnly = true)
    public NoticeWrapper getNoticeList(Pageable pageable) {
        Page<NoticeEntity> page = noticeRepository.findAll(pageable);
        List<NoticeListResponse> notices = page.getContent().stream()
                .map(NoticeListResponse::from)
                .collect(Collectors.toList());

        return NoticeWrapper.of(notices);
    }

    @Override
    @Transactional(readOnly = true)
    public NoticeDetailResponse getNoticeDetail(Long id) {
        NoticeEntity notice = noticeRepository.findById(id)
                .orElseThrow(() -> BusinessException.notFound("Notice not found with id: " + id));

        return NoticeDetailResponse.from(notice);
    }
}
