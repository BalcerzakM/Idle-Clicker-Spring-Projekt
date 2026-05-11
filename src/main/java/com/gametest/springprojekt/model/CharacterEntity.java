package com.gametest.springprojekt.model;

import com.gametest.springprojekt.model.enums.CharacterClass;
import com.gametest.springprojekt.model.enums.SlotType;
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

    @ManyToOne
    private UserEntity user;
    private String name;
    private CharacterClass characterClass;
    private int rizz;
    private int strength;
    private int agility;
    private int endurance;
    private int luck;
    private int money;
    private int cristals;
//    @OneToMany
//    private Set<SlotType> equipment = new HashSet<>();
//    @OneToMany
//    private List<ItemEntity> items = new ArrayList<>();

}
