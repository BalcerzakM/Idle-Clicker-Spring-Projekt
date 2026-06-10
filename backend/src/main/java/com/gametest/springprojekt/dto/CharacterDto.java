package com.gametest.springprojekt.dto;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class CharacterDto { //dto do wyświetlania rankingu, aby nie wyświetlać wszystkich info charactera

    private String name;

    private String characterClass;

    private int auraLvl;


}
