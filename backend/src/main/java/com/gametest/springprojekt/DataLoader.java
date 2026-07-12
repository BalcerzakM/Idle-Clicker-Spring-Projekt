package com.gametest.springprojekt;

import com.gametest.springprojekt.model.*;
import com.gametest.springprojekt.repository.*;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CharacterClassRepository characterClassRepository;

    public DataLoader(UserRepository userRepository, PasswordEncoder passwordEncoder, CharacterClassRepository characterClassRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.characterClassRepository = characterClassRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        if(userRepository.count()>1){
            return;
        }

        Faker faker = new Faker();

        List<CharacterClassEntity> classes = characterClassRepository.findAll();
        List<UserEntity> users = new ArrayList<>();

        for (int i = 0; i < 3000; i++){
            UserEntity user = new UserEntity();
            user.setUsername(faker.name().name() + "_" + i);
            user.setEmail(faker.internet().emailAddress() + "_" + i);
            user.setRole("USER");
            user.setPassword(passwordEncoder.encode(faker.internet().password()));

            CharacterClassEntity randomClass = classes.get(faker.random().nextInt(classes.size()));
            CharacterEntity character = new CharacterEntity();

            character.setUser(user);
            character.setName(faker.funnyName().name() +  "_" + i);
            character.setCharacterClass(randomClass);
            character.setAvatarPicture("avatar1.png");
            character.setAuraLvl(faker.number().numberBetween(1,200));

            character.setAura(faker.number().numberBetween(1,200));
            character.setMoney(faker.number().numberBetween(1,200));
            character.setCristals(faker.number().numberBetween(1,200));
            character.setRizz(faker.number().numberBetween(1,200));
            character.setStrength(faker.number().numberBetween(1,200));
            character.setAgility(faker.number().numberBetween(1,200));
            character.setEndurance(faker.number().numberBetween(1,200));
            character.setLuck(faker.number().numberBetween(1,200));

            character.setCurrentBoss(1);

            user.setCharacters(List.of(character));

            users.add(user);

            if (users.size() == 1000) {
                userRepository.saveAll(users);
                users.clear();
            }
        }

        if (!users.isEmpty()){
            userRepository.saveAll(users);
        }
    }
}
