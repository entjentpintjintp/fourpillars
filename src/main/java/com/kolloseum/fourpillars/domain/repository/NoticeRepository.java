package com.kolloseum.fourpillars.domain.repository;

import com.kolloseum.fourpillars.infrastructure.persistence.entity.NoticeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface NoticeRepository {
    NoticeEntity save(NoticeEntity notice);

    Optional<NoticeEntity> findById(Long id);

    Page<NoticeEntity> findAll(Pageable pageable);

    void delete(NoticeEntity notice);
}
