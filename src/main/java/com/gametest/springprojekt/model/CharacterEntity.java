package com.gametest.springprojekt.model;

import com.gametest.springprojekt.dto.QuestDto;
import com.gametest.springprojekt.model.enums.CharacterClass;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class CharacterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;
    private String name;
    private CharacterClass characterClass;
    private int auraLvl;
    private int aura;
    private int rizz;
    private int strength;
    private int agility;
    private int endurance;
    private int luck;
    private int money;
    private int cristals;

    @OneToMany(mappedBy = "player")
    private Set<EquipmentItem> equipment = new HashSet<>();

    @OneToMany(mappedBy = "player")
    private List<BackpackItem> backpack = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "active_quest_id")
    private QuestEntity activeQuest;

    /**
     * metoda dodająca nagrody do postaci oraz zwalniająca slota activeQuest
     * @param bonusAura
     * @param bonusMoney
     */
    public void grantQuestReward(int bonusAura, int bonusMoney) {// do ewentualnej generalizacji(nagrody z innych źródeł)
        this.aura += bonusAura;
        this.money += bonusMoney;
        this.activeQuest = null;
    }

}
