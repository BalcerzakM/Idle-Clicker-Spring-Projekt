package com.gametest.springprojekt.service;

import com.gametest.springprojekt.exception.CharacterIsInAGangException;
import com.gametest.springprojekt.exception.GangNameTakenException;
import com.gametest.springprojekt.exception.PermissionDeniedException;
import com.gametest.springprojekt.model.CharacterEntity;
import com.gametest.springprojekt.model.GangEntity;
import com.gametest.springprojekt.repository.CharacterRepository;
import com.gametest.springprojekt.repository.GangRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class GangService {
    private final CharacterRepository characterRepository;
    private final GangRepository gangRepository;
    private final int INITIAL_BALANCE = 0;//początkowa wartość jaką gangi mają w skarbcach

    @Transactional
    public GangEntity createGang(CharacterEntity character, String name, String description, String emblemPicturePath) {
        if (character.getGang() != null) {
            throw new CharacterIsInAGangException("Gracz jest aktualnie w innym gangu!");
        }

        if (gangRepository.findByGangName(name) != null){
            throw new GangNameTakenException("Gang o tej nazwie już istnieje!");
        }
        GangEntity gang = new GangEntity();
        gang.setGangName(name);
        gang.setGangDescription(description);
        gang.setEmblemPicturePath(emblemPicturePath);
        gang.setMembers(Set.of(character));
        gang.setCristalBank(INITIAL_BALANCE);
        gang.setMoneyBank(INITIAL_BALANCE);
        gang.setLeader(character);

        character.setGang(gang);

        gangRepository.save(gang);
        return gang;
    }

    @Transactional
    public void addMember(CharacterEntity Leader ,String gangName, String characterName) { //tylko lider może dodać do gangu


        GangEntity gang = gangRepository.findByGangName(gangName)
                .orElseThrow(() -> new RuntimeException("Gang nie istnieje"));

        if (!gang.getMembers().contains(Leader)){
            throw new PermissionDeniedException("Tylko lider może dodać członka");
        }

        CharacterEntity character = characterRepository.findByNameIgnoreCase(characterName)
                .orElseThrow(() -> new RuntimeException("Postać nie istnieje"));

        if (character.getGang() != null) {
            throw new CharacterIsInAGangException("Postać jest już w innym gangu.");
        }

        character.setGang(gang);
        gang.getMembers().add(character);

//        characterRepository.save(character);
//        gangRepository.save(gang);
    }

    @Transactional
    public void quitGang(CharacterEntity character) {


        GangEntity gang = character.getGang();

        if (gang == null) {
            throw new RuntimeException("Postać nie należy do żadnego gangu.");
        }

        gang.getMembers().remove(character);
        character.setGang(null);
//
//        characterRepository.save(character);
//        gangRepository.save(gang);
    }

    @Transactional
    public void removeMember(CharacterEntity Leader ,String characterName) {

        CharacterEntity character = characterRepository.findByNameIgnoreCase(characterName)
                .orElseThrow(() -> new RuntimeException("Postać nie istnieje"));

        GangEntity gang = character.getGang();

        if (!gang.getMembers().contains(Leader)){
            throw new PermissionDeniedException("Tylko lider może usunąć członka");
        }

        if (gang == null) {
            throw new RuntimeException("Postać nie należy do żadnego gangu.");
        }


        gang.getMembers().remove(character);
        character.setGang(null);
//
//        characterRepository.save(character);
//        gangRepository.save(gang);
    }

    @Transactional
    public void depositMoney(String gangName, int amount) {

        if (amount <= 0) {
            throw new IllegalArgumentException("Kwota musi być dodatnia.");
        }

        GangEntity gang = gangRepository.findByGangName(gangName)
                .orElseThrow(() -> new RuntimeException("Gang nie istnieje"));

        gang.setMoneyBank(gang.getMoneyBank() + amount);

        gangRepository.save(gang);
    }

    @Transactional
    public void depositCristals(String gangName, int amount) {

        if (amount <= 0) {
            throw new IllegalArgumentException("Kwota musi być dodatnia.");
        }

        GangEntity gang = gangRepository.findByGangName(gangName)
                .orElseThrow(() -> new RuntimeException("Gang nie istnieje"));

        gang.setCristalBank(gang.getCristalBank() + amount);

        gangRepository.save(gang);
    }




}
