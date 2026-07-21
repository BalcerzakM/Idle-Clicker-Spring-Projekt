package com.gametest.springprojekt;

import com.gametest.springprojekt.model.*;
import com.gametest.springprojekt.model.enums.SlotType;
import com.gametest.springprojekt.repository.*;
import com.gametest.springprojekt.service.GangService;
import com.gametest.springprojekt.service.ItemShopService;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CharacterClassRepository characterClassRepository;
    private final ItemShopService itemShopService;
    private final BaseItemRepository baseItemRepository;
    private final ItemRepository itemRepository;
    private final GangService gangService;
    private final GangRepository gangRepository;

    @Override
    public void run(String... args) throws Exception {

        if(userRepository.count() > 1){
            return;
        }

        Faker faker = new Faker();

        List<CharacterClassEntity> classes = characterClassRepository.findAll();
        List<UserEntity> users = new ArrayList<>();

        List<BaseItemEntity> baseItems = new ArrayList<>();

        //dodanie graczy
        for (int i = 0; i < 150; i++){
            UserEntity user = new UserEntity();

            String noun = faker.word().noun();
            String name = faker.name().firstName() + Character.toUpperCase(noun.charAt(0)) +  noun.substring(1);
            if (faker.random().nextBoolean()){
                name = name + faker.number().digits(2);
            }

            user.setUsername(name);
            user.setEmail(faker.internet().emailAddress());
            user.setRole("USER");
            user.setPassword(passwordEncoder.encode("TEST1234"));

            CharacterClassEntity randomClass = classes.get(faker.random().nextInt(classes.size()));
            CharacterEntity character = new CharacterEntity();

            character.setUser(user);
            character.setName(name);

            character.setCharacterClass(randomClass);
            character.setAvatarPicture("avatar" + faker.number().numberBetween(1, 17) +".png");

            int auraLvl = faker.number().numberBetween(1,100);
            character.setAuraLvl(auraLvl);

            character.setAura(faker.number().numberBetween(1,auraLvl));
            character.setMoney(auraLvl*100);
            character.setCristals(auraLvl*10);
            character.setRizz(faker.number().numberBetween(1,auraLvl));
            character.setStrength(faker.number().numberBetween(1,auraLvl));
            character.setAgility(faker.number().numberBetween(1,auraLvl));
            character.setEndurance(faker.number().numberBetween(1,auraLvl));
            character.setLuck(faker.number().numberBetween(1,auraLvl));

            for (SlotType slotType : SlotType.values()){
                if (slotType.equals(SlotType.NONE)) break;

                baseItems.addAll(baseItemRepository.findBySlotType(slotType));
                ItemEntity item = itemShopService.generateItemEntity(baseItems.get(faker.random().nextInt(baseItems.size())), character, 0);
                itemRepository.save(item);
                character.equipItem(item);
                baseItems.clear();
            }

            character.setCurrentBoss(faker.number().numberBetween(1,5));

            user.setCharacters(List.of(character));

            users.add(user);
        }

        if (!users.isEmpty()){
            userRepository.saveAll(users);
        }

        //dodanie gangow
        for (int i = 0; i < 12; i++) {
            UserEntity gangLeader = users.get(faker.random().nextInt(users.size()));
            String gangName = faker.word().adjective().toUpperCase();

            GangEntity gang = gangService.createGang(
                    gangLeader.getCharacters().getFirst(),
                    gangName,
                    faker.internet().emailSubject(),
                    "gangEmblem" + faker.number().numberBetween(1, 5)
            );

            users.remove(gangLeader);

            gangRepository.save(gang);

            for (int j = 0; j < faker.number().numberBetween(2, 10) && !users.isEmpty(); j++) {
                UserEntity gangMember = users.get(faker.random().nextInt(users.size()));

                gangService.addMember(gangLeader.getCharacters().getFirst(), gangName, gangMember.getCharacters().getFirst().getName());

                users.remove(gangMember);
            }

            gangRepository.save(gang);
        }
    }
}
