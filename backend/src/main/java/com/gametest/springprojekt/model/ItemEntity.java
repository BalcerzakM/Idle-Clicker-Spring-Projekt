package com.gametest.springprojekt.model;

import com.gametest.springprojekt.dto.ItemDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ItemEntity {
    @Transient
    private static final double ITEM_PRICE_DECREASE = 0.3;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @PositiveOrZero
    @Column(nullable = false)
    private int totalRizz;

    @PositiveOrZero
    @Column(nullable = false)
    private int totalStrength;

    @PositiveOrZero
    @Column(nullable = false)
    private int totalAgility;

    @PositiveOrZero
    @Column(nullable = false)
    private int totalEndurance;

    @PositiveOrZero
    @Column(nullable = false)
    private int totalLuck;

    @PositiveOrZero
    @Column(nullable = false)
    private int price;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "base_item_id", nullable = false)
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

    public void decreaseItemPrice() {
        this.setPrice((int) Math.round(this.getPrice()* ITEM_PRICE_DECREASE));
    }
}
