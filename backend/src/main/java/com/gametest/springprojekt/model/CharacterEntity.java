package com.gametest.springprojekt.model;

import com.gametest.springprojekt.exception.BackpackIsAlreadyFullException;
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
    @Transient
    private final int MAX_BACKPACK_SLOTS = 5;

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

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL,  orphanRemoval = true)
    private List<BackpackItem> backpack = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "active_quest_id")
    private ActiveQuestEntity activeQuest;

    /**
     * Metoda dodająca nagrody do postaci oraz zwalniająca slota activeQuest
     * @param bonusAura
     * @param bonusMoney
     */
    public void grantQuestReward(int bonusAura, int bonusMoney) {// do ewentualnej generalizacji (nagrody z innych źródeł)
        this.aura += bonusAura;
        this.money += bonusMoney;
        this.activeQuest = null;
    }

    //dodanie itema do plecaka
    public void addItemToBackpack(ItemEntity item) {
        if (backpack.size() >= MAX_BACKPACK_SLOTS) {
            throw new BackpackIsAlreadyFullException("Plecak jest pelny!");
        }

        backpack.add(new BackpackItem(null, this, item));
    }

    //to juz wstepnie na przyszlosc
    public void removeItemFromBackpack(BackpackItem backpackItem) {
        backpack.remove(backpackItem);
    }
}
