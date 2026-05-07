package com.gametest.sfgspringprojekt.service;

import com.gametest.sfgspringprojekt.model.Player;
import com.gametest.sfgspringprojekt.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {


    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Player player = userRepository.getByUsername(username);
        if (player == null) throw new UsernameNotFoundException("Nie ma takiego użytkownika!");

        return User.withUsername(player.getUsername())
                .password(player.getPassword())
                .roles("USER")
                .build();
    }
}
