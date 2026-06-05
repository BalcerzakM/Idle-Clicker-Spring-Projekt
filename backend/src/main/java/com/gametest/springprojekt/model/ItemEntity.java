package com.gametest.springprojekt.model;

import com.gametest.springprojekt.dto.ItemDto;
import com.gametest.springprojekt.model.enums.SlotType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int totalRizz;

    @Column(nullable = false)
    private int totalStrength;

    @Column(nullable = false)
    private int totalAgility;

    @Column(nullable = false)
    private int totalEndurance;

    @Column(nullable = false)
    private int totalLuck;

    @Column(nullable = false)
    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "base_item_id")
    private BaseItemEntity baseItem;

    public ItemDto generateItemDto() {
        return new ItemDto(
                id,
                this.getBaseItem().getName(),
                this.getBaseItem().getDescription(),
                this.getBaseItem().getItemType(),
                this.getBaseItem().getSlotType(),
                totalRizz,
                totalStrength,
                totalAgility,
                totalEndurance,
                totalLuck,
                price,
                this.getBaseItem().getImagePath()
        );
    }
}
