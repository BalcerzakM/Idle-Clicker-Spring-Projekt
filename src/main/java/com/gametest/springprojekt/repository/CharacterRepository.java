package com.gametest.springprojekt.repository;

import com.gametest.springprojekt.model.CharacterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterRepository extends JpaRepository<CharacterEntity,Long> {
}
