package com.gametest.springprojekt.model;

import com.gametest.springprojekt.model.enums.SlotType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames =
                {"character_id", "slot"} //para typu: id_gracza + slot ma być unikalne przez co nie będzie dwóch itemów na jednym slocie
        )
)
public class EquipmentItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SlotType slot;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY) //aby sie wszcytywało dopiero wtedy gdy jest potrzebne
    @JoinColumn(name = "item_id", nullable = false)
    private ItemEntity item;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id", nullable = false)
    private CharacterEntity player;
}
