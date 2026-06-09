package com.gametest.springprojekt.model;

import com.gametest.springprojekt.dto.ItemDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ItemEntity {
    @Transient
    private final double ITEM_PRICE_DECREASE = 0.3;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private int totalRizz;

    @NotNull
    @Column(nullable = false)
    private int totalStrength;

    @NotNull
    @Column(nullable = false)
    private int totalAgility;

    @NotNull
    @Column(nullable = false)
    private int totalEndurance;

    @NotNull
    @Column(nullable = false)
    private int totalLuck;

    @NotNull
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

    public void decreaseItemPrice() {
        this.setPrice((int) Math.round(this.getPrice()* ITEM_PRICE_DECREASE));
    }
}
