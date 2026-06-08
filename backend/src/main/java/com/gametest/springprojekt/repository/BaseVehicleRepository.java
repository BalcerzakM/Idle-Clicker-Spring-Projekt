package com.gametest.springprojekt.repository;

import com.gametest.springprojekt.model.BaseVehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseVehicleRepository extends JpaRepository<BaseVehicleEntity, Long> {
}
