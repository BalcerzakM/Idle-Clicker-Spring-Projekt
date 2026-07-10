package com.gametest.springprojekt.repository;

import com.gametest.springprojekt.model.EffectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EffectRepository extends JpaRepository<EffectEntity,Long> {
}
