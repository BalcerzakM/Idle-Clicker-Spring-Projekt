package com.gametest.springprojekt;

import com.gametest.springprojekt.model.*;
import com.gametest.springprojekt.model.enums.CharacterClass;
import com.gametest.springprojekt.model.enums.QuestTier;
import com.gametest.springprojekt.model.enums.QuestType;
import com.gametest.springprojekt.model.enums.SlotType;
import com.gametest.springprojekt.repository.*;
import com.gametest.springprojekt.service.ItemShopService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class DataLoader implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final QuestRepository questRepository;
    private final OpponentRepository opponentRepository;
    private final CharacterRepository characterRepository;
    private final ItemRepository itemRepository;
    private final ItemShopService itemShopService;

    public DataLoader(UserRepository userRepository, PasswordEncoder passwordEncoder,  QuestRepository questRepository,  OpponentRepository opponentRepository, CharacterRepository characterRepository,  ItemRepository itemRepository,  ItemShopService itemShopService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.questRepository = questRepository;
        this.opponentRepository = opponentRepository;
        this.characterRepository = characterRepository;
        this.itemRepository = itemRepository;
        this.itemShopService = itemShopService;
    }

    @Override
    public void run(String... args) throws Exception {
        OpponentEntity opponent = new OpponentEntity(null, "test", 1, 1, 1, 1, 1, "");
        opponentRepository.save(opponent);
        QuestEntity quest1 = new QuestEntity(null, "test1", "desc1", QuestTier.EASY, QuestType.RIZZ_FIGHT, opponent, "");
        QuestEntity quest2 = new QuestEntity(null, "test2", "desc1", QuestTier.MEDIUM, QuestType.STRENGTH_FIGHT, opponent, "");
        QuestEntity quest3 = new QuestEntity(null, "test3", "desc3", QuestTier.HARD,  QuestType.STRENGTH_FIGHT, opponent, "");
        questRepository.saveAll(List.of(quest1,quest2,quest3));
        UserEntity player = new UserEntity(null,"test","test", passwordEncoder.encode("test"),List.of() );
        userRepository.save(player);
        CharacterEntity character1 = new CharacterEntity(null,player, "test", CharacterClass.NERD,4,3,420,67,67,67,67,10000,67, Set.of(),List.of(), null);
        player.setCharacters(List.of(character1));
        characterRepository.save(character1);

        itemRepository.save(new ItemEntity(null, "item1", "opis", SlotType.HEAD, 1, 1, 1, 1, 1, 1, ""));
        itemRepository.save(new ItemEntity(null, "item2", "opis", SlotType.HEAD, 1, 1, 1, 1, 1, 1, ""));
        itemRepository.save(new ItemEntity(null, "item3", "opis", SlotType.EMBLEM, 1, 1, 1, 1, 67, 1, ""));
        itemRepository.save(new ItemEntity(null, "item4", "opis", SlotType.LOWER_BODY, 1, 1, 1, 1, 1, 1, ""));

        itemShopService.refreshShopOffers();
    }
}
