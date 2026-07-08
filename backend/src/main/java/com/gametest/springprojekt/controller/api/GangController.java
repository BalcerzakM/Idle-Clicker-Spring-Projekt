package com.gametest.springprojekt.controller.api;

import com.gametest.springprojekt.dto.FullGangInfoDto;
import com.gametest.springprojekt.dto.GangInfoDto;
import com.gametest.springprojekt.model.CharacterEntity;
import com.gametest.springprojekt.service.CharacterService;
import com.gametest.springprojekt.service.GangService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/gang")
@RequiredArgsConstructor
public class GangController {

    private final GangService gangService;
    private final CharacterService characterService;

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

        gangService.depositMoney(gangName, amount);

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

}