package com.gametest.springprojekt.repository;

import com.gametest.springprojekt.model.BaseItemEntity;
import com.gametest.springprojekt.model.enums.ItemType;
import com.gametest.springprojekt.model.enums.SlotType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BaseItemRepository extends JpaRepository<BaseItemEntity, Long> {
    List<BaseItemEntity> findBySlotTypeNot(SlotType slotType);

    List<BaseItemEntity> findBySlotType(SlotType slotType);

    List<BaseItemEntity> findByItemType(ItemType itemType);
}
