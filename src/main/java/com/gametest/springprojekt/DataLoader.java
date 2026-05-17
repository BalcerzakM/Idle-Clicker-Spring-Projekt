package com.gametest.springprojekt;

import com.gametest.springprojekt.model.CharacterEntity;
import com.gametest.springprojekt.model.OpponentEntity;
import com.gametest.springprojekt.model.QuestEntity;
import com.gametest.springprojekt.model.UserEntity;
import com.gametest.springprojekt.model.enums.QuestTier;
import com.gametest.springprojekt.model.enums.QuestType;
import com.gametest.springprojekt.repository.OpponentRepository;
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
    private final OpponentRepository opponentRepository;

    public DataLoader(UserRepository userRepository, PasswordEncoder passwordEncoder,  QuestRepository questRepository,  OpponentRepository opponentRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.questRepository = questRepository;
        this.opponentRepository = opponentRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        UserEntity player = new UserEntity(null,"test","test", passwordEncoder.encode("test"), List.of(new CharacterEntity()));
        OpponentEntity opponent = new OpponentEntity(null, "test", 1, 1, 1, 1, 1);
        opponentRepository.save(opponent);
        QuestEntity quest1 = new QuestEntity(null, "test1", "desc1", QuestTier.EASY, QuestType.RIZZ_FIGHT, opponent);
        QuestEntity quest2 = new QuestEntity(null, "test2", "desc1", QuestTier.MEDIUM, QuestType.STRENGTH_FIGHT, opponent);
        QuestEntity quest3 = new QuestEntity(null, "test3", "desc3", QuestTier.HARD,  QuestType.STRENGTH_FIGHT, opponent);
        questRepository.saveAll(List.of(quest1,quest2,quest3));
        userRepository.save(player);
    }
}
