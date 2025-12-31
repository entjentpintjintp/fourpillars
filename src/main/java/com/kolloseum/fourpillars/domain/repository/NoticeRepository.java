package com.kolloseum.fourpillars.domain.repository;

import com.kolloseum.fourpillars.domain.model.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface NoticeRepository {
    Notice save(Notice notice);

    Optional<Notice> findById(Long id);

    Page<Notice> findAll(Pageable pageable);

    void delete(Notice notice);
}
