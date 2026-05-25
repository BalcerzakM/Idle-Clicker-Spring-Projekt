package com.gametest.springprojekt.repository;

import com.gametest.springprojekt.model.BaseItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseItemRepository extends JpaRepository<BaseItemEntity, Long> {
}
