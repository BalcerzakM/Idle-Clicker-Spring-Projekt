package com.gametest.springprojekt;

import com.gametest.springprojekt.model.CharacterClassEntity;
import com.gametest.springprojekt.model.CharacterEntity;
import com.gametest.springprojekt.model.UserEntity;
import com.gametest.springprojekt.repository.CharacterClassRepository;
import com.gametest.springprojekt.repository.CharacterRepository;
import com.gametest.springprojekt.repository.UserRepository;
import lombok.AllArgsConstructor;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Profile("dev")
@Component
@AllArgsConstructor
public class DevDataSeeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CharacterRepository characterRepository;
    private final CharacterClassRepository characterClassRepository;


    @Override
    public void run(String... args){
        if(userRepository.count()>1){
            return;
        }

        Faker faker = new Faker();

        List<CharacterClassEntity> classes = characterClassRepository.findAll();

        for(int i=0;i<1000;i++){
            UserEntity user = new UserEntity();
            user.setUsername(faker.name().name() + "_" + i);
            user.setEmail("test" + i + "@example.com");
            user.setPassword(passwordEncoder.encode(faker.internet().password()));

            userRepository.save(user);

            CharacterClassEntity randomClass = classes.get(faker.random().nextInt(classes.size()));

            CharacterEntity character = new CharacterEntity();

            character.setUser(user);
            character.setName(faker.funnyName().name() +  "_" + i);

            character.setCharacterClass(randomClass);

            character.setAvatarPicture("avatar1.png");

            character.setAuraLvl(faker.number().numberBetween(1,100));
            character.setAura(faker.number().numberBetween(1,10000));

            character.setMoney(faker.number().numberBetween(1,50000));
            character.setCristals(faker.number().numberBetween(1,5000));

            character.setRizz(randomClass.getBaseRizz());
            character.setStrength(randomClass.getBaseStrength());
            character.setAgility(randomClass.getBaseAgility());
            character.setEndurance(randomClass.getBaseEndurance());
            character.setLuck(randomClass.getBaseLuck());

            characterRepository.save(character);
        }
    }
}
