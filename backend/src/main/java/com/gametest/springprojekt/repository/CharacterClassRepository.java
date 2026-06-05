package com.gametest.springprojekt.repository;

import com.gametest.springprojekt.model.CharacterClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CharacterClassRepository extends JpaRepository<CharacterClassEntity, Long> {

    Optional<CharacterClassEntity> findByClassName(String className);

}
