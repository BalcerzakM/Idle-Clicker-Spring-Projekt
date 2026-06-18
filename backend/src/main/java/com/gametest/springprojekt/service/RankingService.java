package com.gametest.springprojekt.service;

import com.gametest.springprojekt.dto.CharacterDto;
import com.gametest.springprojekt.dto.RankingPlayerDto;
import com.gametest.springprojekt.dto.RankingPositionDto;
import com.gametest.springprojekt.exception.CharacterNotFoundException;
import com.gametest.springprojekt.model.CharacterEntity;
import com.gametest.springprojekt.model.CharacterMapper;
import com.gametest.springprojekt.model.EquipmentItem;
import com.gametest.springprojekt.model.UserEntity;
import com.gametest.springprojekt.model.enums.SlotType;
import com.gametest.springprojekt.repository.CharacterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RankingService {
    private final CharacterRepository characterRepository;
    private final CharacterMapper characterMapper;

    @Transactional(readOnly = true)
    public Page<CharacterDto> getRanking(Pageable pageable) {
        return characterRepository
                .findAll(pageable)
                .map(characterMapper::toDto);
    }

    @Transactional(readOnly = true)
    public RankingPositionDto findPlayerInRanking(String search, int pageSize) {
        CharacterEntity character = characterRepository
                .findByNameIgnoreCase(search.trim())  //dokładne dopasowanie
                .orElse(null);

        if (character == null) {
            return null;
        }

        long rank = characterRepository.countHigherRanked(
                character.getAuraLvl(), character.getId()) + 1;

        int page = (int) ((rank - 1) / pageSize);

        return new RankingPositionDto(rank, page);
    }

    @Transactional(readOnly = true)
    public RankingPlayerDto getPlayersInfoFromRanking(String playerName) {
        CharacterEntity character = characterRepository.findByNameIgnoreCase(playerName).orElse(null);

        if (character == null) {
            throw new CharacterNotFoundException("Nie znaleziono postaci o nazwie " + playerName );
        }

        Map< SlotType, String > eqItemsPicrures = character.getEquipment().stream()
                .collect(Collectors.toMap(
                        EquipmentItem::getSlot,
                        eq -> eq.getItem()
                                .getBaseItem().getImagePath()
                ));

        Map<String, Integer> stats = character.getEquipmentStatsSum();

        return new RankingPlayerDto(
                character.getName(),
                character.getAvatarPicture(),
                eqItemsPicrures,
                character.getAuraLvl(),
                stats.get("rizz"),
                stats.get("strength"),
                stats.get("agility"),
                stats.get("endurance"),
                stats.get("luck")
        );
    }

    //do admina
    public CharacterEntity getCharacterFromPlayersList(String charactersName) {
        CharacterEntity character = characterRepository
                .findByNameIgnoreCase(charactersName.trim())  //dokładne dopasowanie
                .orElse(null);

        if (character == null) {
            return null;
        }
        return character;
    }
}
