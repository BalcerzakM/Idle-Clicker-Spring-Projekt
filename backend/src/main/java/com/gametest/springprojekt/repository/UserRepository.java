package com.gametest.springprojekt.repository;

import com.gametest.springprojekt.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<UserEntity> getByUsername(String username);

    Optional<UserEntity> getUserEntityById(Long id);

    List<UserEntity> findByIsBannedIsTrue();
}
