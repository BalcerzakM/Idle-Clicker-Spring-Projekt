package com.gametest.springprojekt.repository;

import com.gametest.springprojekt.model.ShopOfferEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ShopOfferRepository extends JpaRepository<ShopOfferEntity, Long> {

    List<ShopOfferEntity> findByOfferDate(LocalDate date);
}
