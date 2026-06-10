package com.gametest.springprojekt.dto;

import com.gametest.springprojekt.model.CharacterClassEntity;
import com.gametest.springprojekt.model.EquipmentItem;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CharacterDto { //dto do wyświetlania rankingu, aby nie wyświetlać wszystkich info charactera, potem trzeba dorobić mappera

    private String name;

    private CharacterClassEntity characterClass;

    private String avatarPicture;

    private int auraLvl;

    private List<EquipmentItem> equipment; //ekwipunek aby można było zobaczyć czyjś drip wiadomo

}
