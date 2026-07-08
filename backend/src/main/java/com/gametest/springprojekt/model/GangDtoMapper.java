package com.gametest.springprojekt.model;

import com.gametest.springprojekt.dto.CharacterDto;
import com.gametest.springprojekt.dto.GangInfoDto;
import org.springframework.stereotype.Component;

@Component
public class GangDtoMapper {
    public GangInfoDto toDto(GangEntity gang) {
        GangInfoDto dto = new GangInfoDto();

        dto.setMembersCount(gang.getMembers().size());
        dto.setGangName(gang.getGangName());
        dto.setGangDesc(gang.getGangDescription());
        dto.setGangEmblem(gang.getEmblemPicturePath());
        dto.setGangLeader(gang.getLeader().getName());
        return dto;
    }
}
