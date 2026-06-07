package com.gametest.springprojekt;

import com.gametest.springprojekt.model.*;
import com.gametest.springprojekt.repository.*;
import com.gametest.springprojekt.service.ItemShopService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CharacterRepository characterRepository;
    private final ItemShopService itemShopService;
    private final CharacterClassRepository characterClassRepository;

    public DataLoader(UserRepository userRepository, PasswordEncoder passwordEncoder, CharacterRepository characterRepository, ItemShopService itemShopService, CharacterClassRepository characterClassRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.characterRepository = characterRepository;
        this.itemShopService = itemShopService;
        this.characterClassRepository = characterClassRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        UserEntity player = new UserEntity(null,"test","test", passwordEncoder.encode("test"),List.of());
        userRepository.save(player);
        CharacterClassEntity characterClass= new CharacterClassEntity(null, "TEST", 10, 10, 10, 10, 10, 10);
        characterClassRepository.save(characterClass);
        CharacterEntity character1 = new CharacterEntity(null,player, "test", characterClass,"avatar3.png",1,1000,420,67,67,67,67,10000,67, List.of(),List.of(), null, null);
        player.setCharacters(List.of(character1));
        characterRepository.save(character1);

        itemShopService.refreshShopOffers();
    }
}
