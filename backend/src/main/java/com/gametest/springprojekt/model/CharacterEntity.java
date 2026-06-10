package com.gametest.springprojekt.model;

import com.gametest.springprojekt.exception.BackpackIsAlreadyFullException;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CharacterEntity {
    @Transient
    private final int MAX_BACKPACK_SLOTS = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String name;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_class_id", nullable = false)
    private CharacterClassEntity characterClass;

    @NotBlank
    @Column(nullable = false)
    private String avatarPicture;

    @Positive
    @Column(nullable = false)
    private int auraLvl;

    @PositiveOrZero
    @Column(nullable = false)
    private int aura;

    @PositiveOrZero
    @Column(nullable = false)
    private int rizz;

    @PositiveOrZero
    @Column(nullable = false)
    private int strength;

    @PositiveOrZero
    @Column(nullable = false)
    private int agility;

    @PositiveOrZero
    @Column(nullable = false)
    private int endurance;

    @PositiveOrZero
    @Column(nullable = false)
    private int luck;

    @PositiveOrZero
    @Column(nullable = false)
    private int money;

    @PositiveOrZero
    @Column(nullable = false)
    private int cristals;

    @Size(max = 7)
    @OneToMany(mappedBy = "player", cascade = CascadeType.PERSIST, orphanRemoval = true) //aby się zapisywało po założeniu i usuwało po zdjęciu
    private List<EquipmentItem> equipment = new ArrayList<>();

    @Size(max = MAX_BACKPACK_SLOTS)
    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL,  orphanRemoval = true)
    private List<BackpackItem> backpack = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "active_quest_id")
    private ActiveQuestEntity activeQuest;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "active_vehicle_id")
    private ActiveVehicleEntity activeVehicle;
  
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "bouncer_duty_id")
    private BouncerDutyEntity bouncerDuty;

    /**
     * Metoda dodająca nagrody do postaci oraz zwalniająca slota activeQuest
     * @param bonusAura
     * @param bonusMoney
     */
    public void grantQuestReward(int bonusAura, int bonusMoney, ItemEntity rewardItem) {// do ewentualnej generalizacji (nagrody z innych źródeł)
        this.addAura(bonusAura);
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

    private void updateAuraLevel() {
        int aura = this.getAura();
        int auraLevel = 1 + (int) Math.sqrt(aura / 100.0);

        this.setAuraLvl(auraLevel);
    }

    public void addAura(int bonusAura) {
        this.aura += bonusAura;
        updateAuraLevel();
    }
}
