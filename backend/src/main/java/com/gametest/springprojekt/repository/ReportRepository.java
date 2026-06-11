package com.gametest.springprojekt.repository;

import com.gametest.springprojekt.model.ReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<ReportEntity, Long> {
    List<ReportEntity> findAllByOrderByCreatedAtDesc();
}
