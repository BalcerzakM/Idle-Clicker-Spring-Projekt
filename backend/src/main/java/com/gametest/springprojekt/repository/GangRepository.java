package com.gametest.springprojekt.repository;

import com.gametest.springprojekt.model.GangEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GangRepository {
    void save(GangEntity gang);

    Optional<GangEntity> findByGangName(String gangName);
}
