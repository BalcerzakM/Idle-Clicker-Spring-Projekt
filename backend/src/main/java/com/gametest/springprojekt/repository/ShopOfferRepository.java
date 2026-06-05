package com.gametest.springprojekt.repository;

import com.gametest.springprojekt.model.CharacterEntity;
import com.gametest.springprojekt.model.ShopOfferEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ShopOfferRepository extends JpaRepository<ShopOfferEntity, Long> {

    List<ShopOfferEntity> findByCharacter(CharacterEntity character);

    void deleteByCharacter(CharacterEntity character);

    List<ShopOfferEntity> findByCharacterAndOfferDate(CharacterEntity character, LocalDate date);

}
