package com.gametest.springprojekt.repository;

import com.gametest.springprojekt.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity getByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
