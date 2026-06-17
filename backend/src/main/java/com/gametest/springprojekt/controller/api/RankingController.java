package com.gametest.springprojekt.controller.api;

import com.gametest.springprojekt.dto.CharacterDto;
import com.gametest.springprojekt.dto.RankingPlayerDto;
import com.gametest.springprojekt.dto.RankingPositionDto;
import com.gametest.springprojekt.service.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ranking")
@RequiredArgsConstructor
public class RankingController {

    private final RankingService rankingService;

    //domyślnie pierwsza strona i po 10 rekordów oraz sort po lewelu
    @GetMapping
    public Page<CharacterDto> getRanking(
            @PageableDefault(
                    size = 10,
                    sort = {"auraLvl", "id"}, //id dodane gdy są postacie o tym samym lvl
                    direction = Sort.Direction.DESC)
            Pageable pageable

    ){
        return rankingService.getRanking(pageable);
    }

    @GetMapping("/search")
    public ResponseEntity<RankingPositionDto> searchRanking(
            @RequestParam String name,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        RankingPositionDto result = rankingService.findPlayerInRanking(name, pageSize);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/player/{playerName}")
    public ResponseEntity<RankingPlayerDto> getPlayerFromRanking(
            @PathVariable String playerName
    ) {
        RankingPlayerDto player = rankingService.getPlayersInfoFromRanking(playerName);
        return ResponseEntity.ok(player);
    }
}
