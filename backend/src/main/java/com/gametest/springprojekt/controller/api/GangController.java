package com.gametest.springprojekt.controller.api;

import com.gametest.springprojekt.dto.FullGangInfoDto;
import com.gametest.springprojekt.dto.GangCombatDto;
import com.gametest.springprojekt.dto.GangInfoDto;
import com.gametest.springprojekt.model.CharacterEntity;
import com.gametest.springprojekt.service.CharacterService;
import com.gametest.springprojekt.service.CombatService;
import com.gametest.springprojekt.service.GangService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/gang")
@RequiredArgsConstructor
public class GangController {

    private final GangService gangService;
    private final CharacterService characterService;
    private final CombatService combatService;

    @PostMapping("/create")
    public String createGang(@RequestParam String name,
                             @RequestParam String description,
                             @RequestParam String emblemPath) {

        CharacterEntity character = characterService.getCurrentCharacter();

        gangService.createGang(character, name, description, emblemPath);

        return "Gang został utworzony.";
    }

    @PostMapping("/{gangName}/addMember")
    public String addMember(@PathVariable String gangName,
                            @RequestParam String characterName) {
        gangService.addMember(characterService.getCurrentCharacter(),gangName, characterName);

        return "Dodano członka.";
    }

    @DeleteMapping("/removeMember")
    public String removeMember(@RequestParam String characterName) {

        gangService.removeMember(characterService.getCurrentCharacter(), characterName);

        return "Usunięto członka.";
    }

    @DeleteMapping("/quitGang")
    public String quitGang() {
        gangService.quitGang(characterService.getCurrentCharacter());
        return "Opuszczono Gang.";
    }

    @PutMapping("/{gangName}/depositMoney")
    public String depositMoney(@PathVariable String gangName,
                               @RequestParam int amount) {
        CharacterEntity character = characterService.getCurrentCharacter();
        gangService.depositMoney(character,gangName, amount);

        return "Wpłacono pieniądze.";
    }

    @PutMapping("/{gangName}/depositCristals")
    public String depositCristals(@PathVariable String gangName,
                                  @RequestParam int amount) {

        gangService.depositCristals(gangName, amount);

        return "Wpłacono kryształy.";
    }


    @GetMapping
    public Page<GangInfoDto> getGangList(
            Pageable pageable
    ){
        return gangService.getGangList(pageable);
    }

    @GetMapping("/{gangName}/info")
    public FullGangInfoDto getGangInfo(
            @PathVariable String gangName
    ){
        return gangService.getGangInfo(gangName);
    }

    @PostMapping("/{gangName}/join")
    public String requestToJoin(@PathVariable String gangName) {
        CharacterEntity character = characterService.getCurrentCharacter();
        gangService.requestToJoin(character, gangName );

        return "Wysłano prośbę.";
    }

    @PostMapping("/{gangName}/acceptRequest")
    public String acceptJoinRequest(@PathVariable String gangName,
                                    @RequestParam String characterName) {
        CharacterEntity currentCharacter = characterService.getCurrentCharacter();
        gangService.acceptJoinRequest(gangName, characterName, currentCharacter);
        return "Prośba o dołączenie została zaakceptowana.";
    }

    @PostMapping("/{gangName}/rejectRequest")
    public String rejectJoinRequest(@PathVariable String gangName,
                                    @RequestParam String characterName) {
        CharacterEntity currentCharacter = characterService.getCurrentCharacter();
        gangService.rejectJoinRequest(gangName, characterName, currentCharacter);
        return "Prośba o dołączenie została odrzucona.";
    }

    @PostMapping("/startBattle")
    public ResponseEntity<GangCombatDto> startGangBattle() {
        CharacterEntity currentCharacter = characterService.getCurrentCharacter();
        return ResponseEntity.ok(gangService.startGangCombat(currentCharacter));
    }

    @PostMapping("/startVote") //głosowanie o walke z danym gangiem
    public ResponseEntity<?> startVote(
            @RequestParam String gangName
    ) {
        CharacterEntity currentCharacter = characterService.getCurrentCharacter();
        return ResponseEntity.ok(gangService.startVote(currentCharacter ,gangName));
    }


    @PatchMapping("/vote")
    public ResponseEntity<?> addVote() {
        CharacterEntity currentCharacter = characterService.getCurrentCharacter();
        return ResponseEntity.ok(gangService.addAVote(currentCharacter));
    }

    @GetMapping("/watch")
    public ResponseEntity<GangCombatDto> watchGangBattle() {
        CharacterEntity currentCharacter = characterService.getCurrentCharacter();
        return ResponseEntity.ok(gangService.watchLastGangCombat(currentCharacter));
    }

}