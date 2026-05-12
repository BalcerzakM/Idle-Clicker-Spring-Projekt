package com.gametest.springprojekt;

import com.gametest.springprojekt.model.CharacterEntity;
import com.gametest.springprojekt.model.QuestEntity;
import com.gametest.springprojekt.model.UserEntity;
import com.gametest.springprojekt.model.enums.QuestTier;
import com.gametest.springprojekt.repository.QuestRepository;
import com.gametest.springprojekt.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final QuestRepository questRepository;

    public DataLoader(UserRepository userRepository, PasswordEncoder passwordEncoder,  QuestRepository questRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.questRepository = questRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        UserEntity player = new UserEntity(null,"test","test", passwordEncoder.encode("test"), List.of(new CharacterEntity()));
        QuestEntity quest1 = new QuestEntity(null, "test1", "test1", QuestTier.EASY);
        QuestEntity quest2 = new QuestEntity(null, "test2", "test2", QuestTier.MEDIUM);
        QuestEntity quest3 = new QuestEntity(null, "test3", "test3", QuestTier.HARD);
        questRepository.saveAll(List.of(quest1,quest2,quest3));
        userRepository.save(player);
    }
}
