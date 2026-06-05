package com.gametest.springprojekt.model;

import com.gametest.springprojekt.exception.BackpackIsAlreadyFullException;
import com.gametest.springprojekt.model.enums.CharacterClass;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;


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
    private String avatarPicture;
    private int auraLvl;
    private int aura;
    private int rizz;
    private int strength;
    private int agility;
    private int endurance;
    private int luck;
    private int money;
    private int cristals;

    @OneToMany(mappedBy = "player", cascade = CascadeType.PERSIST, orphanRemoval = true) //aby się zapisywało po założeniu i usuwało po zdjęciu
    private List<EquipmentItem> equipment = new ArrayList<>();

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
    public void grantQuestReward(int bonusAura, int bonusMoney, ItemEntity rewardItem) {// do ewentualnej generalizacji (nagrody z innych źródeł)
        this.aura += bonusAura;
        this.money += bonusMoney;
        this.activeQuest = null;
        if (rewardItem != null) {
            this.addItemToBackpack(rewardItem);
        }
    }

    //dodanie itema do plecaka
    public void addItemToBackpack(ItemEntity item) {
        if (backpack.size() >= MAX_BACKPACK_SLOTS) {
            throw new BackpackIsAlreadyFullException("Plecak jest pelny!");
        }

        backpack.add(new BackpackItem(null, this, item));
    }

    //tutaj trzeba jeszcze walidacje dodać
    public void equipItem(ItemEntity item) {
        equipment.add(new EquipmentItem(null, item.getBaseItem().getSlotType(), item, this));
    }


    public Map<String, Integer> getEquipmentStatsSum() {
        Map<String, Integer> totals = new HashMap<>();
        totals.put("rizz", this.rizz);
        totals.put("strength", this.strength);
        totals.put("agility", this.agility);
        totals.put("endurance", this.endurance);
        totals.put("luck", this.luck);

        for (EquipmentItem eq : equipment) {
            ItemEntity item = eq.getItem();
            if (item != null) {
                totals.merge("rizz", item.getTotalRizz(), Integer::sum);
                totals.merge("strength", item.getTotalStrength(), Integer::sum);
                totals.merge("agility", item.getTotalAgility(), Integer::sum);
                totals.merge("endurance", item.getTotalEndurance(), Integer::sum);
                totals.merge("luck", item.getTotalLuck(), Integer::sum);
            }
        }
        return totals;
    }// trzeba dodać te stąd

    public void updateAuraLevel() {
        int aura = this.getAura();
        int auraLevel = 1 + (int) Math.sqrt(aura / 100.0);

        this.setAuraLvl(auraLevel);
    }
}
