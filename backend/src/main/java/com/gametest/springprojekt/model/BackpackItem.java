package com.gametest.springprojekt.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class BackpackItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne(fetch = FetchType.LAZY)
    private CharacterEntity player;

    @ManyToOne(fetch = FetchType.LAZY)
    private ItemEntity item;

}
