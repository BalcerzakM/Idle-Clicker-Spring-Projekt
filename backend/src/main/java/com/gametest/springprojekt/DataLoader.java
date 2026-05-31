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
    private final ItemShopService itemShopService;

    public DataLoader(UserRepository userRepository, PasswordEncoder passwordEncoder,  QuestRepository questRepository,  OpponentRepository opponentRepository, CharacterRepository characterRepository, ItemShopService itemShopService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.questRepository = questRepository;
        this.opponentRepository = opponentRepository;
        this.characterRepository = characterRepository;
        this.itemShopService = itemShopService;
    }

    @Override
    public void run(String... args) throws Exception {

        UserEntity player = new UserEntity(null,"test","test", passwordEncoder.encode("test"),List.of() );
        userRepository.save(player);
        CharacterEntity character1 = new CharacterEntity(null,player, "test", CharacterClass.NERD,"avatar3.png",4,3,420,67,67,67,67,10000,67, List.of(),List.of(), null);
        player.setCharacters(List.of(character1));
        characterRepository.save(character1);

        itemShopService.refreshShopOffers();
    }
}
