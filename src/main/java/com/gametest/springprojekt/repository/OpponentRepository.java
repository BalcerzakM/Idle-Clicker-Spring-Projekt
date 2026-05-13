package com.gametest.springprojekt.repository;

import com.gametest.springprojekt.model.OpponentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpponentRepository extends JpaRepository<OpponentEntity,Long> {
}
