package com.gametest.sfgspringprojekt.repository;

import com.gametest.sfgspringprojekt.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Player, Long> {

    Player getByUsername(String username);
}
