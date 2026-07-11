package com.gametest.springprojekt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GangInfoDto {
    private int membersCount;
    private String gangName;
    private String gangDesc;
    private String gangLeader;
    private String gangEmblem;
}
