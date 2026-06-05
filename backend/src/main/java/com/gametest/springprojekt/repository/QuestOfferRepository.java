package com.gametest.springprojekt.repository;

import com.gametest.springprojekt.model.CharacterEntity;
import com.gametest.springprojekt.model.QuestEntity;
import com.gametest.springprojekt.model.QuestOfferEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestOfferRepository extends JpaRepository<QuestOfferEntity, Long> {
    List<QuestOfferEntity> findByCharacter(CharacterEntity character);

    QuestOfferEntity findByQuestAndCharacter(QuestEntity quest, CharacterEntity character);

    void deleteByCharacter(CharacterEntity character);
}
