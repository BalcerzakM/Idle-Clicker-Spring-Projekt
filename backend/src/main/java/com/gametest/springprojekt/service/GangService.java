package com.gametest.springprojekt.service;

import com.gametest.springprojekt.dto.FullGangInfoDto;
import com.gametest.springprojekt.dto.GangCombatDto;
import com.gametest.springprojekt.dto.GangInfoDto;
import com.gametest.springprojekt.exception.*;
import com.gametest.springprojekt.model.CharacterEntity;
import com.gametest.springprojekt.model.mapper.GangDtoMapper;
import com.gametest.springprojekt.model.GangEntity;
import com.gametest.springprojekt.model.mapper.CharacterMapper;
import com.gametest.springprojekt.repository.CharacterRepository;
import com.gametest.springprojekt.repository.GangRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GangService {
    private final CharacterRepository characterRepository;
    private final GangRepository gangRepository;
    private final int INITIAL_BALANCE = 0;//początkowa wartość jaką gangi mają w skarbcach
    private final GangDtoMapper gangDtoMapper;
    private final CharacterMapper characterMapper;
    private final CombatService combatService;

    @Transactional
    public GangEntity createGang(CharacterEntity character, String name, String description, String emblemPicturePath) {
        if (character.getGang() != null) {
            throw new CharacterIsInAGangException("Gracz jest aktualnie w innym gangu!");
        }

        if (gangRepository.findByGangName(name).isPresent()) {
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
    public void addMember(CharacterEntity leader ,String gangName, String characterName) { //tylko lider może dodać do gangu


        GangEntity gang = gangRepository.findByGangName(gangName)
                .orElseThrow(() -> new RuntimeException("Gang nie istnieje"));

        if (!gang.getLeader().getId().equals(leader.getId())) {
            throw new PermissionDeniedException("Tylko lider może dodać członka");
        }

        CharacterEntity character = characterRepository.findByNameIgnoreCase(characterName)
                .orElseThrow(() -> new RuntimeException("Postać nie istnieje"));

        if (character.getGang() != null) {
            throw new CharacterIsInAGangException("Postać jest już w innym gangu.");
        }

        character.setGang(gang);
        gang.getMembers().add(character);

        characterRepository.save(character);
        gangRepository.save(gang);
    }

    @Transactional
    public void quitGang(CharacterEntity character) {


        GangEntity gang = character.getGang();

        if (gang == null) {
            throw new RuntimeException("Postać nie należy do żadnego gangu.");
        }

        character.setGang(null);
        gang.getMembers().remove(character);

        if (gang.getMembers().isEmpty()) {
            gang.setLeader(null);
            gang.setRequests(null);
            gangRepository.delete(gang);
        }
        else if (gang.getLeader() == character) {
            gang.setLeader(gang.getMembers().iterator().next());
        }



        characterRepository.save(character);
//        gangRepository.save(gang);
    }

    @Transactional
    public void removeMember(CharacterEntity leader ,String characterName) {

        CharacterEntity character = characterRepository.findByNameIgnoreCase(characterName)
                .orElseThrow(() -> new RuntimeException("Postać nie istnieje"));

        if (character.getGang() == null) {
            throw new RuntimeException("Postać nie należy do żadnego gangu.");
        }

        GangEntity gang = character.getGang();


        if (gang.getLeader() != leader) {
            throw new PermissionDeniedException("Tylko lider może usunąć członka");
        }

        gang.getMembers().remove(character);
        character.setGang(null);

        characterRepository.save(character);
        gangRepository.save(gang);
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


    @Transactional(readOnly = true)
    public Page<GangInfoDto> getGangList(Pageable pageable) {
        return gangRepository
                .findAll(pageable)
                .map(gangDtoMapper::toDto);
    }

    @Transactional
    public void requestToJoin(CharacterEntity character, String gangName) {
        if (character.getGang() != null) {
            throw new CharacterIsInAGangException("Należysz już do gangu.");
        }
        GangEntity gang = gangRepository.findByGangName(gangName)
                .orElseThrow(() -> new RuntimeException("Gang nie istnieje"));

        if(gang.getMembers().size()>30){
            throw new GangAlreadyFullException("Limit miejsc tego gangu został osiągnięty. ");
        }

        if(gang.getRequests().contains(character)){
            throw new RuntimeException("Już wysłano prośbę do tego gangu. ");
        }

        gang.getRequests().add(character);

        gangRepository.save(gang);
    }

    @Transactional(readOnly = true)
    public FullGangInfoDto getGangInfo(String gangName) {
        GangEntity gang = gangRepository.findByGangName(gangName).orElseThrow(() -> new RuntimeException("Podany gang nie istnieje"));
        FullGangInfoDto dto = new FullGangInfoDto();
        dto.setGangName(gang.getGangName());
        dto.setGangDesc(gang.getGangDescription());
        dto.setGangLeader(gang.getLeader().getName());
        dto.setMembersCount(gang.getMembers().size());
        dto.setMembers(gang.getMembers().stream().map(characterMapper::toDto).collect(Collectors.toSet())); //jak to działa to alleluja
        dto.setRequests(gang.getRequests().stream().map(character -> character.getName()).collect(Collectors.toSet()));
        dto.setGangEmblem(gang.getEmblemPicturePath());
        dto.setCristalBank(gang.getCristalBank());
        dto.setMoneyBank(gang.getMoneyBank());
        dto.setGangToAttackName(gang.getGangToAttack());
        dto.setVotes(gang.getVotes());

        return dto;
    }

    @Transactional
    public void acceptJoinRequest(String gangName, String characterName, CharacterEntity currentCharacter) {
        addMember(currentCharacter,gangName,characterName);
        GangEntity gang = gangRepository.findByGangName(gangName)
                .orElseThrow(() -> new RuntimeException("Gang nie istnieje"));
        CharacterEntity character = characterRepository.findByNameIgnoreCase(characterName)
                .orElseThrow(() -> new RuntimeException("Postać nie istnieje"));

        gang.getRequests().remove(character);
        gangRepository.save(gang);
    }

    @Transactional
    public void rejectJoinRequest(String gangName, String characterName, CharacterEntity currentCharacter) {
        GangEntity gang = gangRepository.findByGangName(gangName)
                .orElseThrow(() -> new RuntimeException("Gang nie istnieje. "));

        if (gang.getLeader()!=currentCharacter) {
            throw new PermissionDeniedException("Tylko lider może usunąć prośbę o dołączenie.");
        }

        CharacterEntity character = characterRepository.findByNameIgnoreCase(characterName)
                .orElseThrow(() -> new RuntimeException("Postać nie istnieje"));

        gang.getRequests().remove(character);
        gangRepository.save(gang);
    }

    private List<CharacterEntity> getGangMembersList(GangEntity gang) {
        return gang.getMembers().stream()
                .sorted(Comparator.comparingInt(CharacterEntity::getAuraLvl).reversed())
                .toList();
    }

    @Transactional
    public @Nullable GangCombatDto startGangCombat(CharacterEntity currentCharacter) {

        GangEntity gang1 = currentCharacter.getGang();

        if (gang1.getLeader() != currentCharacter) {
            throw new PermissionDeniedException("Tylko lider może zacząć bitwę gangów.");
        }

        GangEntity gang2 = gangRepository.findByGangName(gang1.getGangToAttack())
                .orElseThrow(() -> new RuntimeException("Gang nie istnieje"));

        if (gang1.getVotes() < gang1.getMembers().size()/2) {
            throw new InsufficientVotesException("Oddano za mało głosów");
        }

        List<CharacterEntity> gangA = getGangMembersList(gang1);
        List<CharacterEntity> gangB = getGangMembersList(gang2);

        GangCombatDto result = combatService.startGangCombat(gangA, gangB);

        gang1.setGangToAttack(null);
        gangRepository.save(gang1);

        return result;
    }

    @Transactional
    public @Nullable Object startVote(CharacterEntity character, String gangName) {

        GangEntity gang = character.getGang();

        if (gang == null) {
            throw new PermissionDeniedException("Nie Należysz do żadnego gangu.");
        }

        if (!gangRepository.existsByGangName(gangName)) {
            throw new RuntimeException("Gang nie istnieje.");
        }

        if (gang.getGangToAttack() != null) {
            throw new VotingAlreadyActiveException("Aktywne głosowanie już trwa");
        }

        gang.setGangToAttack(gangName);
        gang.setVotes(1);
        gangRepository.save(gang);
        return "Stworzono głosowanie";
    }

    @Transactional
    public @Nullable Object addAVote(CharacterEntity character) {

        GangEntity gang = character.getGang();

        if (gang == null) {
            throw new PermissionDeniedException("Nie Należysz do żadnego gangu.");
        }

        gang.setVotes(gang.getVotes()+1);
        gangRepository.save(gang);
        return "Pomyślnie zagłosowano";
    }


}
