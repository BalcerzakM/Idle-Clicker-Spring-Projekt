package com.gametest.springprojekt.dto;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FullGangInfoDto {
    private int membersCount;
    private Set<CharacterDto> members;
    private String gangName;
    private String gangDesc;
    private String gangLeader;
    private String gangEmblem;
    private Set<String> requests;
    private int moneyBank;
    private int cristalBank;
}
