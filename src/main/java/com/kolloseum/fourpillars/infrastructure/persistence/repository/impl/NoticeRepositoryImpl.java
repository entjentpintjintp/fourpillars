package com.kolloseum.fourpillars.infrastructure.persistence.repository.impl;

import com.kolloseum.fourpillars.domain.model.entity.Notice;
import com.kolloseum.fourpillars.domain.repository.NoticeRepository;
import com.kolloseum.fourpillars.infrastructure.persistence.entity.NoticeEntity;
import com.kolloseum.fourpillars.infrastructure.persistence.mapper.NoticeMapper;
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
    public Notice save(Notice notice) {
        // Domain -> Entity
        NoticeEntity entity;
        if (notice.getId() != null) {
            // Update scenario: Fetch existing, update fields
            entity = noticeJpaRepository.findById(notice.getId())
                    .orElse(NoticeMapper.toEntity(notice)); // Fallback if not found, though unconventional
            entity.update(notice.getTitle(), notice.getContent());
        } else {
            // Create scenario
            entity = NoticeMapper.toEntity(notice);
        }

        NoticeEntity savedEntity = noticeJpaRepository.save(entity);
        return NoticeMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Notice> findById(Long id) {
        return noticeJpaRepository.findById(id)
                .map(NoticeMapper::toDomain);
    }

    @Override
    public Page<Notice> findAll(Pageable pageable) {
        return noticeJpaRepository.findAll(pageable)
                .map(NoticeMapper::toDomain);
    }

    @Override
    public void delete(Notice notice) {
        if (notice.getId() != null) {
            noticeJpaRepository.deleteById(notice.getId());
        }
    }
}
