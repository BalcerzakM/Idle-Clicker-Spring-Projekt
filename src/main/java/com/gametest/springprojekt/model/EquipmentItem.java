package com.gametest.springprojekt.model;

import com.gametest.springprojekt.model.enums.SlotType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames =
                {"player_id", "slot"} //para typu: id_gracza + slot ma być unikalne przez co nie będzie dwóch itemów na jednym slocie
        )
)
public class EquipmentItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private SlotType slot;

    @ManyToOne(fetch = FetchType.LAZY) //aby sie wszcytywało dopiero wtedy gdy jest potrzebne
    @JoinColumn(name = "item_id")
    private ItemEntity item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    private CharacterEntity player;
}
