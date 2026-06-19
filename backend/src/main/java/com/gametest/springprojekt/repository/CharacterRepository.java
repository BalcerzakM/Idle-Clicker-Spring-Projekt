package com.gametest.springprojekt.repository;

import com.gametest.springprojekt.model.CharacterEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CharacterRepository extends JpaRepository<CharacterEntity,Long> {
    boolean existsByName(String name);

    @Query("SELECT COUNT(c) FROM CharacterEntity c WHERE c.auraLvl > :aura OR (c.auraLvl = :aura AND c.id > :id)")
    long countHigherRanked(@Param("aura") int aura, @Param("id") Long id);

    Optional<CharacterEntity> findByNameIgnoreCase(String trim);
}
