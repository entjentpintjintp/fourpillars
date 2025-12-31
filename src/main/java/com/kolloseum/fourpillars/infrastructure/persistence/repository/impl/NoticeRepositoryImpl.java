package com.kolloseum.fourpillars.infrastructure.persistence.repository.impl;

import com.kolloseum.fourpillars.domain.repository.NoticeRepository;
import com.kolloseum.fourpillars.infrastructure.persistence.entity.NoticeEntity;
import com.kolloseum.fourpillars.infrastructure.persistence.repository.NoticeJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class NoticeRepositoryImpl implements NoticeRepository {

    private final NoticeJpaRepository noticeJpaRepository;

    @Override
    public NoticeEntity save(NoticeEntity notice) {
        return noticeJpaRepository.save(notice);
    }

    @Override
    public Optional<NoticeEntity> findById(Long id) {
        return noticeJpaRepository.findById(id);
    }

    @Override
    public Page<NoticeEntity> findAll(Pageable pageable) {
        return noticeJpaRepository.findAll(pageable);
    }

    @Override
    public void delete(NoticeEntity notice) {
        noticeJpaRepository.delete(notice);
    }
}
