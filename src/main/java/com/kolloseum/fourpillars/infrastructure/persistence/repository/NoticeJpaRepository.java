package com.kolloseum.fourpillars.infrastructure.persistence.repository;

import com.kolloseum.fourpillars.infrastructure.persistence.entity.NoticeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeJpaRepository extends JpaRepository<NoticeEntity, Long> {
}
