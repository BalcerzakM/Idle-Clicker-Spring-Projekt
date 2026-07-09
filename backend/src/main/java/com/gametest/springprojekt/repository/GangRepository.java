package com.gametest.springprojekt.repository;

import com.gametest.springprojekt.model.GangEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GangRepository extends JpaRepository<GangEntity, Long> {

    Optional<GangEntity> findByGangName(String gangName);

}
