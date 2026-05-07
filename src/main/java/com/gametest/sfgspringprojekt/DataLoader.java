package com.gametest.sfgspringprojekt;

import com.gametest.sfgspringprojekt.model.Player;
import com.gametest.sfgspringprojekt.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        Player player = new Player(null,"test", passwordEncoder.encode("test"));
        userRepository.save(player);
    }
}
