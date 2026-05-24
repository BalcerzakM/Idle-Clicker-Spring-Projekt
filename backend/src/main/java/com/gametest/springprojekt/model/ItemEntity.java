package com.gametest.springprojekt.model;

import com.gametest.springprojekt.model.enums.SlotType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //private String name;
    //private String description;
    //private SlotType slotType;
    private int bonusRizz;
    private int bonusStrength;
    private int bonusAgility;
    private int bonusEndurance;
    private int bonusLuck;
    private int price;
    //private String imagePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "base_item_id")
    private BaseItemEntity baseItem;
}
