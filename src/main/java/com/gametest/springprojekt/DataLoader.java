package com.gametest.springprojekt;

import com.gametest.springprojekt.model.CharacterEntity;
import com.gametest.springprojekt.model.OpponentEntity;
import com.gametest.springprojekt.model.QuestEntity;
import com.gametest.springprojekt.model.UserEntity;
import com.gametest.springprojekt.model.enums.CharacterClass;
import com.gametest.springprojekt.model.enums.QuestTier;
import com.gametest.springprojekt.model.enums.QuestType;
import com.gametest.springprojekt.repository.CharacterRepository;
import com.gametest.springprojekt.repository.OpponentRepository;
import com.gametest.springprojekt.repository.QuestRepository;
import com.gametest.springprojekt.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.EnableAsync;
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

    public DataLoader(UserRepository userRepository, PasswordEncoder passwordEncoder,  QuestRepository questRepository,  OpponentRepository opponentRepository, CharacterRepository characterRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.questRepository = questRepository;
        this.opponentRepository = opponentRepository;
        this.characterRepository = characterRepository;
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
        CharacterEntity character1 = new CharacterEntity(null,player, "test", CharacterClass.NERD,4,67,67,67,67,67,67,67, Set.of(),List.of(), quest1);
        player.setCharacters(List.of(character1));
        characterRepository.save(character1);
    }
}
