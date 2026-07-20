package com.gametest.springprojekt.model;

import com.gametest.springprojekt.dto.GangCombatDto;
import com.gametest.springprojekt.model.mapper.GangCombatDtoJsonConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GangEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "gang")
    @Column(nullable = false)
    private Set<CharacterEntity> members;

    @NotBlank
    @Size(min = 3, max = 20)
    @Column(nullable = false)
    private String gangName;

    @NotBlank
    @Size(min = 3, max = 200)
    @Column(nullable = false)
    private String gangDescription;

    @OneToOne(fetch = FetchType.LAZY)
    private CharacterEntity leader;

    @PositiveOrZero
    private int moneyBank;

    @PositiveOrZero
    private int cristalBank;

    @NotBlank
    @Column(nullable = false)
    private String emblemPicturePath;

    @ManyToMany
    private Set<CharacterEntity> requests; //prosby o dołączenie


    private String gangToAttack;

    @PositiveOrZero
    private int votes;

    @Convert(converter = GangCombatDtoJsonConverter.class)
    @Column(columnDefinition = "json")
    private GangCombatDto lastCombat;// przechowujemy ostatnią walke jako json


    //kiedyś można dodać chat
}
